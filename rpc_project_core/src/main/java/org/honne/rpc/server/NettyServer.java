package org.honne.rpc.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.honne.rpc.factory.ZKFactory;

import java.net.InetAddress;

import static org.honne.rpc.util.Constants.SERVER_PATH;
import static org.honne.rpc.util.Constants.port;

public class NettyServer {
    public static void start(int port) throws Exception {
        System.setProperty("java.net.preferIPv4Stack", "true");
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, false)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline p = socketChannel.pipeline();
                            p.addLast(new DelimiterBasedFrameDecoder(65535, Delimiters.lineDelimiter()));
                            p.addLast(new StringDecoder());
                            p.addLast(new StringEncoder());
//                            p.addLast(new IdleStateHandler(60, 45, 20, TimeUnit.SECONDS));
                            p.addLast(new NettyServerHandler());
                        }
                    });

            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            // 注册服务器地址到zk
            CuratorFramework client = ZKFactory.createZK();
            InetAddress address = InetAddress.getLocalHost().isLoopbackAddress() ? InetAddress.getByName("myhost") : InetAddress.getLocalHost();
            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(SERVER_PATH + "/" + address.getHostAddress() + "#" + port + "#");

            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        NettyServer.start(7988);
    }
}
