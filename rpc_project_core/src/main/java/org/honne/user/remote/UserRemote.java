package org.honne.user.remote;

import org.honne.rpc.payload.FutureResponse;
import org.honne.rpc.util.ResponseUtil;
import org.honne.user.bean.User;

import java.util.List;

public interface UserRemote {
    FutureResponse saveUser(User user);

    FutureResponse saveUsers(List<User> users);
}
