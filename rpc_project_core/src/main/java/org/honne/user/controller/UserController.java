package org.honne.user.controller;

import org.honne.rpc.payload.FutureResponse;
import org.honne.rpc.util.ResponseUtil;
import org.honne.user.bean.User;
import org.honne.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

//@Controller
//public class UserController {
//    @Resource
//    private UserService userService;
//
//    public FutureResponse saveUser(User user) {
//        userService.save(user);
//        return ResponseUtil.createSuccessResponse(user);
//    }
//
//    public FutureResponse saveUsers(List<User> users) {
//        userService.saveList(users);
//        return ResponseUtil.createSuccessResponse(users);
//    }
//
//}
