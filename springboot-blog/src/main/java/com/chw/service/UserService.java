package com.chw.service;

import com.chw.pojo.User;

public interface UserService {
    User checkUser(String username, String password);
}
