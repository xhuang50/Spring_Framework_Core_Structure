package com.redline.springcore.test;

import com.redline.springcore.dao.UserDaoImpl;
import com.redline.springcore.po.User;
import com.redline.springcore.service.UserService;
import com.redline.springcore.service.UserServiceImpl;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DemoV1 {
    @Test
    public void test(){
        UserService userService = getUserService();
        Map<String, Object> map = new HashMap<>();
        map.put("username", "Catie");
        List<User> users = userService.queryUsers(map);
        System.out.println(users);
    }

    private UserService getUserService() {
        UserServiceImpl userService = new UserServiceImpl();
        UserDaoImpl userDao = new UserDaoImpl();
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/enhancement");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        userDao.setDataSource(dataSource);
        userService.setUserDao(userDao);
        return userService;
    }
}
