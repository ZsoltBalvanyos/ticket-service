package com.zsoltbalvanyos.core.repositories;

import com.zsoltbalvanyos.core.entities.PartnerBalance;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface PartnerRepository extends CrudRepository<PartnerBalance, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<PartnerBalance> findAndLockByPartnerId(long partnerId);
}
