package com.bb;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

public class TestSetDataZnode {
    public static void main(String[] args) {
        String conn = args[0];
        String path = args[1];
        int threadNum = Integer.parseInt(args[2]);
        int znNum = Integer.parseInt(args[3]);
        String value = args[4];
        int version = Integer.parseInt(args[5]);
        int m = 0;
        while (m < threadNum) {
            SetDataZnode setDataZnode = new SetDataZnode(conn, path, znNum, threadNum, value, version);
            setDataZnode.start();
            m++;
        }
        System.out.printf("");
    }
}

class SetDataZnode extends Thread {
    String conn;
    String path;
    int znNum;
    int threadId;
    String value;
    int version;

    public SetDataZnode(String conn, String path, int znNum, int threadId, String value, int version) {
        this.conn = conn;
        this.path = path;
        this.znNum = znNum;
        this.threadId = threadId;
        this.value = value;
        this.version=version;
    }

    @Override
    public void run() {
        try {
            long consumtime = createZnode(conn, path, znNum);
            System.out.println("thread----" + threadId + ": " + consumtime);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public long createZnode(String conn, String path, int znNum) throws IOException, KeeperException, InterruptedException {
        ZooKeeper zk = new ZooKeeper(conn, 5000, new Watcher() {
            public void process(WatchedEvent event) {
                System.out.println("event:" + event);
            }
        });


        long start = System.currentTimeMillis();

        for (int i = 0;i<znNum;i++){
            zk.setData(path+"0"+Long.toString(1000000000+i).substring(1,10),(value+i).getBytes(),version);
        }
        long end = System.currentTimeMillis();
        zk.close();
        long consumtime = (end - start);
        return consumtime;
    }
}
