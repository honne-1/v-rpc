package org.honne.user.remote;

import org.honne.rpc.annotation.Remote;
import org.honne.rpc.payload.FutureResponse;
import org.honne.rpc.util.ResponseUtil;
import org.honne.user.bean.User;
import org.honne.user.service.UserService;

import javax.annotation.Resource;
import java.util.List;

@Remote
public class UserRemoteImpl implements UserRemote{
    @Resource
    private UserService userService;

    public FutureResponse saveUser(User user) {
        userService.save(user);
        return ResponseUtil.createSuccessResponse(user);
    }

    public FutureResponse saveUsers(List<User> users) {
        userService.saveList(users);
        return ResponseUtil.createSuccessResponse(users);
    }
}
