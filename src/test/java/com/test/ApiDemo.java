package com.test;


import org.apache.log4j.BasicConfigurator;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class ApiDemo {
    public static void main(String[] args) {

    }
    /**
     * 更新znode的数据，即写入
     *
     * @throws IOException
     * @throws KeeperException
     * @throws InterruptedException
     */
    @Test
    public void setZdata() throws IOException, KeeperException, InterruptedException {
        BasicConfigurator.configure();
        String conn = "172.21.11.63:8501,172.21.11.64:8501,172.21.11.65:8501";
        ZooKeeper zk = new ZooKeeper(conn, 5000, new Watcher() {
            public void process(WatchedEvent event) {
                System.out.printf("over" + event);
            }
        });

        Stat stat = zk.setData("/tversion", "happy_V5".getBytes(), 0);
        System.out.println("stat:" + stat);
        System.out.println("##################");
        zk.close();
    }

    /**
     * 创建znode
     *
     * @throws IOException
     * @throws KeeperException
     * @throws InterruptedException
     */
    @Test
    public void createZnode() throws IOException, KeeperException, InterruptedException {
//        加载log4j
        BasicConfigurator.configure();
        String conn = "172.21.11.63:8501,172.21.11.64:8501,172.21.11.65:8501";
        // 建立zkclient
        ZooKeeper zk = new ZooKeeper(conn, 5000, new Watcher() {
            public void process(WatchedEvent event) {
                System.out.println("event:" + event);
            }
        });
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
       /* 创建acl,自己创建acl比较具体随意定制，可以用ZooDefs.Ids.??? 现成的acl权限arrylist，是根据各个用户来控制
        ACL acl = new ACL(Perms.ALL, ZooDefs.Ids.ANYONE_ID_UNSAFE);
        List<ACL> aclList = new ArrayList<ACL>();
        aclList.add(acl);
        zk.create("/seq_e", "sucess".getBytes(), aclList, CreateMode.EPHEMERAL_SEQUENTIAL);
        */
        // 创建znode,类型通过CreateMode.   枚举确定
        zk.create("/tversion", "sucess".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

        System.out.println("done");
        zk.close();
    }

    /**
     *  获取儿子
     * @throws IOException
     * @throws KeeperException
     * @throws InterruptedException
     */
    @Test
    public void getChildreN() throws IOException, KeeperException, InterruptedException {
//        BasicConfigurator.configure();
        String conn = "172.21.11.63:8501,172.21.11.64:8501,172.21.11.65:8501";

        ZooKeeper zk = new ZooKeeper(conn, 5000, new Watcher() {
            public void process(WatchedEvent event) {
                System.out.println("搞事情了" + event);
            }
        });
        System.out.println(zk.getSessionId());
        List<String> chrildList = zk.getChildren("/", null);

        System.out.println("##########################");
        for (String str : chrildList) {
            System.out.println(str);
        }
        zk.close();
    }

    /**
     *   删除znode
     * @throws IOException
     * @throws KeeperException
     * @throws InterruptedException
     */
    @Test
    public void rmZnode() throws IOException, KeeperException, InterruptedException {
//        BasicConfigurator.configure();
        String conn = "172.21.11.63:8501,172.21.11.64:8501,172.21.11.65:8501";
        ZooKeeper zk = new ZooKeeper(conn, 5000, new Watcher() {
            public void process(WatchedEvent event) {
                System.out.printf("over:::" + event);
            }
        });
        zk.delete("/root1",0);
        zk.close();
    }
    /**
     *  获取znode数据
     */
    @Test
    public void getDataZnode() throws IOException, KeeperException, InterruptedException {
//        BasicConfigurator.configure();
        String conn = "172.21.11.63:8501,172.21.11.64:8501,172.21.11.65:8501";
        ZooKeeper zk = new ZooKeeper(conn, 5000, new Watcher() {
            public void process(WatchedEvent event) {
                System.out.printf("over:::" + event+"\n");
            }
        });

        System.out.println(new String(zk.getData("/tversion",false, null)));
    }



    /**
     * 注册观察者，测试观察，回调
     *
     * @throws Exception
     */
    Watcher w = null;
    @Test
    public  void watcherDemo() throws IOException, KeeperException, InterruptedException {
//        BasicConfigurator.configure();
        String conn = "172.21.11.63:8501,172.21.11.64:8501,172.21.11.65:8501";
        final ZooKeeper zk = new ZooKeeper(conn, 50000, new Watcher() {
            public void process(WatchedEvent event) {
                System.out.println("连接事件："+event);
            }
        });

        w = new Watcher() {
            public void process(WatchedEvent event) {
                System.out.println("only w::"+event);
                if (event.getType().equals(Event.EventType.NodeDataChanged)){
                    try {
                        zk.getData("/seq0000000029", w, null);

                    } catch (KeeperException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        byte[] str = zk.getData("/seq0000000029", w, null);
        System.out.println(zk.getSessionId());
        System.out.println(new String(str));
        Thread.sleep(Integer.MAX_VALUE);
    }

    @Test
    public void t1(){
        for (int i=0;i<12;i++) {
            String path_p = Long.toString(1000000000+i);
            String path= path_p.substring(1, 10);
            System.out.println(path);
        }
    }
    @Test
    public void testWathcer() throws IOException, KeeperException, InterruptedException {
//        String conn="172.21.11.63:8501,172.21.11.64:8501,172.21.11.65:8501";
//        ZooKeeper zk = new ZooKeeper(conn, 5000, new MyWatcher());
//        System.out.println("---------------");
//        List<String> list= zk.getChildren("/",new MyWatcher(zk));
//        Thread.sleep(Integer.MAX_VALUE);
//        for (String str : list )
//            System.out.println(str);
//        while (true){}
//        zk.close();
    }




}

