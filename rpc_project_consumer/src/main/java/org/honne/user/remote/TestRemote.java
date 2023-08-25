package org.honne.user.remote;

import org.honne.consumer.payload.FutureResponse;
import org.honne.user.bean.User;

public interface TestRemote {
    public FutureResponse testUser(User user);
}