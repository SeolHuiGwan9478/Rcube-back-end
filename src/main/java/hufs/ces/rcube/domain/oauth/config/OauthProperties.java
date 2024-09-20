package hufs.ces.rcube.domain.oauth.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@Getter
@ConfigurationProperties(prefix = "oauth") //외부 프로퍼티 값을 객체로 바인딩(yml에 있는 값을 자바 객체로 바인딩)
@AllArgsConstructor
public class OauthProperties {
    private final Map<String, User> user = new HashMap<>();   //map으로 정의한 이유는 여러 oAuth 제공자에 대해 동적으로 정보를 저장할 수 있음, key값은 둘 다 구글

    private final Map<String, Provider> provider = new HashMap<>();


    @Getter
    @Setter
    public static  class User{   //static 필드로 둠으로써 값을 바인딩 받을 수 있는 상태가 됨
        private String clientId;
        private String clientSecret;
        private String redirectUrl;
    }
    @Getter
    @Setter
    public static class Provider{
        private String tokenUrl;
        private String userInfoUrl;

    }
}
