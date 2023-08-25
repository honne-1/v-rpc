import org.honne.rpc.client.TCPClient;
import org.honne.rpc.payload.ClientRequest;
import org.honne.rpc.payload.FutureResponse;
import org.honne.user.bean.User;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TCPTest {
    @Test
    public void testGetResponse(){
        ClientRequest clientRequest = new ClientRequest();
        clientRequest.setContent("Hello World");
        FutureResponse futureResponse = TCPClient.send(clientRequest);
        System.out.println(futureResponse.getResult());
    }

    @Test
    public void testSaveUser(){
        User user = new User(1, "张三");
        ClientRequest clientRequest = new ClientRequest();
        clientRequest.setContent(user);
        clientRequest.setCommand("org.honne.user.controller.UserController.saveUser");
        FutureResponse futureResponse = TCPClient.send(clientRequest);
        System.out.println(futureResponse.getResult());
    }

    @Test
    public void testSaveUsers(){
        User user = new User(1, "张三");
        User user2 = new User(2, "李四");
        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(user2);
        ClientRequest clientRequest = new ClientRequest();
        clientRequest.setContent(users);
        clientRequest.setCommand("org.honne.user.controller.UserController.saveUsers");
        FutureResponse futureResponse = TCPClient.send(clientRequest);
        System.out.println(futureResponse.getResult());
    }


}
