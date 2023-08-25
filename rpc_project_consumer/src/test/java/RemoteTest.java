
import com.alibaba.fastjson.JSONObject;
import org.honne.consumer.annotation.RemoteInvoke;
import org.honne.consumer.payload.FutureResponse;
import org.honne.user.bean.User;
import org.honne.user.remote.UserRemote;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RemoteTest.class)
@ComponentScan("org.honne")
public class RemoteTest {
    @RemoteInvoke
    private UserRemote userRemote;

    @Test
    public void testSaveUser() {
        User user = new User();
        user.setId(1);
        user.setName("honne");
        Long start = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            FutureResponse futureResponse = userRemote.saveUser(user);
        }
        Long end = System.currentTimeMillis();

        System.out.println("time: " + (end - start)/1000.0 + "s");
    }

    @Test
    public void testSaveUsers() {
        User user = new User();
        user.setId(1);
        user.setName("honne");
        User user2 = new User();
        user2.setId(2);
        user2.setName("honne2");
        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(user2);
        userRemote.saveUsers(users);
    }

}
