# JAD_Chatting_Bot

* 源码是src文件夹里面的Server.java
* 可执行的ja 是P3_chatting_bot.jar
* 实验报告 是report_3160101557.pdf


* 另外还有演示视频demo.mp4，下载链接：https://pan.zju.edu.cn/share/d2e5afcd2d4c85a47c5224a0bc
* 客户端测试用`nc localhost 8080` 模拟连接，同时在src文件夹里面有来自课堂示例的客户端代码

#### 如何运行服务端
```
java -jar P3_chatting_bot.jar
```
服务端的PORT是8080

#### 如何运行客户端
```
nc localhost 8080
```
来演示消息转发

另外，可以开启客户端测试代码来测试服务器转发消息压力：
在客户端测试代码文件夹中：`java MultiJabberClient` 来开测试服务器。