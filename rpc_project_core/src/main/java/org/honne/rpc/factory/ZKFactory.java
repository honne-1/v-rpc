package org.honne.rpc.factory;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.log4j.BasicConfigurator;
import org.honne.rpc.util.Constants;


public class ZKFactory {

    public static CuratorFramework client;

    public static CuratorFramework createZK() {
        if (client == null) {
            RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
            client = CuratorFrameworkFactory.newClient(Constants.zookeeperAddress, retryPolicy);
            client.start();
        }
        return client;
    }

    public static void main(String[] args) throws Exception {
        CuratorFramework client = createZK();
        client.create().forPath("/netty");
    }
}
