package hufs.ces.rcube.domain.oauth.utill;

import hufs.ces.rcube.domain.oauth.config.OauthProperties;
import hufs.ces.rcube.domain.oauth.domain.OauthProvider;

import java.util.HashMap;
import java.util.Map;

public class OauthAdapter {

    // OauthProperties를 OauthProvider로 변환해준다. user와 provider를 하나로 합침, 분리해놓음으로서 역할을 분리하고, 확장성,정보가 독립적관리되어 변화에 더 유연
    public static Map<String, OauthProvider> getOauthProviders(OauthProperties properties) {
        Map<String, OauthProvider> oauthProviderMap = new HashMap<>();

        properties.getUser().forEach((key, user) -> {
            OauthProperties.Provider provider = properties.getProvider().get(key); //구글에 해당하는 provider정보 가져오기
            if (provider != null) {//클라이언트 정보말고 provider정보가 있어야 의미가 있으므로
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
