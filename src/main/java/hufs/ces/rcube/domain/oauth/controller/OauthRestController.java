package hufs.ces.rcube.domain.oauth.controller;

import hufs.ces.rcube.domain.oauth.dto.LoginResponse;
import hufs.ces.rcube.domain.oauth.service.OauthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class OauthRestController {

    private final OauthService oauthService;

    @GetMapping("/login/oauth/{provider}")
    public ResponseEntity<LoginResponse> login(@PathVariable String provider, @RequestParam String code) {
        LoginResponse loginResponse = oauthService.login(provider, code);  // 변수 선언과 메서드 호출
        return ResponseEntity.ok().body(loginResponse);  // 정상적으로 응답 반환
    }
}
