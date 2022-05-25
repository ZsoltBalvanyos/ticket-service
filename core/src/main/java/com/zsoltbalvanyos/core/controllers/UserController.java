package com.zsoltbalvanyos.core.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/validate")
    public String validate(
        @RequestParam("userId") long userId,
        @RequestParam("email") String email,
        @RequestParam("deviceHash") String deviceHash
    ) {
        return "valid user";
    }
}
