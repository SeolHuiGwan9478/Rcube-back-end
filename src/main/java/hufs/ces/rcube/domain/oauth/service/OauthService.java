package hufs.ces.rcube.domain.oauth.service;

import hufs.ces.rcube.domain.oauth.domain.OauthProvider;
import hufs.ces.rcube.domain.oauth.repository.InMemoryProviderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OauthService {
    private final InMemoryProviderRepository inMemoryProviderRepository;

    public LoginResponse login(String providerName, String code){
        // 프론트에서 넘어온 provider 이름을 통해 InMemoryProviderRepository에서 OauthProvider 가져오기
        OauthProvider provider = inMemoryProviderRepository.findByProviderName(providerName);
        return null;
    }
}
