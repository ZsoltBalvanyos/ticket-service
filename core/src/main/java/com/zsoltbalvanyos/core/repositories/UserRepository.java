package com.zsoltbalvanyos.core.repositories;

import com.zsoltbalvanyos.core.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
