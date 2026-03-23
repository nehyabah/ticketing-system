package com.pm.ticketing.repository;

import com.pm.ticketing.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
