package com.cat.dao.impl;

import com.cat.dao.UserDao;
import com.cat.model.User;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import javax.annotation.Resource;

/**
 * Created by Archimedes on 2016/07/21.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:/spring.xml"})
public class UserDaoImplTest {

    @Resource
    private UserDao userDao;

    @Test
    public void save() throws Exception {
        User user = new User("zsy", "abcd");
        userDao.save(user);
    }

    @Test
    public void delete() throws Exception {

    }

    @Test
    public void delete1() throws Exception {

    }

    @Test
    public void delete2() throws Exception {

    }

    @Test
    public void delete3() throws Exception {

    }

    @Test
    public void update() throws Exception {

    }

    @Test
    public void merge() throws Exception {

    }

    @Test
    public void find() throws Exception {

    }

    @Test
    public void find1() throws Exception {

    }

    @Test
    public void findList() throws Exception {

    }

    @Test
    public void findList1() throws Exception {
        List<User> list = userDao.findList("z", "name", "desc", -1, -1);
        list.forEach(user -> System.out.println(user));
    }

    @Test
    public void findList2() throws Exception {
        User user = userDao.find("zsy");
        System.out.println(user);
    }

    @Test
    public void count() throws Exception {
        System.out.println(userDao.count("z"));
    }

}