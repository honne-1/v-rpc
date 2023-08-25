package org.honne.rpc.server;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.*;
import org.honne.rpc.medium.Medium;
import org.honne.rpc.payload.FutureResponse;
import org.honne.rpc.payload.ServerRequest;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    private static final Executor exec = Executors.newFixedThreadPool(10);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 将客户端的请求交给线程池处理
        exec.execute(new Runnable() {
            @Override
            public void run() {
                // 将客户端的请求反序列化成对象
                ServerRequest request = JSONObject.parseObject(msg.toString(), ServerRequest.class);
                // 将客户端的请求交给中介者处理
                Medium medium = Medium.newInstance();
                // 将中介者处理的结果序列化成对象并发送给客户端
                FutureResponse result = medium.process(request);
                ctx.channel().writeAndFlush(JSONObject.toJSONString(result) + "\r\n");
//                System.out.println("Send message: " + JSONObject.toJSONString(result));
            }
        });

//        Medium medium = Medium.newInstance();
//        FutureResponse result = medium.process(request);

//        System.out.println("Received message: " + request.getContent().toString());

//        FutureResponse futureResponse = new FutureResponse();
//        futureResponse.setId(request.getId());
//        futureResponse.setResult("is ok");

//        ctx.channel().writeAndFlush(JSONObject.toJSONString(result) + "\r\n");
//        System.out.println("Send message: " + JSONObject.toJSONString(result));

    }


//    @Override
//    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
//        if (evt instanceof IdleStateEvent){
//            IdleStateEvent event = (IdleStateEvent) evt;
//            String eventType = null;
//            switch (event.state()){
//                case READER_IDLE:
//                    eventType = "READER_IDLE";
//                    System.out.println(ctx.channel().remoteAddress() + " timeout event: " + eventType);
//                    ctx.channel().close();
//                    break;
//                case WRITER_IDLE:
//                    eventType = "WRITER_IDLE";
//                    System.out.println(ctx.channel().remoteAddress() + " timeout event: " + eventType);
//                    break;
//                case ALL_IDLE:
//                    eventType = "ALL_IDLE";
//                    System.out.println(ctx.channel().remoteAddress() + " timeout event: " + eventType);
//                    ctx.channel().writeAndFlush("ping\r\n");
//                    break;
//            }
//
//        }
//
//    }
}
