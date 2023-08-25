package org.honne.consumer.client;

import com.alibaba.fastjson.JSON;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorWatcher;
import org.honne.consumer.async.DefaultFuture;
import org.honne.consumer.connection.ChannelFutureManager;
import org.honne.consumer.payload.ClientRequest;
import org.honne.consumer.payload.FutureResponse;
import org.honne.consumer.util.Constants;
import org.honne.consumer.zk.ServerWatcher;
import org.honne.consumer.zk.ZKFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static org.honne.consumer.util.Constants.*;


public class TCPClient {

    public static final Bootstrap bootstrap = new Bootstrap();

    static ChannelFuture channelFuture = null;

    static {
        String host = "localhost";
        int port = 7988;
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            bootstrap
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline p = socketChannel.pipeline();
                            p.addLast(new DelimiterBasedFrameDecoder(65535, Delimiters.lineDelimiter()));
                            p.addLast(new StringDecoder());
                            p.addLast(new StringEncoder());
                            p.addLast(new NettyClientHandler());
                        }
                    });
            CuratorFramework client = ZKFactory.createZK();
            List<String> serverPaths = client.getChildren().forPath(SERVER_PATH);

            CuratorWatcher watcher = new ServerWatcher();
            client.getChildren().usingWatcher(watcher).forPath(SERVER_PATH);


            for (String serverPath : serverPaths) {
                String[] serverInfo = serverPath.split("#");
                ChannelFutureManager.realServerPaths.add(serverInfo[0] + ":" + serverInfo[1]);
                ChannelFuture channelFuture = TCPClient.bootstrap.connect(serverInfo[0], Integer.parseInt(serverInfo[1]));
                ChannelFutureManager.add(channelFuture);
            }
            if (ChannelFutureManager.realServerPaths.size() > 0) {
                String[] serverInfo = ChannelFutureManager.realServerPaths.toArray()[0].toString().split(":");
                host = serverInfo[0];
                port = Integer.parseInt(serverInfo[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static FutureResponse send(ClientRequest request) {
//        System.out.println("send: " + JSON.toJSONString(request));
        channelFuture = ChannelFutureManager.get(ChannelFutureManager.position);
        channelFuture.channel().writeAndFlush(JSON.toJSONString(request) + "\r\n");
        DefaultFuture defaultFuture = new DefaultFuture(request);
        return defaultFuture.get();
    }

    public static void main(String[] args) {
        ClientRequest request = new ClientRequest();
        request.setContent("Hello, server: (>^ω^<)喵");
        FutureResponse response = TCPClient.send(request);
        System.out.println("response: " + JSON.toJSONString(response));
    }
}
