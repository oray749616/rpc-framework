# rpc-framework

## 项目模块概览
- **rpc-api:** 通用服务接口
- **rpc-common:** 实体对象、工具类等公用类
- **rpc-core:** 框架的核心实现
- **test-client:** 测试用消费端（客户端）
- **test-server:** 测试用提供端（服务端）

## 更新记录
- **v1.0:** 实现了客户端与服务端的一个远程过程调用
- **v2.0:** 定义通用的消息格式，使用动态代理把不同的方法封装成统一的Request对象格式
- **v3.0:** 重构了服务端代码，添加线程池，对一些工具类进行解耦