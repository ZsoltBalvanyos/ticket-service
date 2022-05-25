package com.zsoltbalvanyos.partner.repositories;

import com.zsoltbalvanyos.partner.entities.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    @Query("SELECT r FROM Seat r WHERE r.eventId = :eventId")
    List<Seat> findByEventId(
        @Param("eventId") Long eventId
    );

    @Lock(LockModeType.OPTIMISTIC)
    Optional<Seat> findByEventIdAndSeatId(
        @Param("eventId") Long eventId,
        @Param("seatId") Long seatId
    );
}
