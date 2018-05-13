package com.springboot.dubbo.dubbo.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

@Slf4j
public class ZkConnect implements Watcher {

    private static final String zkServerPath = "127.0.0.1";

    private static final int timeout = 5000;

    public static void main(String[] args) {
        try {
            ZooKeeper zooKeeper = new ZooKeeper(zkServerPath, timeout, new ZkConnect());
            long sessionId = zooKeeper.getSessionId();
            byte[] sessionPasswd = zooKeeper.getSessionPasswd();

            log.warn("客户端开始连接zookeeper服务器...");
            log.warn("连接状态: {}", zooKeeper.getState());

            Thread.sleep(2000);

            log.warn("连接状态: {}", zooKeeper.getState());

            Thread.sleep(2000);

            // 开始会话重连
            log.warn("开始会话重连");

            ZooKeeper zooKeeperSession = new ZooKeeper(zkServerPath, timeout, new ZkConnect(), sessionId, sessionPasswd);

            log.warn("重新连接状态: {}", zooKeeperSession.getState());

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        log.warn("接收到watcher通知: {}", watchedEvent.toString());
    }
}
