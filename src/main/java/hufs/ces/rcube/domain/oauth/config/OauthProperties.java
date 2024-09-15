package hufs.ces.rcube.domain.oauth.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@Getter
@ConfigurationProperties(prefix = "oauth2") //프로퍼티 값을 객체로 바인딩
@AllArgsConstructor
public class OauthProperties {
    private final Map<String, User> user = new HashMap<>();

    private final Map<String, Provider> provider = new HashMap<>();

    @Getter
    @Setter
    public static  class User{
        private String clientId;
        private String clientSecret;
        private String redirectUrl;
    }
    @Getter
    @Setter
    public static class Provider{
        private String tokenUrl;
        private String userInfoUrl;
        private String userNameAttribute;

    }
}
