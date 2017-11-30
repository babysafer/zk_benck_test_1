package com.bb;

import org.apache.zookeeper.*;

import java.io.IOException;

/**
 * 监听args[0]地址的zk， znode路径为args[1]
 * 监听的事件是  该路径znode的数据改变、删除、创建
 */
public class TestExitsWatch implements Watcher {
    private static ZooKeeper zk;
    private static String path;

    public static void main(String[] args) {
        String conn = args[0];
        path = args[1];

        try {
            zk = new ZooKeeper(conn, 5000, new TestExitsWatch());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //在 path 注册一个watch 监听 NodeDataChanged、NodeDeleted、NodeCreated
    private void doThing() {
        try {
            System.out.println("start watch at :" + path);
            System.out.println(zk.exists(path, new TestExitsWatch()));
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
                    if (event.getType() == Event.EventType.NodeCreated) {
                        System.out.println(event.getPath() + " created");
                        System.out.println(zk.getChildren(event.getPath(), true));
                        System.out.println(zk.exists(event.getPath(), true));
                    } else if (event.getType().equals(Event.EventType.NodeDataChanged)) {
                        System.out.println(event.getPath() + " datachanged");
                        System.out.println(zk.getChildren(event.getPath(), true));
                        System.out.println(zk.exists(event.getPath(), true));
                    } else if (event.getType() == Event.EventType.NodeDeleted) {
                        System.out.println(event.getPath() + "has already been deleted");
//                         节点被删除后不存在不可以getchildred，会nullpont
//                        System.out.println(zk.getChildren(event.getPath(), true));
                        System.out.println(zk.exists(event.getPath(), true));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }
}

