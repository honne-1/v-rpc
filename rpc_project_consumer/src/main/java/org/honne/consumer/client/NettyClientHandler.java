package org.honne.consumer.client;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.honne.consumer.async.DefaultFuture;
import org.honne.consumer.payload.FutureResponse;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class NettyClientHandler extends ChannelInboundHandlerAdapter implements ChannelHandler {

    private static final Executor exec = Executors.newFixedThreadPool(10);
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        if ("ping".equals(msg.toString())) {
//            System.out.println("客户端收到服务器的ping消息");
//            ctx.channel().writeAndFlush("pong\r\n");
//            return;
//        }
//        System.out.println("channelRead: " + msg.toString());
//        FutureResponse futureResponse = JSONObject.parseObject(msg.toString(), FutureResponse.class);
//        System.out.println("Received message: " + futureResponse.getResult());
//        DefaultFuture.receive(futureResponse);
        final Object m = msg;
        exec.execute(new Runnable() {
            @Override
            public void run() {
                FutureResponse futureResponse = JSONObject.parseObject(m.toString(), FutureResponse.class);
//                System.out.println("Received message: " + JSONObject.toJSONString(futureResponse));
                DefaultFuture.receive(futureResponse);
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
