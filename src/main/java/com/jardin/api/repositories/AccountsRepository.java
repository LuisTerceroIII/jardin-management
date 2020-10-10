package com.jardin.api.repositories;

import com.jardin.api.model.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountsRepository extends JpaRepository<Account,Long> {
}
