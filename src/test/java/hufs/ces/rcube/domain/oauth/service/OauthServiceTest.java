package hufs.ces.rcube.domain.oauth.service;
import hufs.ces.rcube.domain.member.entity.Member;
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
import org.mockito.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static hufs.ces.rcube.domain.member.entity.Role.USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
    private WebClient.RequestBodyUriSpec requestBodyUriSpec; // 추가
    @Mock
    private WebClient.RequestBodySpec requestBodySpec; // 추가
    @Mock
    private WebClient.ResponseSpec responseSpec; // 추가

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogin() {
        String providerName = "testProvider";
        String code = "testCode";

        OauthProvider provider = OauthProvider.builder()
                .clientId("testClientId")
                .clientSecret("testClientSecret")
                .tokenUrl("http://localhost/token")
                .userInfoUrl("http://localhost/userinfo")
                .redirectUrl("http://localhost/callback")
                .build();

        when(providerRepository.findByProviderName(providerName)).thenReturn(provider);

        OauthTokenResponse tokenResponse = OauthTokenResponse.builder()
                .accessToken("testAccessToken")
                .tokenType("tokenType")
                .scope("scope")
                .build();

        WebClient.RequestBodyUriSpec requestBodyUriSpec = Mockito.mock(WebClient.RequestBodyUriSpec.class);
        WebClient.RequestBodySpec requestBodySpec = Mockito.mock(WebClient.RequestBodySpec.class);
        WebClient.ResponseSpec responseSpec = Mockito.mock(WebClient.ResponseSpec.class);

        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(provider.getTokenUrl())).thenReturn(requestBodySpec);
        when(requestBodySpec.headers(any())).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any())).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(OauthTokenResponse.class)).thenReturn(Mono.just(tokenResponse));


        UserProfile userProfile = UserProfile.builder()
                .oauthId("testOauthId")
                .name("Test User")
                .email("test@example.com")
                .imageUrl("http://example.com/image.jpg")
                .build();

        when(memberRepository.findByOauthId(userProfile.getOauthId())).thenReturn(java.util.Optional.empty());
        when(memberRepository.save(any(Member.class))).thenReturn(Member.builder().id(1L).email("test@example.com").name("Test User").imageUrl("http://example.com/image.jpg").role(USER).build());

        when(jwtTokenProvider.createAccessToken("1")).thenReturn("testJwtAccessToken");
        when(jwtTokenProvider.createRefreshToken()).thenReturn("testJwtRefreshToken");

        LoginResponse loginResponse = oauthService.login(providerName, code);

        assertEquals("Test User", loginResponse.getName());
        assertEquals("testJwtAccessToken", loginResponse.getAccessToken());
        assertEquals("testJwtRefreshToken", loginResponse.getRefreshToken());
    }
}
