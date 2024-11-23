package hufs.ces.rcube.domain.oauth.config;

import hufs.ces.rcube.domain.oauth.domain.OauthProvider;
import hufs.ces.rcube.domain.oauth.repository.InMemoryProviderRepository;
import hufs.ces.rcube.domain.oauth.utill.OauthAdapter;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@AllArgsConstructor
@EnableConfigurationProperties(OauthProperties.class) //@ConfigurationProperties룰 사용하는 클래스를 활성화, OauthProperties 클래스를 빈으로 등록하고 활성화해줌
public class OauthConfig {                            //프로퍼티 파일에 적어준 정보가 하나의 OauthProperties 객체로 만들어짐

    private final OauthProperties properties;


    @Bean
    public InMemoryProviderRepository inMemoryProviderRepository(){
        Map<String, OauthProvider> providers = OauthAdapter.getOauthProviders(properties);
        return new InMemoryProviderRepository((providers));
    }




}
