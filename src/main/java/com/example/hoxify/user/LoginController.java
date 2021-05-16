package com.example.hoxify.user;

import com.example.hoxify.shared.CurrentUser;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
public class LoginController {
    @PostMapping("/api/1.0/login")
    Map<String, Object> handleLogin(@CurrentUser User logedInUser){
        return Collections.singletonMap("id",logedInUser.getId());
    }

}
