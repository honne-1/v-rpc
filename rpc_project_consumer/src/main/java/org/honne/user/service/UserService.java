package org.honne.user.service;

import org.honne.user.bean.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    public void save(User user) {
        System.out.println("======================save user==========================");
    }

    public void saveList(List<User> users) {
        System.out.println("======================save users==========================");
    }
}
