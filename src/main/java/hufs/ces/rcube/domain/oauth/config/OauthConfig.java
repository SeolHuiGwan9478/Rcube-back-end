package hufs.ces.rcube.domain.oauth.config;

import hufs.ces.rcube.domain.oauth.domain.OauthProvider;
import hufs.ces.rcube.domain.oauth.repository.InMemoryProviderRepository;
import hufs.ces.rcube.domain.oauth.util.OauthAdapter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@EnableConfigurationProperties(OauthProperties.class)
public class OauthConfig {

    private final OauthProperties properties;

    public OauthConfig(OauthProperties properties) {
        this.properties = properties;
    }

    @Bean
    public InMemoryProviderRepository inMemoryProviderRepository(){
        Map<String, OauthProvider> providers = OauthAdapter.getOauthProviders(properties);
        return new InMemoryProviderRepository((providers))
    }




}
