package com.zsoltbalvanyos.ticket.repositories;

import com.zsoltbalvanyos.ticket.entities.PromotedEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromotedEventRepository extends JpaRepository<PromotedEvent, Long> {
}
