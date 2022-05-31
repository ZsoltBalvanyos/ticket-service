package com.zsoltbalvanyos.core.services;

import com.zsoltbalvanyos.core.dtos.UserDetails;
import com.zsoltbalvanyos.core.entities.UserDevice;
import com.zsoltbalvanyos.core.entities.UserToken;
import com.zsoltbalvanyos.core.exceptions.CoreException;
import com.zsoltbalvanyos.core.exceptions.ErrorCode;
import com.zsoltbalvanyos.core.repositories.UserBankCardRepository;
import com.zsoltbalvanyos.core.repositories.UserDeviceRepository;
import com.zsoltbalvanyos.core.repositories.UserRepository;
import com.zsoltbalvanyos.core.repositories.UserTokenRepository;
import com.zsoltbalvanyos.core.utils.PrefixBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserValidationService {

    private final UserRepository userRepository;
    private final UserTokenRepository userTokenRepository;
    private final UserDeviceRepository userDeviceRepository;
    private final UserBankCardRepository userBankCardRepository;

    /**
     * Validates the user token and checks if the provided card id
     * belongs to the user.
     *
     * @param token the user token
     * @param cardId the card id of the user
     * @return the user details extracted from the token
     */
    public UserDetails validate(String token, long cardId) {
        var userDetails = validate(token);
        validateCard(userDetails.getUserId(), cardId);
        return userDetails.withCardId(cardId);
    }

    /**
     * Validates the user token.
     *
     * @param token the user token
     * @return the user details extracted from the token
     */
    public UserDetails validate(String token) {

        var parts = new String(Base64.getDecoder().decode(token)).split("&");

        if (parts.length != 3) {
            throw new CoreException(ErrorCode.INVALID_TOKEN);
        }

        try {
            var userId = Long.parseLong(parts[1]);
            var email = parts[0];
            var deviceHash = parts[2];

            validateUser(userId, email);
            validateToken(userId, token);
            validateDevice(userId, deviceHash);

            return UserDetails.builder()
                .userId(userId)
                .email(email)
                .deviceHash(deviceHash)
                .build();
        } catch (NumberFormatException e) {
            throw new CoreException(ErrorCode.INVALID_TOKEN);
        }
    }

    private void validateUser(long userId, String email) {
        var user = userRepository.findById(userId)
            .orElseThrow(() -> new CoreException(ErrorCode.USER_NOT_FOUND));

        if (!user.getEmail().equalsIgnoreCase(email)) {
            throw new CoreException(ErrorCode.USER_ID_EMAIL_MISMATCH);
        }
    }

    private void validateToken(long userId, String token) {
        var userToken = UserToken.builder()
            .userId(userId)
            .token(token)
            .build();

        if (!userTokenRepository.existsById(userToken)) {
            throw new CoreException(ErrorCode.INVALID_TOKEN);
        }
    }

    private void validateDevice(long userId, String deviceHash) {
        var userDevice = UserDevice.builder()
            .userId(userId)
            .deviceHash(deviceHash)
            .build();

        if (!userDeviceRepository.existsById(userDevice)) {
            throw new CoreException(ErrorCode.UNREGISTERED_DEVICE);
        }
    }

    private void validateCard(long userId, long cardId) {
        var userBankCard = userBankCardRepository.findById(PrefixBuilder.prefixCardId(cardId))
            .orElseThrow(() -> new CoreException(ErrorCode.CARD_NOT_FOUND));

        if (userBankCard.getUserId() != userId) {
            throw new CoreException(ErrorCode.USER_ID_CARD_ID_MISMATCH);
        }
    }
}
