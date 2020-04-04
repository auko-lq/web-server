### 静态web服务器

对之前的版本完成了重构（以前写的代码真**）

#### 配置

`conf`文件夹内可配置：

- 应用的位置，类似tomcat的context，默认为项目根目录的`webapps`文件夹
- 端口号，默认为`8889`
- 日志相关配置

#### 运行

- 可直接执行`webServer-2.0.jar`包，批处理命令也是执行的jar包
- 支持maven，可自行打包运行，需要注意的是，依赖的jar会打包到根目录的lib文件夹，而项目jar包是使用相对路径`lib\`来依赖的，所以需要把项目jar放到根目录，就如我提交的一样

#### 小功能

- 当不指定资源时，（即请求时端口号后面不加内容），会默认重定向到`index.html`
- 当请求资源时没有后缀名，会补上`.html`，并重定向

#### 流程图

![流程图.png](https://github.com/aukocharlie/web-server/blob/master/sources/%E6%B5%81%E7%A8%8B%E5%9B%BE.png?raw=true)

#### todo

- [ ] 添加线程池
- [ ] 使用`Netty`
- [ ] 进行压测