package org.honne.rpc.server;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import static org.honne.rpc.util.Constants.port;

@Component
public class NettyInitialization implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        try {
            NettyServer.start(port);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
