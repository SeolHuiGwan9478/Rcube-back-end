package hufs.ces.rcube.domain.oauth.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    private final String secretKey = "yourSecretKey";  // JWT 서명에 사용할 비밀키
    private final long validityInMilliseconds = 3600000; // 1시간 (밀리초)

    // 액세스 토큰 생성
    public String createAccessToken(String userId) {
        Claims claims = Jwts.claims().setSubject(userId);
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // 리프레시 토큰 생성 (별도로 설정 가능)
    public String createRefreshToken() {
        Date now = new Date();
        Date validity = new Date(now.getTime() + (validityInMilliseconds * 24)); // 24시간

        return Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // 토큰에서 사용자 ID 추출
    public String getUserIdFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // 토큰 유효성 확인
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
