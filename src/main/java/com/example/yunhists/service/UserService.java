package com.example.yunhists.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.yunhists.entity.User;

public interface UserService extends IService<User> {

    // Create
    int register(User user);

    // Read
    User login(String email, String password);
    User getUserById(Integer id);
    User getUserByEmail(String email);

    // Update
    int updateUsername(Integer id, String username);
    int updatePassword(Integer id, String password);
    int updateUserRights(Integer id, Integer userRights);
    int updateLang(Integer id, String lang);
    int addPoints(Integer id);
    int updateUserToDeletedUser(Integer id);

    // Delete
    int deleteUserById(Integer id);

}
