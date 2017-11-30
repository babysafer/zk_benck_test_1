package com.bb;

import org.apache.zookeeper.*;
import java.io.IOException;

/**
 *  测试最大连接数
 *  args[0]为zk的连接串， args[1]创建session的个数
 *
 */
public class TestMaxConn {
    public static void main(String[] args) {
        String conn = args[0];
        int threadNum =Integer.parseInt(args[1]);
        int m =0;
        while ( m <threadNum){
            CreatSession creatSession=new CreatSession(conn,m);
            creatSession.start();
            m++;
        }
    }
}
class CreatSession extends Thread{
    String conn ;
    int threadId;

    public CreatSession(String conn,int threadId) {
        this.conn = conn;
        this.threadId=threadId;
    }

    @Override
    public void run() {
        try {
            creatSession();
            System.out.println("thread--"+threadId);
            Thread.sleep(Integer.MAX_VALUE);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void creatSession() throws IOException, KeeperException, InterruptedException {
        ZooKeeper zk = new ZooKeeper(conn, 5000, new Watcher() {
            public void process(WatchedEvent event) {
                System.out.println("event:" + event);
            }
        });
        zk.getChildren("/",false);
    }
}



