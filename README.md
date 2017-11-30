## 工具依赖的环境
- jdk 1.7 以上
- 到目标zk主机网络是通的

## 功能测试
### 创建节点
    java -cp zk_benck_test_1.jar com.bb.TestCreate 172.21.11.63:8501,172.21.11.64:8501,172.21.11.65:8501  /fun/start  xixi  2  5
- 首先通过zkCli创建一个永久的节点/fun作为父节点，在下面创建以start开头的顺序节点。
- 第二个参数'xixi'是所创建出来的顺序节点的值。
- 第三个参数'2'有两个进程同时创建。
- 第四个参数'5'是单个进程创建的节点个数。

### 获取节点值
    java -cp zk_benck_test_1.jar com.bb.TestReadZnode 172.21.11.63:8501,172.21.11.64:8501,172.21.11.65:8501  /fun/start 3 10
- 获取值之前要在上一步创建出`/fun/start`开头的顺序节点。
- 第二个参数`3`有两个进程同时读取。
- 第三个参数`5`是单个进程读取的节点个数。

### 更改节点的值
    java -cp zk_benck_test_1.jar  com.bb.TestSetDataZnode 172.21.11.63:8501,172.21.11.64:8501,172.21.11.65:8501  /fun/start 1  10  hello 0
- 更改值之前要创建出`/fun/start`开头的顺序节点。
- 第二个参数`1`有1个进程在更改`znode`的值，当前工具版本只支持一个线程更改
- 第三个参数`10`是单个进程操作的节点个数
- 第四个参数`hello`是更改后节点的值
- 第五个参数`0`是`/fun`下顺序节点的`version`

### 删除节点
    java -cp zk_benck_test_1.jar com.bb.TestRmZnode 172.21.11.63:8501,172.21.11.64:8501,172.21.11.65:8501  /fun/start 1  10
- 删除之前要创建出`/fun/start`开头的顺序节点。当前工具只支持删除`dataVersion=0`的znode
- 第二个参数`1`有1个进程在删除`znode`的值，当前工具版本只支持一个线程更改
- 第三个参数`10`是单个进程操作的节点个数

### zk事件订阅
    java -cp zk_benck_test_1.jar com.bb.TestExitsWatch  172.21.11.63:8501,172.21.11.64:8501,172.21.11.65:8501  /fun
- 此时就对`/fun` 这个节点进行了永久注册，并阻塞式的在命令行显示并等待事件发生
- 单独开启一个`zkcli`对`/fun`节点分别进行 创建，修改值，增加删除子节点
- 观察触发的显示。图片可以参考附件的zookeeper测试报告。    

## 性能测试
### 测试创建znode 效率
    java -cp zk_benck_test_1.jar com.bb.TestCreate 172.21.11.63:8501,172.21.11.64:8501,172.21.11.65:8501  /fun/start  xixi  200  5000
    java -cp zk_benck_test_1.jar com.bb.TestCreate 172.21.11.63:8501,172.21.11.64:8501,172.21.11.65:8501  /fun/start  xixi  20  5000
- 各参数对应如上面功能测试解释。
- 分布创建10w，100w 会输出每个进程的消耗时间，观察输出。

### 测试获取znode数据的效率
    java -cp zk_benck_test_1.jar com.bb.TestReadZnode 172.21.11.63:8501,172.21.11.64:8501,172.21.11.65:8501  /fun/start 200 500
- 各参数对应如上面功能测试解释。
- 200个客户端并发读取共10w个znode的值，观察输出。可根据实际需求随意调整。

### 测试更改znode数据
    java -cp zk_benck_test_1.jar  com.bb.TestSetDataZnode 172.21.11.63:8501,172.21.11.64:8501,172.21.11.65:8501  /fun/start 1  100000  hello 0
- 各参数对应如上面功能测试解释。
- 可得到一个线程更改`10w`个`znode`所消耗的时间。

### 测试删除znode性能
    java -cp zk_benck_test_1.jar com.bb.TestRmZnode 172.21.11.63:8501,172.21.11.64:8501,172.21.11.65:8501  /fun/start 1  100000
- 各参数对应如上面功能测试解释。
- 可得到一个线程删除`10w`个`znode`所消耗的时间。

### 测试zookeeper集群最大连接数与连接数是否均匀分布在个节点
    java -cp zk_benck_test_1.jar com.bb.TestMaxConn 172.21.11.63:8501,172.21.11.64:8501,172.21.11.65:8501 1024
- 模拟出`1024`个`client`连接到集群，通过[zControlCY](https://github.com/babysafer/Zcontrol_m)页面观察连接分配
