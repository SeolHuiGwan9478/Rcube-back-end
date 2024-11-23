package hufs.ces.rcube.domain.oauth.repository;

import hufs.ces.rcube.domain.oauth.domain.OauthProvider;
import lombok.AllArgsConstructor;

import java.util.Map;
@AllArgsConstructor
public class InMemoryProviderRepository { //상태 비저정성과 서버 장애시 , 정보가 소실될수잇음

    private final Map<String, OauthProvider> providers;
    public OauthProvider findByProviderName(String name){
        return providers.get(name);
    } //구글에 해당하는 정보 반환

}
