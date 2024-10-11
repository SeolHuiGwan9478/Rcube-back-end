package hufs.ces.rcube.domain.post.controller;

import hufs.ces.rcube.domain.post.entity.TechStack;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TechStackController {

    @PostMapping("/validate-techstack")
    public ResponseEntity<String> validateTechStack(@RequestParam String techStack) {
        if (TechStack.isValid(techStack)) {
            return ResponseEntity.ok("Valid tech stack: " + techStack);
        } else {
            return ResponseEntity.badRequest().body("Invalid tech stack: " + techStack);
        }
    }
}
