package com.zsoltbalvanyos.ticket.repositories;

import com.zsoltbalvanyos.ticket.entities.Partner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

public interface PartnerRepository extends JpaRepository<Partner, Long> {

    @Query("SELECT p FROM Partner p, PromotedEvent pe WHERE p.partnerId = pe.partnerId")
    Set<Partner> getPartnersWithActiveEvent();

    @Query("SELECT p FROM Partner p, PromotedEvent pe WHERE p.partnerId = pe.partnerId AND pe.eventId = :eventId")
    Optional<Partner> getPartnerOfEvent(@Param("eventId") Long eventId);
}
