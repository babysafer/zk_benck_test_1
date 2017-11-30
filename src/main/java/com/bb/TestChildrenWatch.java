package com.bb;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

/**
 * 监听args[0]地址的zk， znode路径为args[1]
 * 监听的事件是  该路径znode的子节点的变化
 */
public class TestChildrenWatch implements Watcher {
    private static ZooKeeper zk;
    private static String path;

    public static void main(String[] args) {
        String conn = args[0];
        path = args[1];

        try {
            zk = new ZooKeeper(conn, 5000, new TestChildrenWatch());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //在 path 注册一个watch 监听 NodeChildrenChanged
    private void doThing() {
        try {
            System.out.println("start watch at :" + path);
            System.out.println(zk.getChildren(path, new TestChildrenWatch()));
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process(WatchedEvent event) {
        if (event.getState() == Event.KeeperState.SyncConnected) {
            if (event.getType() == Event.EventType.None && null == event.getPath()) {
                System.out.println(".....sission connected....");
                doThing();
            } else {
                try {
                    if (event.getType() == Event.EventType.NodeChildrenChanged) {
                        System.out.println(event.getPath() + ":  Children has changed");
                        System.out.println(zk.getChildren(event.getPath(), true));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }
}

