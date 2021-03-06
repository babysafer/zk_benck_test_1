package com.test;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.List;

/**
 * 异步
 * 异步获取没有返回值，同步获取有返回值
 */
public class SecondL implements Watcher {
    private static ZooKeeper zk;

    public static void main(String[] args) throws IOException, InterruptedException {
        String conn = "172.21.11.63:8501,172.21.11.64:8501,172.21.11.65:8501";
        zk = new ZooKeeper(conn, 5000, new SecondL());
        System.out.println(zk.getState());
        Thread.sleep(Integer.MAX_VALUE);
    }

    private void doThing() {
        zk.create("/fun", "hello".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT, new IsStringCallback(), "create");
        System.out.println("搞些事情");
    }

    @Override
    public void process(WatchedEvent event) {
        System.out.println("watch start::" + event);

        if (event.getState() == Event.KeeperState.SyncConnected) {
            if (event.getType() == Event.EventType.None && null == event.getPath()) {
                doThing();
            } else {
                if (event.getType() == Event.EventType.NodeChildrenChanged)
                    try {
                        List<String> list = zk.getChildren(event.getPath(), true);
                        for (String str : list)
                            System.out.println(str);
                    } catch (KeeperException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
            }
        }
    }

    static class IsStringCallback implements AsyncCallback.StringCallback {
        /**
         * 回调函数，
         *
         * @param rc   返回码 ，成功返回0
         * @param path 提交的路径
         * @param ctx  上下文
         * @param name 返回的真实路径
         */
        @Override
        public void processResult(int rc, String path, Object ctx, String name) {
            StringBuffer sb = new StringBuffer();
            sb.append("rc:" + rc).append("\n");
            sb.append("path:" + path).append("\n");
            sb.append("name:" + name).append("\n");
            sb.append("Object ctx:" + ctx).append("\n");
            System.out.println(sb);
        }
    }
}
