package com.zsoltbalvanyos.core.repositories;

import com.zsoltbalvanyos.core.entities.UserDevice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDeviceRepository extends JpaRepository<UserDevice, UserDevice> {
}
