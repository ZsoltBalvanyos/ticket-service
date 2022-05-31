package com.zsoltbalvanyos.core.controllers;

import com.zsoltbalvanyos.core.exceptions.CoreException;
import com.zsoltbalvanyos.core.exceptions.ErrorCode;
import com.zsoltbalvanyos.core.services.UserValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserValidationService userValidationService;

    /**
     * Validates the user token expected in the header of each request.
     * If the request is for a payment operation, the card id is also validated.
     *
     * @param token the user token
     * @param cardId the card id of the user
     */
    @GetMapping("/validate")
    public void validate(
        @RequestHeader("User-Token") Optional<String> token,
        @RequestParam("cardId") Optional<Long> cardId
        ) {
        if (token.isEmpty()) {
            throw new CoreException(ErrorCode.TOKEN_NOT_FOUND);
        }

        if (cardId.isPresent()) {
            userValidationService.validate(token.get(), cardId.get());
        } else {
            userValidationService.validate(token.get());
        }
    }
}
