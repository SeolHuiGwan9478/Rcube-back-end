package hufs.ces.rcube.domain.oauth.util;

import hufs.ces.rcube.domain.oauth.config.OauthProperties;
import hufs.ces.rcube.domain.oauth.domain.OauthProvider;

import java.util.HashMap;
import java.util.Map;

public class OauthAdapter {

    // OauthProperties를 OauthProvider로 변환해준다.
    public static Map<String, OauthProvider> getOauthProviders(OauthProperties properties) {
        Map<String, OauthProvider> oauthProviderMap = new HashMap<>();

        properties.getUser().forEach((key, user) -> {
            OauthProperties.Provider provider = properties.getProvider().get(key);
            if (provider != null) {
                OauthProvider oauthProvider = OauthProvider.builder()
                        .clientId(user.getClientId())
                        .clientSecret(user.getClientSecret())
                        .redirectUrl(user.getRedirectUrl())
                        .tokenUrl(provider.getTokenUrl())
                        .userInfoUrl(provider.getUserInfoUrl())
                        .build();
                oauthProviderMap.put(key, oauthProvider);
            }
        });

        return oauthProviderMap;
    }
}
