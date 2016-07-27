package com.cat.dao;

import com.cat.model.User;

import java.util.List;

/**
 * Created by Archimedes on 2016/07/21.
 */
public interface UserDao {

    void save(User user);

    void delete(Integer id);

    void delete(User user);

    void delete(Integer[] ids);

    void delete(List<User> users);

    void update(User user);

    void merge(User user);

    User find(Integer id);

    //准确查询
    User find(String name);

    List<User> findList();

    //模糊查询
    List<User> findList(String name);

    List<User> findList(String name, String sort, String order, int pageNo, int pageSize);

    int count(String name);
}
