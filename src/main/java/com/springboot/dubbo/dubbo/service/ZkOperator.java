package com.springboot.dubbo.dubbo.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;

import java.io.IOException;
import java.util.List;

@Slf4j
public class ZkOperator implements Watcher {

    private static final String zkServerPath = "127.0.0.1";

    private static final int timeout = 5000;

    private ZooKeeper zooKeeper = null;

    public ZkOperator() {
    }

    /**
     * 不能再finally块关闭ZooKeeper  会造成连接提前关闭
     *
     * @param connectString zk服务端ip
     */
    public ZkOperator(String connectString) {
        try {
            zooKeeper = new ZooKeeper(connectString, timeout, new ZkOperator());
        } catch (IOException e) {
            e.printStackTrace();
            if (null != zooKeeper) {
                try {
                    zooKeeper.close();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * PERSISTENT:持久节点
     * PERSISTENT_SEQUENTIAL:临时持久节点
     * EPHEMERAL:临时节点
     * EPHEMERAL_SEQUENTIAL:临时顺序节点
     *
     * @param path    创建的路径
     * @param data    数据
     * @param aclList 权限
     */
    private void createZkNode(String path, byte[] data, List<ACL> aclList) {
        String result = "";
        try {
            result = zooKeeper.create(path, data, aclList, CreateMode.EPHEMERAL);
            log.info("创建节点成功 : {}", result);
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process(WatchedEvent event) {
        log.warn("接收到watcher通知: {}", event.toString());
    }

    public static void main(String[] args) {
        ZkOperator zkOperator = new ZkOperator(zkServerPath);
        zkOperator.createZkNode("/testNode", "testNode".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE);
    }
}
