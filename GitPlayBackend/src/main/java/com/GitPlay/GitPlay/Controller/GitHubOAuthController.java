package com.GitPlay.GitPlay.Controller;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GitHubOAuthController {

    @GetMapping("/login")
    public String login() {
        return "Login with GitHub!";
    }

    @GetMapping("/login/oauth2/code/github")
    public String callback(OAuth2User principal) {
        // Access user details
        String username = (String) principal.getAttributes().get("login");
        String email = (String) principal.getAttributes().get("email");
        return "Logged in as: " + username + " with email: " + email;
    }
}
