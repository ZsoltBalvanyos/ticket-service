package com.zsoltbalvanyos.core.repositories;

import com.zsoltbalvanyos.core.entities.Partner;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface PartnerRepository extends CrudRepository<Partner, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Partner> findAndLockByPartnerId(long partnerId);
}
