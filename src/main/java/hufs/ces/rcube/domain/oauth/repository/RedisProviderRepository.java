package hufs.ces.rcube.domain.oauth.repository;

import hufs.ces.rcube.domain.oauth.domain.OauthProvider;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class RedisProviderRepository {
    private final RedisTemplate<String, OauthProvider> redisTemplate;
    public OauthProvider findByProviderName(String providerName) {
        return redisTemplate.opsForValue().get(providerName);
    }
    public void save(String providerName, OauthProvider provider) {
        redisTemplate.opsForValue().set(providerName, provider);
    }
    public void delete(String providerName) {
        redisTemplate.delete(providerName);
    }
}
