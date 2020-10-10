package com.jardin.api.repositories;

import com.jardin.api.model.entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRolesRepository extends JpaRepository<UserRole,Long> {
}
