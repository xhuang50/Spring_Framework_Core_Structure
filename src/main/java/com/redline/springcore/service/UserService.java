package com.redline.springcore.service;

import com.redline.springcore.po.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    List<User> queryUsers(Map<String, Object> param);
}
