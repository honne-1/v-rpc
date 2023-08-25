package org.honne.user.remote;

import org.honne.consumer.payload.FutureResponse;
import org.honne.user.bean.User;

import java.util.List;

public interface UserRemote {
    public FutureResponse saveUser(User user);

    public FutureResponse saveUsers(List<User> users);
}
