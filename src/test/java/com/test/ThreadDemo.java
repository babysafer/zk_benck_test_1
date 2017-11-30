package com.test;

public class ThreadDemo {
    public static void main(String[] args) {
        int i =0;
//        一个对象名字没变，就会自动垃圾回收么
        while ( i< 10){
            Athread a1= new Athread();
            //run跑完了才会，继续下面的i++么？
            a1.run();

            i++;
        }
    }
}
class Athread extends Thread{
    @Override
    public void run() {
        System.out.println("my thread");
        //当前线程 谦逊一点
        Thread.yield();
    }
}


