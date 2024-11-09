package hufs.ces.rcube.domain.oauth.service;

import hufs.ces.rcube.domain.exception.CommonErrorCode;
import hufs.ces.rcube.domain.exception.RestApiException;
import hufs.ces.rcube.domain.member.entity.Member;
import hufs.ces.rcube.domain.oauth.dto.LoginResponse;
import hufs.ces.rcube.domain.oauth.dto.OauthTokenResponse;
import hufs.ces.rcube.domain.oauth.domain.OauthProvider;
import hufs.ces.rcube.domain.oauth.dto.UserProfile;
import hufs.ces.rcube.domain.oauth.repository.InMemoryProviderRepository;
import hufs.ces.rcube.domain.oauth.utill.OauthAttributes;
import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import hufs.ces.rcube.domain.oauth.security.JwtTokenProvider;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import hufs.ces.rcube.domain.member.repository.MemberRepository;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class OauthService {
    private final InMemoryProviderRepository inMemoryProviderRepository;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    // WebClient 인스턴스를 재사용
    private final WebClient webClient = WebClient.builder().build();

    public LoginResponse login(String providerName, String code) {
        // 제공자 정보 가져오기
        OauthProvider provider = inMemoryProviderRepository.findByProviderName(providerName);

        // Access Token 가져오기
        OauthTokenResponse tokenResponse = getToken(code, provider);

        // 유저 정보 가져오기
        UserProfile userProfile = getUserProfile(providerName, tokenResponse, provider);

        // 유저 DB에 저장 또는 업데이트
        Member member = saveOrUpdate(userProfile);

        // JWT 토큰 생성
        String accessToken = jwtTokenProvider.createAccessToken(String.valueOf(member.getId()));
        String refreshToken = jwtTokenProvider.createRefreshToken();

        return LoginResponse.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .imageUrl(member.getImageUrl())
                .role(member.getRole())
                .tokenType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // Access Token을 받아오는 메서드
    protected OauthTokenResponse getToken(String code, OauthProvider provider) {
        return webClient.post()
                .uri(provider.getTokenUrl())
                .headers(header -> {
                    header.setBasicAuth(provider.getClientId(), provider.getClientSecret());
                    header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                    header.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
                    header.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
                })
                .bodyValue(tokenRequest(code, provider))
                .retrieve()
                .onStatus(status -> !status.is2xxSuccessful(), clientResponse ->
                        Mono.error(new RestApiException(CommonErrorCode.FAILED_TO_RETRIEVE_ACCESS_TOKEN, "Access token을 가져오지 못했습니다: " + clientResponse.statusCode()))
                )
                .bodyToMono(OauthTokenResponse.class)
                .block();
    }

    private Member saveOrUpdate(UserProfile userProfile) {
        Member member = memberRepository.findByOauthId(userProfile.getOauthId())
                .map(entity -> entity.update(
                        userProfile.getEmail(), userProfile.getName(), userProfile.getImageUrl()))
                .orElseGet(userProfile::toMember);
        return memberRepository.save(member);
    }

    // 토큰 요청에 필요한 파라미터 설정 메서드
    private MultiValueMap<String, String> tokenRequest(String code, OauthProvider provider) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("code", code);
        formData.add("grant_type", "authorization_code");
        formData.add("redirect_uri", provider.getRedirectUrl());
        return formData;
    }

    public UserProfile getUserProfile(String providerName, OauthTokenResponse tokenResponse, OauthProvider provider) {
        Map<String, Object> userAttributes = getUserAttributes(provider, tokenResponse);
        return OauthAttributes.extract(providerName, userAttributes);
    }

    // Oauth 서버에서 유저 정보 가져오기
    private Map<String, Object> getUserAttributes(OauthProvider provider, OauthTokenResponse tokenResponse) {
        return webClient.get()
                .uri(provider.getUserInfoUrl())
                .headers(header -> header.setBearerAuth(tokenResponse.getAccessToken()))
                .retrieve()
                .onStatus(status -> !status.is2xxSuccessful(), clientResponse ->
                        Mono.error(new RestApiException(CommonErrorCode.FAILED_TO_RETRIEVE_USER_ATTRIBUTES, "유저 정보를 가져오지 못했습니다: " + clientResponse.statusCode()))
                )
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }
}
