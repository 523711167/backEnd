package org.pindaodao.Controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @RequestMapping("/api/helloWorld")
    public String helloWorld() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("authentication = " + authentication);
        return "Hello World";
    }
}
