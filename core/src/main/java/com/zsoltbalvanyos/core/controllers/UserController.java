package com.zsoltbalvanyos.core.controllers;

import com.zsoltbalvanyos.core.services.UserValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserValidationService userValidationService;

    @GetMapping("/validate")
    public void validate(
        @RequestParam("token") String token
    ) {
        userValidationService.validate(token);
    }
}
