package com.cat.service;

import com.cat.dao.UserDao;
import com.cat.model.User;
import com.cat.service.impl.UserService;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by Archimedes on 2016/07/21.
 */
@Component
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Override
    public User login(String name, String password) {
        User user = userDao.find(name);
        if (user == null) {
            return null;
        }
        user.getPassword().equals(password);
        return user;
    }
}
