RabbitMQ的环境安装及配置(Windows)

------

## RabbitMQ简介:

MQ全称为Message Queue, 消息队列（MQ）是一种应用程序对应用程序的通信方法。应用程序通过读写出入队列的消息（针对应用程序的数据）来通信，而无需专用连接来链接它们。消息传递指的是程序之间通过在消息中发送数据进行通信，而不是通过直接调用彼此来通信，直接调用通常是用于诸如远程过程调用的技术。排队指的是应用程序通过 队列来通信。队列的使用除去了接收和发送应用程序同时执行的要求。其中较为成熟的MQ产品有IBM WEBSPHERE MQ等等。

## RabbitMQ的环境安装及配置

### RabbitMQ 下载

RabbitMQ下载地址:http://www.rabbitmq.com/install-windows.html
成功下载rabbitmq-server.exe程序之后，还需要在Windows上安装Erlang for Windows。
Erlang下载地址:http://www.erlang.org/downloads 。
根据电脑的系统选择版本，我的是windows系统64位的，所以选择Windows 64-bit Binary File的。
![这里写图片描述](/bf8960fb-e5b7-4ad4-9888-f6655824caa9\128\index_files\0.05919164442582631.png)

![这里写图片描述](/bf8960fb-e5b7-4ad4-9888-f6655824caa9\128\index_files\0.8460081092213104.png)

### Erlang for Windows 安装和配置

下载完成之后先安装Erlang for Windows。安装完后，配置环境变量。因为我的实在D:\Program Files\erl9.0 这里，所以配置环境变量的时候选择该路径就可以了。新建一个ERLANG_HOME，选择刚才的安装路径，并将这个路径加入系统环境变量Path中 [ ;%ERLANG_HOME%\bin ] 。 注意 “；”一定要加！

![这里写图片描述](/bf8960fb-e5b7-4ad4-9888-f6655824caa9\128\index_files\0.5832656177795776.png)

![这里写图片描述](/bf8960fb-e5b7-4ad4-9888-f6655824caa9\128\index_files\0.7027245904646522.png)

![这里写图片描述](/bf8960fb-e5b7-4ad4-9888-f6655824caa9\128\index_files\0.2272445533665519.png)

### RabbitMQ安装和配置

成功安装和配置好Erlang for Windows之后，再来安装配置rabbitMQ。双击下载下来的rabbitmq-server.exe并完成安装之后，依旧配置环境变量。依旧安装在D:\Program Files\RabbitMQ Server\rabbitmq_server-3.6.11。新建一个RABBITMQ_HOME，添加此路径 D:\Program Files\RabbitMQ Server\rabbitmq_server-3.6.11 。 并将此加入到系统环境变量Path [;%RABBITMQ_HOME%\sbin] 。
![这里写图片描述](/bf8960fb-e5b7-4ad4-9888-f6655824caa9\128\index_files\0.652184995016989.png)

![这里写图片描述](/bf8960fb-e5b7-4ad4-9888-f6655824caa9\128\index_files\0.7197283395179286.png)

### RabbitMQ启动

成功配置好RabbitMQ之后， 切换到D:\Program Files\RabbitMQ Server\rabbitmq_server-3.6.11\sbin 路径下，双击rabbitmq-server.bat 启动。
出现 Starting broker 则启动成功。如果提示【node with name rabbit already running on ***】的错误，就试着删除【C:\Users\Administrator\AppData\Roaming\rabbitmq】这个目录，如果还是没有效果，就点击开始菜单，在所有程序》RabbitMQ Service 》RabbitMQ Service stop，先关闭已经启动的RabbitMQ，然后再启动。

![这里写图片描述](/bf8960fb-e5b7-4ad4-9888-f6655824caa9\128\index_files\0.7611749224220188.png)

![这里写图片描述](/bf8960fb-e5b7-4ad4-9888-f6655824caa9\128\index_files\0.41048559921273564.png)

![这里写图片描述](/bf8960fb-e5b7-4ad4-9888-f6655824caa9\128\index_files\0.2557135228440602.png)

### RabbitMQ使用

成功安装配置好RabbitMQ之后，我们使用官方提供的一个web管理工具rabbitmq_management，安装RabbitMQ自带有该工具，切换到rabbitMQ sbin目录下，输入rabbitmq-plugins enable rabbitmq_management 启动。
成功启动之后，在浏览器输入 http://localhost:15672/ 账号密码都是：guest。

![这里写图片描述](/bf8960fb-e5b7-4ad4-9888-f6655824caa9\128\index_files\0.7993703771805044.png)

![这里写图片描述](/bf8960fb-e5b7-4ad4-9888-f6655824caa9\128\index_files\0.1593148556917753.png)

![这里写图片描述](/bf8960fb-e5b7-4ad4-9888-f6655824caa9\128\index_files\0.8415602218844298.png)