package com.bb;

import org.apache.zookeeper.*;

import java.io.IOException;

public class TestRmZnode {
    public static void main(String[] args) {
        String conn = args[0];
        String path = args[1];
        int threadNum = Integer.parseInt(args[2]);
        int znNum = Integer.parseInt(args[3]);
        int m = 0;
        while (m < threadNum) {
            RmZnode rmZnode = new RmZnode(conn, path, znNum, m);
            rmZnode.start();
            m++;
        }
    }
}

class RmZnode extends Thread {
    String conn;
    String path;
    int znNum;
    int threadId;

    public RmZnode(String conn, String path, int znNum, int threadId) {
        this.conn = conn;
        this.path = path;
        this.znNum = znNum;
        this.threadId = threadId;
    }

    @Override
    public void run() {
        try {
            long consumtime = createZnode(conn, path, znNum);
            System.out.println("thread--" + threadId + ": " + consumtime);
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
        //TODO  寻找高效拼接字符串的方法
//        for (int i = 0; i < znNum; i++) {
//            if ((i + "").length() == 1)
//                zk.delete(path + "000000000" + i, 0);
//            if ((i + "").length() == 2)
//                zk.delete(path + "00000000" + i, 0);
//            if ((i + "").length() == 3)
//                zk.delete(path + "0000000" + i, 0);
//            if ((i + "").length() == 4)
//                zk.delete(path + "000000" + i, 0);
//            if ((i + "").length() == 5)
//                zk.delete(path + "00000" + i, 0);
//            if ((i + "").length() == 6)
//                zk.delete(path + "0000" + i, 0);
//            if ((i + "").length() == 7)
//                zk.delete(path + "000" + i, 0);
//            if ((i + "").length() == 8)
//                zk.delete(path + "00" + i, 0);
//        }
        for (int i = 0;i<znNum;i++){
            zk.delete(path+"0"+Long.toString(1000000000+i).substring(1,10),0);
        }
        long end = System.currentTimeMillis();
        zk.close();
        long consumtime = (end - start);
        return consumtime;
    }
}

