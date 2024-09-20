package hufs.ces.rcube.domain.oauth.utill;

import hufs.ces.rcube.domain.oauth.dto.UserProfile;
import java.util.Map;

public enum OauthAttributes {
    GOOGLE("google") {
        @Override
        public UserProfile of(Map<String, Object> attributes) {
            return UserProfile.builder()
                    .oauthId(String.valueOf(attributes.get("sub")))
                    .email((String) attributes.get("email"))
                    .name((String) attributes.get("name"))
                    .imageUrl((String) attributes.get("picture"))
                    .build();
        }
    };

    private final String providerName;

    OauthAttributes(String name) {
        this.providerName = name;
    }

    public static UserProfile extract(String providerName, Map<String, Object> attributes) {
        if (!providerName.equals(GOOGLE.providerName)) {
            throw new IllegalArgumentException("Unsupported provider: " + providerName);
        }
        return GOOGLE.of(attributes);
    }

    public abstract UserProfile of(Map<String, Object> attributes);
}
