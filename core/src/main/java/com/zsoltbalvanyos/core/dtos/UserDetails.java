package com.zsoltbalvanyos.core.dtos;

import lombok.Builder;
import lombok.Value;
import lombok.With;

import java.util.Optional;

@Value
@Builder
@With
public class UserDetails {
    long userId;
    String email;
    String deviceHash;
    long cardId;

    public Optional<Long> getCardId() {
        return cardId == 0 ? Optional.empty() : Optional.of(cardId);
    }
}
