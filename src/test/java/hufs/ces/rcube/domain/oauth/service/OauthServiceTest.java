package hufs.ces.rcube.domain.oauth.service;

import hufs.ces.rcube.domain.member.entity.Member;
import hufs.ces.rcube.domain.member.repository.MemberRepository;
import hufs.ces.rcube.domain.oauth.domain.OauthProvider;
import hufs.ces.rcube.domain.oauth.dto.LoginResponse;
import hufs.ces.rcube.domain.oauth.dto.OauthTokenResponse;
import hufs.ces.rcube.domain.oauth.dto.UserProfile;
import hufs.ces.rcube.domain.oauth.repository.InMemoryProviderRepository;
import hufs.ces.rcube.domain.oauth.security.JwtTokenProvider;
import hufs.ces.rcube.domain.oauth.service.OauthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Optional;

import static hufs.ces.rcube.domain.member.entity.Role.USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OauthServiceTest {

    @InjectMocks
    private OauthService oauthService;


    @Mock
    private InMemoryProviderRepository providerRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpecMock;

    @Mock
    private WebClient.RequestHeadersUriSpec<?> requestHeadersUriSpecMock;

    @Mock
    private WebClient.RequestHeadersSpec<?> requestHeadersSpecMock;

    @Mock
    private WebClient.ResponseSpec responseSpecMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogin() {
        // given
        String providerName = "google";
        String code = "authCode";

        OauthProvider provider = OauthProvider.builder()
                .clientId("clientId")
                .clientSecret("clientSecret")
                .tokenUrl("tokenUrl")
                .userInfoUrl("userInfoUrl")
                .redirectUrl("redirectUrl")
                .build();

        OauthTokenResponse tokenResponse = OauthTokenResponse.builder()
                .accessToken("accessToken")
                .scope("scope")
                .tokenType("tokenType")
                .build();

        UserProfile userProfile = UserProfile.builder()
                .oauthId("oauthId")
                .email("email")
                .name("name")
                .imageUrl("imageUrl")
                .build();

        Member member = Member.builder()
                .id(1L)
                .oauthId("oauthId")
                .name("name")
                .email("email")
                .imageUrl("imageUrl")
                .role(USER)
                .build();

        // Mocking behavior
        when(providerRepository.findByProviderName(providerName)).thenReturn(provider);
        when(webClient.post()).thenReturn(requestBodyUriSpecMock);
        when(requestBodyUriSpecMock.uri(any(URI.class))).thenReturn(requestBodyUriSpecMock);
        when(requestBodyUriSpecMock.headers(any())).thenReturn(requestBodyUriSpecMock);
        when(requestBodyUriSpecMock.retrieve()).thenReturn(responseSpecMock); // retrieve() 부분 수정
        when(responseSpecMock.bodyToMono(OauthTokenResponse.class)).thenReturn(Mono.just(tokenResponse));

        // Mocking getUserProfile 호출
        when(oauthService.getUserProfile(providerName, tokenResponse, provider)).thenReturn(userProfile);

        when(memberRepository.findByOauthId(userProfile.getOauthId())).thenReturn(Optional.of(member));
        when(jwtTokenProvider.createAccessToken(String.valueOf(member.getId()))).thenReturn("jwtAccessToken");
        when(jwtTokenProvider.createRefreshToken()).thenReturn("jwtRefreshToken");

        // when
        LoginResponse response = oauthService.login(providerName, code);

        // then
        assertEquals(member.getId(), response.getId());
        assertEquals(member.getName(), response.getName());
        assertEquals(member.getEmail(), response.getEmail());
        assertEquals(member.getImageUrl(), response.getImageUrl());
        assertEquals(member.getRole(), response.getRole());
        assertEquals("Bearer", response.getTokenType());
        assertEquals("jwtAccessToken", response.getAccessToken());
        assertEquals("jwtRefreshToken", response.getRefreshToken());
    }
}
