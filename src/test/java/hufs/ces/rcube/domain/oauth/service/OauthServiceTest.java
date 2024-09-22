package hufs.ces.rcube.domain.oauth.service;
import hufs.ces.rcube.domain.member.entity.Member;
import hufs.ces.rcube.domain.member.entity.Role;
import hufs.ces.rcube.domain.oauth.dto.LoginResponse;
import hufs.ces.rcube.domain.oauth.dto.OauthTokenResponse;
import hufs.ces.rcube.domain.oauth.dto.UserProfile;
import hufs.ces.rcube.domain.oauth.domain.OauthProvider;
import hufs.ces.rcube.domain.oauth.repository.InMemoryProviderRepository;
import hufs.ces.rcube.domain.oauth.service.OauthService;
import hufs.ces.rcube.domain.oauth.security.JwtTokenProvider;
import hufs.ces.rcube.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static hufs.ces.rcube.domain.member.entity.Role.USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) //junit과 mockito연동
public class OauthServiceTest {

    @InjectMocks //인스터스 생성및 필드 주입
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
    private WebClient.Builder webClientBuilder; //웹 클라이언트 빌더를 통해 웹클라이언트 인스턴스 생성해줌


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(webClientBuilder.build()).thenReturn(webClient); //인스턴스 생성대신 모킹된 웹클라이언트(가짜 객체)를 생성하도록함
    } //이를 통해 네트워크 호출 없이 테스트 수행 가능

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
        //특정메서드 호출 시 특정 값을 반환하도록 설정, 실제 로직을 호출하지 않고도 테스트할 수 있음.

        when(providerRepository.findByProviderName(providerName)).thenReturn(provider);
        when(oauthService.getToken(code, provider)).thenReturn(tokenResponse);
        when(oauthService.getUserProfile(providerName, tokenResponse, provider)).thenReturn(userProfile);
        when(memberRepository.findByOauthId(userProfile.getOauthId())).thenReturn(Optional.of(member)); //없으면 empty반환
        when(jwtTokenProvider.createAccessToken(String.valueOf(member.getId()))).thenReturn("jwtAccessToken");
        when(jwtTokenProvider.createRefreshToken()).thenReturn("jwtRefreshToken");

        // when
        LoginResponse response = oauthService.login(providerName, code);


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
