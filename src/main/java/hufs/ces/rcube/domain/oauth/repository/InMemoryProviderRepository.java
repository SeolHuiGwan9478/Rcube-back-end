package hufs.ces.rcube.domain.oauth.repository;

import hufs.ces.rcube.domain.oauth.domain.OauthProvider;
import lombok.AllArgsConstructor;

import java.util.Map;
@AllArgsConstructor
public class InMemoryProviderRepository {
    private final Map<String, OauthProvider> providers;
    public OauthProvider findByProviderName(String name){
        return providers.get(name);
    }

}
