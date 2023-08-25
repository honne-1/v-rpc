package org.honne.consumer.connection;

import io.netty.channel.ChannelFuture;

import java.util.concurrent.CopyOnWriteArrayList;

import java.util.concurrent.atomic.AtomicInteger;

public class ChannelFutureManager {
    public static CopyOnWriteArrayList<ChannelFuture> channelFutures = new CopyOnWriteArrayList<>();
    public static CopyOnWriteArrayList<String> realServerPaths = new CopyOnWriteArrayList<>();

    public static AtomicInteger position = new AtomicInteger(0);

    public static void add(ChannelFuture channelFuture) {
        channelFutures.add(channelFuture);
    }

    public static void remove(ChannelFuture channelFuture) {
        channelFutures.remove(channelFuture);
    }

    public static void clear() {
        channelFutures.clear();
    }

    public static ChannelFuture get(AtomicInteger pos) {
        ChannelFuture channelFuture = null;
        int size = channelFutures.size();
        // 轮循机制
        if (pos.get() >= size) {
            channelFuture = channelFutures.get(0);
            ChannelFutureManager.position= new AtomicInteger(1);
        } else {
            channelFuture = channelFutures.get(pos.getAndIncrement());
        }
        return channelFuture;
    }

}
