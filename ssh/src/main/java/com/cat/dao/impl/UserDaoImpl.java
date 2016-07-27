package com.cat.dao.impl;

import com.cat.dao.UserDao;
import com.cat.model.User;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

/**
 * Created by Archimedes on 2016/07/21.
 */

@Repository//@Component
public class UserDaoImpl implements UserDao {

    @Resource
    private HibernateTemplate hibernateTemplate;

    @Override
    public void save(User user) {
        hibernateTemplate.save(user);
    }

    @Override
    public void delete(Integer id) {
        hibernateTemplate.delete(id);
    }

    @Override
    public void delete(User user) {
        hibernateTemplate.delete(user);
    }

    @Override
    public void delete(Integer[] ids) {
        for (int id : ids) {
            this.delete(id);
        }
    }

    @Override
    public void delete(List<User> users) {
        for (User user : users) {
            this.delete(user);
        }
    }

    @Override
    public void update(User user) {
        hibernateTemplate.update(user);
    }

    @Override
    public void merge(User user) {
        hibernateTemplate.saveOrUpdate(user);
//        if (user.getId() == null) {
//            this.save(user);
//        } else {
//            this.update(user);
//        }
    }

    @Override
    public User find(Integer id) {
        return hibernateTemplate.get(User.class, id);
    }

    @Override
    public User find(String name) {
        List<User> users = (List<User>) hibernateTemplate.find("from User where name = ?", name);
        return users == null || users.isEmpty() ? null : users.get(0);
    }

    @Override
    public List<User> findList() {
        return (List<User>) hibernateTemplate.find("from User");
    }

    @Override
    public List<User> findList(String name) {
        return (List<User>) hibernateTemplate.find("from User where name like ?", "%" + name + "%");
    }

    @Override
    public List<User> findList(String name, String sort, String order, int pageNo, int pageSize) {
        StringBuffer sql = new StringBuffer("from User user where 1 = 1");
        Map<String, Object> map = new HashMap<>();

        if (name != null) {
            sql.append(" and user.name like :name");
            map.put("name", "%" + name + "%");
        }

        if (sort != null && order != null) {
            sql.append(" order by user." + sort + " " + order);
        }

        Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
        Query query = session.createQuery(sql.toString());
        if (pageNo >= 0 && pageSize > 0) {
            query.setFirstResult((pageNo - 1) * pageSize).setMaxResults(pageSize);
        }
        query.setProperties(map);

        return query.list();

    }

    @Override
    public int count(String name) {
        StringBuffer sql = new StringBuffer("select count(*) from User as user where 1 = 1");
        Map<String, Object> map = new HashMap<>();

        if (name != null || name.trim().length() > 0) {
            sql.append(" and user.name like :name");
            map.put("name", "%" + name + "%");
        }
        Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
        Query query = session.createQuery(sql.toString());
        query.setProperties(map);

        return ((Long) query.iterate().next()).intValue();
    }
}
