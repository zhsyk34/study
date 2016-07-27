package com.cat.service.impl;

import com.cat.model.User;

/**
 * Created by Archimedes on 2016/07/21.
 */
public interface UserService {

    User login(String name, String password);
}
