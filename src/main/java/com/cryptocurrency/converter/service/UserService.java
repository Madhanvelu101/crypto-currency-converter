package com.cryptocurrency.converter.service;


import com.cryptocurrency.converter.entity.User;

public interface UserService {
    void save(User user);

    User findByUsername(String username);
}
