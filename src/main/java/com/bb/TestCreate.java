package com.bb;

import org.apache.zookeeper.*;

import java.io.IOException;

public class TestCreate {
    public static void main(String[] args) {
        String conn = args[0];
        String path = args[1];
        String value = args[2];
        int threadNum =Integer.parseInt(args[3]);
        int znNum =Integer.parseInt(args[4]);
        int m =0;
        while ( m <threadNum){
            CreateZnode createZnode=new CreateZnode(conn,path,value,znNum,m);
            createZnode.start();
            m++;
        }
    }
}
class CreateZnode extends Thread{
    String conn ;
    String path;
    String value;
    int znNum;
    int threadId;

    public CreateZnode(String conn, String path, String value,int znNum,int threadId) {
        this.conn = conn;
        this.path = path;
        this.value = value;
        this.znNum = znNum;
        this.threadId= threadId;
    }

    @Override
    public void run() {
        try {
            long time =createZnode(conn,path,value,znNum);
            System.out.println("thread--"+threadId+": "+time);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public long createZnode(String conn,String path,String value,int znNum) throws IOException, KeeperException, InterruptedException {
        ZooKeeper zk = new ZooKeeper(conn, 5000, new Watcher() {
            public void process(WatchedEvent event) {
                System.out.println("event:" + event);
            }
        });
        long start = System.currentTimeMillis();
        for (int i=0;i <znNum;i++)
            zk.create(path, value.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
        long end = System.currentTimeMillis();
        zk.close();
        long time=(end-start);
        return  time;
    }
}
