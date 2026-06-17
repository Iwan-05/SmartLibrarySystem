package com.csc3402.smartlibrarysystem.repository;

import com.csc3402.smartlibrarysystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>    {
    User findByUsername(String username);
    List<User> findAllByRole(String role);
}
