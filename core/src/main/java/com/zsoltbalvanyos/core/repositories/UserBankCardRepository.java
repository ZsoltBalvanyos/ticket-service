package com.zsoltbalvanyos.core.repositories;

import com.zsoltbalvanyos.core.entities.UserBankCard;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface UserBankCardRepository extends CrudRepository<UserBankCard, String> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<UserBankCard> findAndLockByCardId(@NonNull String cardId);
}
