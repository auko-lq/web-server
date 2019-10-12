# 自实现web服务器
## 配置

conf文件夹里的myServer.properties可以设置文件根目录与端口

- 更目录为myServer文件夹的位置, 即文件根目录应为 xxxxxxx/myServer
- 端口默认为8889端口

## 主入口
MyServer.java

## 依赖
- log4j

## log4j配置
log文件相对路径待解决

## 特殊功能
- 没有uri时重定向到index.html
- uri没有后缀名时, 尝试访问对应的html文件, 若存在则重定向到该文件

## 计划
打包成可执行文件或者 用批处理打开, 但是因为idea的相对路径和java的相对路径有点晕, 待解决



## 流程简介

![未命名文件](F:\学习\笔记\markdown\img\未命名文件.png)

