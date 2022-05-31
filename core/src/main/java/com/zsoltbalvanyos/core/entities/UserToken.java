package com.zsoltbalvanyos.core.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.io.Serializable;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@IdClass(UserToken.class)
public class UserToken implements Serializable {

    @Id
    private long userId;
    @Id
    private String token;
}
