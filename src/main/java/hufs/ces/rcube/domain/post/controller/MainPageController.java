package hufs.ces.rcube.domain.post.controller;

import hufs.ces.rcube.domain.post.dto.MainPageDto;
import hufs.ces.rcube.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/main")
@RequiredArgsConstructor
public class MainPageController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<MainPageDto> getMainPageData() {
        MainPageDto mainPageData = postService.getMainPageData();
        return ResponseEntity.ok(mainPageData);
    }
}
