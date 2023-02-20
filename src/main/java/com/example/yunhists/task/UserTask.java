package com.example.yunhists.task;

import com.example.yunhists.entity.User;
import com.example.yunhists.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserTask {

    @Autowired
    UserService userService;

    @Scheduled(cron ="0 0 0 * * ?")
    public void resetTodayDownload() {
        List<User> userList = userService.getUserAll();
        for(User user : userList) {
            user.setTodayDownload(0);
            userService.saveOrUpdate(user);
        }
    }

}
