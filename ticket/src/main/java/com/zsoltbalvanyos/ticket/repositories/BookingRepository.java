package com.zsoltbalvanyos.ticket.repositories;

import com.zsoltbalvanyos.ticket.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}
