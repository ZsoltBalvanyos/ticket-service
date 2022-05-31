package com.zsoltbalvanyos.ticket.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.io.Serializable;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@IdClass(BookingStatus.class)
public class BookingStatus implements Serializable {

    @Id
    private long bookingId;

    @Id
    @Enumerated(EnumType.STRING)
    private BookingState status;

}
