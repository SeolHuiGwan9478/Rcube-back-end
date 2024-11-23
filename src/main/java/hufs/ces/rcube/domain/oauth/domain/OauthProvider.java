package hufs.ces.rcube.domain.oauth.domain;

import hufs.ces.rcube.domain.oauth.config.OauthProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OauthProvider {
    private final String clientId;
    private final String clientSecret;
    private final String redirectUrl;
    private final String tokenUrl;
    private final String userInfoUrl;






}