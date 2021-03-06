package com.chw.dao;

import com.chw.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository <User,Long> {
    User findByUsernameAndPassword(String username, String password);
}
