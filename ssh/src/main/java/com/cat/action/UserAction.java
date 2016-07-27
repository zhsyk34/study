package com.cat.action;

import com.cat.model.User;
import com.cat.service.impl.UserService;
import com.opensymphony.xwork2.ActionSupport;

import javax.annotation.Resource;

/**
 * Created by Archimedes on 2016/07/21.
 */
public class UserAction extends ActionSupport {

    @Resource
    private UserService userService;
    private String name;
    private String password;
    private String message;

    //http://localhost:8080/ssh/User_login?name=zsy&password=abcd
    public String login() {
        User user = userService.login(name, password);

        message = user == null ? "error" : "sucess";
        return SUCCESS;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
