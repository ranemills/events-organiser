package com.mills.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @RequestMapping
    public Principal user(Principal principal) {
        return principal;
    }

}
