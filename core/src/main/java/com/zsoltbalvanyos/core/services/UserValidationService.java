package com.zsoltbalvanyos.core.services;

import com.zsoltbalvanyos.core.entities.UserDevice;
import com.zsoltbalvanyos.core.entities.UserToken;
import com.zsoltbalvanyos.core.exceptions.InvalidUserTokenException;
import com.zsoltbalvanyos.core.exceptions.UserException;
import com.zsoltbalvanyos.core.repositories.UserDeviceRepository;
import com.zsoltbalvanyos.core.repositories.UserRepository;
import com.zsoltbalvanyos.core.repositories.UserTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Base64;

import static com.zsoltbalvanyos.core.exceptions.UserException.Error.USER_ID_NOT_FOUND;
import static com.zsoltbalvanyos.core.exceptions.UserException.Error.USER_ID_EMAIL_ADDRESS_MISMATCH;
import static com.zsoltbalvanyos.core.exceptions.UserException.Error.UNREGISTERED_DEVICE;

@Component
@RequiredArgsConstructor
public class UserValidationService {

    private final UserRepository userRepository;
    private final UserTokenRepository userTokenRepository;
    private final UserDeviceRepository userDeviceRepository;

    public void validate(String token) {

        var parts = new String(Base64.getDecoder().decode(token)).split("&");

        if (parts.length != 3) {
            throw new InvalidUserTokenException();
        }

        try {
            var userId = Long.parseLong(parts[1]);
            var email = parts[0];
            var deviceHash = parts[2];

            validateUser(userId, email);
            validateToken(userId, token);
            validateDevice(userId, deviceHash);
        } catch (NumberFormatException e) {
            throw new InvalidUserTokenException();
        }
    }

    private void validateUser(long userId, String email) {
        var user = userRepository.findById(userId)
            .orElseThrow(() -> new UserException(USER_ID_NOT_FOUND));

        if (user.getEmail().equalsIgnoreCase(email)) {
            throw new UserException(USER_ID_EMAIL_ADDRESS_MISMATCH);
        }
    }

    private void validateToken(long userId, String token) {
        var userToken = UserToken.builder()
            .userId(userId)
            .token(token)
            .build();

        if (!userTokenRepository.existsById(userToken)) {
            throw new InvalidUserTokenException();
        }
    }

    private void validateDevice(long userId, String deviceHash) {
        var userDevice = UserDevice.builder()
            .userId(userId)
            .deviceHash(deviceHash)
            .build();

        if (!userDeviceRepository.existsById(userDevice)) {
            throw new UserException(UNREGISTERED_DEVICE);
        }
    }
}
