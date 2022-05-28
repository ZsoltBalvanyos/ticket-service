package com.zsoltbalvanyos.core.repositories;

import com.zsoltbalvanyos.core.entities.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTokenRepository extends JpaRepository<UserToken, UserToken> {
}
