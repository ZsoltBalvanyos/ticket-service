package com.zsoltbalvanyos.core.repositories;

import com.zsoltbalvanyos.core.entities.ReservedAmount;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface ReservedAmountRepository extends CrudRepository<ReservedAmount, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<ReservedAmount> findAndLockByTransactionId(long transactionId);
}
