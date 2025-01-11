package com.GitPlay.GitPlay.Controller;

import com.GitPlay.GitPlay.Object.Terminal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/user_reg")
public class UserRegistration {

    @Autowired
    private Terminal terminal;

    @PostMapping("/reg")
    public void whereToUse(@RequestParam String useValue) {
        if ("GitHub.com".equals(useValue)) {
            new Thread(() -> {
                try {
                    terminal.executeSingleCommand("gh repo create my-new-repo --public --clone\n");
                    System.out.println("Command executed successfully.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
