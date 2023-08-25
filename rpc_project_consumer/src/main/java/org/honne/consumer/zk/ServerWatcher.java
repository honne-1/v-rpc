package org.honne.consumer.zk;

import io.netty.channel.ChannelFuture;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.zookeeper.WatchedEvent;
import org.honne.consumer.client.TCPClient;
import org.honne.consumer.connection.ChannelFutureManager;

import java.util.List;

public class ServerWatcher implements CuratorWatcher {

    @Override
    public void process(WatchedEvent watchedEvent) throws Exception {
        String eventPath = watchedEvent.getPath();
        CuratorFramework client = ZKFactory.createZK();
        client.getChildren().usingWatcher(this).forPath(eventPath);
        List<String> newServerPaths = client.getChildren().forPath(eventPath);
        ChannelFutureManager.realServerPaths.clear();
        for (String serverPath : newServerPaths) {
            String[] serverInfo = serverPath.split("#");
            ChannelFutureManager.realServerPaths.add(serverInfo[0] + ":" + serverInfo[1]);

        }
        ChannelFutureManager.clear();
        for (String realServer : ChannelFutureManager.realServerPaths) {
            String[] serverInfo = realServer.split(":");
            ChannelFuture channelFuture = TCPClient.bootstrap.connect(serverInfo[0], Integer.parseInt(serverInfo[1]));
            ChannelFutureManager.add(channelFuture);
        }
    }
}