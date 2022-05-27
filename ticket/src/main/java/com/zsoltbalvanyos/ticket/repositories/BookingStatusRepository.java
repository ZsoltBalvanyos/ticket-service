package com.zsoltbalvanyos.ticket.repositories;

import com.zsoltbalvanyos.ticket.entities.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingStatusRepository extends JpaRepository<BookingStatus, Long> {
}
