package com.myonlineshop.myonlineshop.persistence;

import com.myonlineshop.myonlineshop.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByOwnerId(Long OwnerId);
}
