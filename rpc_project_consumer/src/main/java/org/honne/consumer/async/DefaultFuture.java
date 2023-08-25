package org.honne.consumer.async;

import lombok.Data;
import org.honne.consumer.payload.ClientRequest;
import org.honne.consumer.payload.FutureResponse;
import org.honne.consumer.util.ResponseUtil;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
@Data
public class DefaultFuture {
    public final static ConcurrentHashMap<Long, DefaultFuture> allDefaultFutures = new ConcurrentHashMap<>();
    final Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    private FutureResponse response;

    private Long timeout = 2 * 60 * 1000L;

    private Long startTime = System.currentTimeMillis();

    public DefaultFuture(ClientRequest request) {
        allDefaultFutures.put(request.getId(), this);
    }

    public FutureResponse get() {
        lock.lock();
        try {
            while (!done()) {
                condition.await();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return this.response;
    }


    public FutureResponse get(long time) {
        lock.lock();
        try {
            while (!done()) {
                condition.await(time, TimeUnit.MILLISECONDS);
                if ((System.currentTimeMillis() - startTime) > time) {
                    System.out.println("Future中的请求超时");
                    break;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return this.response;
    }

    private boolean done() {
        if (this.response != null) {
            return true;
        }
        return false;
    }

    public static void receive(FutureResponse response) {
        if (response != null) {
            DefaultFuture future = allDefaultFutures.get(response.getId());
            if (future != null) {
                Lock lock = future.lock;
                lock.lock();
                try {
                    future.setResponse(response);
                    future.condition.signal();
                    allDefaultFutures.remove(future);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }


    static class FutureThreadClearTimeout extends Thread {
        @Override
        public void run() {
            Set<Long> keySet = allDefaultFutures.keySet();
            for (Long key : keySet) {
                DefaultFuture future = allDefaultFutures.get(key);
                if (future == null) {
                    allDefaultFutures.remove(future);
                } else {
                    if ((System.currentTimeMillis() - future.getStartTime()) > future.getTimeout()) {
                        FutureResponse timeoutResponse = ResponseUtil.createTimeoutResponse(key);
                        receive(timeoutResponse);
                    }
                }
            }
        }
    }

    static {
        FutureThreadClearTimeout futureThreadClearTimeout = new FutureThreadClearTimeout();
        futureThreadClearTimeout.setDaemon(true);
        futureThreadClearTimeout.start();
    }
}
