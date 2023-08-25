package org.honne.rpc.client;

import com.alibaba.fastjson.JSON;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.honne.rpc.async.DefaultFuture;
import org.honne.rpc.payload.ClientRequest;
import org.honne.rpc.payload.FutureResponse;

import static org.honne.rpc.util.Constants.host;
import static org.honne.rpc.util.Constants.port;

public class TCPClient {

    static final Bootstrap bootstrap = new Bootstrap();

    static ChannelFuture channelFuture;

    static {
        EventLoopGroup group = new NioEventLoopGroup();
        bootstrap
                .group(group)
                .channel(NioSocketChannel.class)
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
        try {
            channelFuture = bootstrap.connect(host, port).sync();
//            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
//            group.shutdownGracefully();
        }
    }
    public static FutureResponse send(ClientRequest request) {
//        System.out.println("send: " + JSON.toJSONString(request));
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
