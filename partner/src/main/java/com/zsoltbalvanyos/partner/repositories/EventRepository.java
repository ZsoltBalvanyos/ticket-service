package com.zsoltbalvanyos.partner.repositories;

import com.zsoltbalvanyos.partner.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
