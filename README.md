> 记录自己的 SpringCloud 的学习过程，从 2025年9月21日开始。

# 0.分布式简述

部署一个项目，一般有三种方式：

1. 单体架构
2. 集群架构
3. 分布式架构

## 0.1. 单体架构

单体机构的基本逻辑走向是：

用户访问域名，自动转到绑定了这个域名的公网 IP 地址，我们的应用程序就部署在一个公网 IP 之中。

他的优点是：建立简单。

他的缺点是：一旦应用的并发量过大，就会产生问题。

## 0.2 集群架构

集群架构的基本逻辑走向是：

用户访问域名，自动转到绑定了这个域名的 公网 IP 地址，这个 IP 地址上部署了一个网关，一般是使用 Nginx，他能分配不用的用户，前往不同的服务器，这些服务器上分别都部署了我们的应用程序。

他的优点是：解决了单体架构高并发可能产生的宕机危险。

他的缺点是：不能模块化升级维护项目，且不支持一个项目用多个语言编写

## 0.3 分布式架构

分布式架构的基本逻辑走向是：

用户访问域名，自动转到绑定了这个域名的 公网 IP 地址，这个 IP 地址上部署了一个网关，一般是使用 Nginx，他能分配不用的用户，前往不同的服务器，这些服务器上分别都部署了我们的应用程序的某个或多个功能模块。

他的优点是：解决了单体架构高并发可能产生的宕机危险。同时解决了集群架构的缺点，每个业务模块与模块之间相互独立，支持多种语言编写。

# 1. 第一个微服务程序

## 1.1. 确定项目版本

1. 框架版本

   | SpringBoot | SpringCloud | SpringCloud Alibaba |
   | ---------- | ----------- | ------------------- |
   | 3.3.4      | 2023.0.3    | 2023.0.3.2          |

2. 组件版本

   | Nacos | Sentinel | Seata |
   | ----- | -------- | ----- |
   | 2.4.3 | 1.8.8    | 2.2.0 |

## 1.2. 创建 SpringBoot 项目

直接创建新的 SpringBoot 项目，取名 `cloud-demo`，创建完成之后删掉除了 `.idea` 文件夹 和 `pom.xml` 之外的所有文件。并将 `pom.xml` 文件修改为：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <!--确定 SpringBoot 的版本-->
        <version>3.3.4</version>
        <relativePath/>
    </parent>
    <!--确定打包方式-->
    <packaging>pom</packaging>
    <groupId>com.sangui</groupId>
    <artifactId>cloud-demo</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>cloud-demo</name>
    <description>cloud-demo</description>
    <!--添加依赖配置信息-->
    <properties>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <spring-cloud.version>2023.0.3</spring-cloud.version>
        <spring-cloud-alibaba.version>2023.0.3.2</spring-cloud-alibaba.version>
    </properties>
    <!--添加依赖-->
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
            <dependency>
                <groupId>com.alibaba.cloud</groupId>
                <artifactId>spring-cloud-alibaba-dependencies</artifactId>
                <version>${spring-cloud-alibaba.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>
```

## 1.3. 创建 services 模块

为项目添加新的模块，添加普通的 Java 模块，取名 `services`。这就是微服务模块。

删除改模块里的 `src` 文件夹，并修改该模块的打包方式为 `pom`，如：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>com.sangui</groupId>
        <artifactId>cloud-demo</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </parent>

    <!--设置打包方式-->
    <packaging>pom</packaging>

    <artifactId>services</artifactId>

    <properties>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

</project>
```

未来可在此 `pom.xml` 文件中加入依赖，每个具体的微服务模块都可以使用这个依赖（继承父依赖）。

## 1.4. 创建具体的微服务模块

在 services 里添加具体的微服务模块模块：`service-product`，该模块也是普通的 Java 模块。同样的道理，再添加一个 `service-order` 模块。

# 2. Nacos

**Nacos** 的全称是 Dynamic **Na**ming and **Co**nfiguration **S**ervice ，一个更易于构建云原生应用的**动态服务发现**、**配置管理**和**服务管理**平台，其官网为：https://nacos.io/ 。

## 2.1 Nacos 下载

下载地址：https://nacos.io/download/nacos-server/ 。选择版本为 2.4.3 的二进制包下载。

## 2.2. 启动 Nacos

下载好的 zip 包解压后即可启动。进入此文件夹的 bin 目录下，执行以下命令（Win 环境）即可以单机模式启动 Nacos：

```cmd
startup.cmd -m standalone
```

打开浏览器，输入：http://localhost:8848/nacos/ ，即可访问 Nacos，如下图。

![image-20250921163809259](README.assets/image-20250921163809259.png)

其中里面最重要的两个菜单就是 `配置管理` 和 `服务管理`。配置管理是作为配置中心，服务管理是作为注册中心。

## 2.3. Nacos 的功能——服务注册

+ **Step1 启动微服务**

  1. 引入依赖

     + spring-cloud-starter-alibaba-nacos-discovery

       在父服务中引入此依赖，子服务中因为继承关系，也会默认引入，无需再手动引入。

       ```xml
       <!--服务发现-->
       <dependency>
           <groupId>com.alibaba.cloud</groupId>
           <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
       </dependency>
       ```

     +  spring-boot-starter-web

       在子服务中引入 web 依赖

       ```xml
       <dependency>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-starter-web</artifactId>
       </dependency>
       ```

  2. 编写微服务的主入口程序

     ```java
     package com.sangui.order;
     
     
     import org.springframework.boot.SpringApplication;
     import org.springframework.boot.autoconfigure.SpringBootApplication;
     
     /**
      * @Author: sangui
      * @CreateTime: 2025-09-21
      * @Description: 订单微服务的主入口程序
      * @Version: 1.0
      */
     @SpringBootApplication
     public class OrderMainApplication {
         public static void main(String[] args) {
             SpringApplication.run(OrderMainApplication.class, args);
         }
     }
     ```

  3. 编写 yaml 配置文件

     ```yaml
     spring:
       application:
         # 设置这个微服务项目的项目名
         name: service-order
       cloud:
         nacos:
           # 配置 Nacos 地址，不配置也行，默认就是本地的 8848 端口
           server-addr: 127.0.0.1:8848
     
     server:
       # 设置这个微服务项目的端口号
       port: 8000
     ```

  至此，即可启动 order-server 这个微服务了。

+ **Step2 查看注册中心效果**

  打开浏览器，输入：http://localhost:8848/nacos/ ，查看`服务管理`的`服务列表`，即可看到刚刚注册的微服务了，如下图：

  ![image-20250922132756506](README.assets/image-20250922132756506.png)

  同样的步骤，为 `service-product` 这个服务，也注册启动，注意修改服务的端口号，再次查看 Nacos，就可以看到注册的两个微服务了，如下图：

  ![image-20250922132849586](README.assets/image-20250922132849586.png)

+ **Step3 集群模式启动测试**

  单机情况下可以通过改变端口号模拟微服务集群，那么，我们可以复制一个 OrderMainApplication 服务，并修改这个 OrderMainApplication 的端口，但不修改该服务的项目名。

  修改该服务端口的方法为：配置环境时修改选项，增加 `Program arguments` ， 在程序参数中输入不一样的端口号，如：

  ```
  --server.port=8001
  ```

  启动这个新的服务之后，照此操作，我又新建了若干个实例，server-order 我总共建立了两个实例，端口号分别是 8000 和 8001，server-product 我总共建立了三个实例，端口号分别是 9000、9001 和 9002。

  我们再去 Nacos 的服务列表看，看到 server-order 的服务的实例数就变成了 2 个，server-product 的服务的实例数就变成了 3 个，如下图：
  
  ![image-20250922132921317](README.assets/image-20250922132921317.png)

我们还可以点击某个服务的详情，里面记录了这个服务的每个实例信息，包括该实例的 IP、端口号、项目名

## 2.4. Nacos 的功能——服务发现

+ **Step1 开启服务发现功能**

  在微服务的入口程序中使用 @EnableDiscoveryClient 注解，即可开启微服务的发现功能，如：

  ```java
  package com.sangui.product;
  
  
  import org.springframework.boot.SpringApplication;
  import org.springframework.boot.autoconfigure.SpringBootApplication;
  import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
  
  /**
   * @Author: sangui
   * @CreateTime: 2025-09-21
   * @Description: 产品微服务的主入口程序
   * @Version: 1.0
   */
  // 开启服务发现功能
  @EnableDiscoveryClient
  @SpringBootApplication
  public class ProductMainApplication {
      public static void main(String[] args) {
          SpringApplication.run(ProductMainApplication.class, args);
      }
  }
  ```

+ **Step2 测试服务发现的 DiscoveryClient**

  1. 加入测试依赖

     ```xml
     <dependency>
         <groupId>org.springframework.boot</groupId>
         <artifactId>spring-boot-starter-test</artifactId>
         <scope>test</scope>
     </dependency>
     ```

  2. 写测试程序

     ```java
     package com.sangui.product;
     
     
     import jakarta.annotation.Resource;
     import org.junit.jupiter.api.Test;
     import org.springframework.boot.test.context.SpringBootTest;
     import org.springframework.cloud.client.ServiceInstance;
     import org.springframework.cloud.client.discovery.DiscoveryClient;
     
     import java.util.List;
     
     /**
      * @Author: sangui
      * @CreateTime: 2025-09-22
      * @Description: 测试服务发现
      * @Version: 1.0
      */
     @SpringBootTest
     public class DiscoveryTest {
         // 自动注入即可
         @Resource
         DiscoveryClient discoveryClient;
     
         /**
          * 测试 DiscoveryClient
          */
         @Test
         void discoveryClientTest() {
             // 遍历打印所有微服务
             for (String service : discoveryClient.getServices()) {
                 System.out.println("MyService:" + service);
                 /*
                 上述打印结果：
                 MyService:service-order
                 MyService:service-product
                  */
     
                 // 获取某一个微服务的所有实例
                 List<ServiceInstance> instances = discoveryClient.getInstances(service);
                 for (ServiceInstance instance : instances) {
                     System.out.println("该微服务的名字是：" + service + "，该实例 IP 为：" + instance.getHost() + "，端口号为：" + instance.getPort());
                     /*
                     上述打印结果：
                     该微服务的名字是：service-order，该实例 IP 为：192.168.28.1，端口号为：8001
                     该微服务的名字是：service-order，该实例 IP 为：192.168.28.1，端口号为：8000
                     该微服务的名字是：service-product，该实例 IP 为：192.168.28.1，端口号为：9000
                     该微服务的名字是：service-product，该实例 IP 为：192.168.28.1，端口号为：9001
                     该微服务的名字是：service-product，该实例 IP 为：192.168.28.1，端口号为：9002
                      */
                 }
             }
         }
     }
     ```

+ **Step3 测试服务发现的 NacosServiceDiscovery**

  除了使用 SpringCloud 自带的 API，还可以使用 Nacos 提供的 API，即 NacosServiceDiscovery。

  ```java
  @Resource
  NacosServiceDiscovery nacosServiceDiscovery;
  
  @Test
  void nacosDiscoveryClientTest() throws NacosException {
      // 遍历打印所有微服务
      for (String service : nacosServiceDiscovery.getServices()) {
          System.out.println("MyService:" + service);
          /*
          上述打印结果：
          MyService:service-order
          MyService:service-product
           */
  
          // 获取某一个微服务的所有实例
          List<ServiceInstance> instances = nacosServiceDiscovery.getInstances(service);
          for (ServiceInstance instance : instances) {
              System.out.println("该微服务的名字是：" + service + "，该实例 IP 为：" + instance.getHost() + "，端口号为：" + instance.getPort());
              /*
              上述打印结果：
              该微服务的名字是：service-order，该实例 IP 为：192.168.28.1，端口号为：8001
              该微服务的名字是：service-order，该实例 IP 为：192.168.28.1，端口号为：8000
              该微服务的名字是：service-product，该实例 IP 为：192.168.28.1，端口号为：9000
              该微服务的名字是：service-product，该实例 IP 为：192.168.28.1，端口号为：9001
              该微服务的名字是：service-product，该实例 IP 为：192.168.28.1，端口号为：9002
               */
          }
      }
  }
  ```

  这里的测试的代码几乎和 DiscoveryClient 的代码一模一样，只是把注入的对象换成了 nacosDiscoveryClient，加了异常抛出，具体 API 是一样的。

其实在之后的实际情况中，我们其实无需底层调用这些代码，之后的`远程调用`用到的`服务发现`功能，会封装化为自动化的过程，无需我们手动写。唯一要记住的就是，所有的微服务都可以加入 @EnableDiscoveryClient 注解，这样之后微服务才能发现别的微服务，之间才可以互相调用

## 2.5. Nacos ——编写微服务 API

这里，通过一个简单的例子，来学习我们的编写微服务的 API。我们的例子是订单和商品两个微服务之间的关联。订单的实体类中包含商品列表，也就是是说一个订单对应这多个商品，订单需要商品提供商品信息来确定订单总额，商品单独存在则无意义，现在让这两个微服务之间相互联系。

1. 独自编写好 product 服务

   ```java
   package com.sangui.product.controller;
   
   
   import com.sangui.product.bean.Product;
   import com.sangui.product.service.ProductService;
   import jakarta.annotation.Resource;
   import org.springframework.web.bind.annotation.GetMapping;
   import org.springframework.web.bind.annotation.PathVariable;
   import org.springframework.web.bind.annotation.RequestParam;
   import org.springframework.web.bind.annotation.RestController;
   
   /**
    * @Author: sangui
    * @CreateTime: 2025-09-22
    * @Description: 商品的 Controller
    * @Version: 1.0
    */
   @RestController
   public class ProductController {
       @Resource
       private ProductService productService;
   
       // 查询商品
       @GetMapping("/product/{id}")
       public Product getProduct(@PathVariable("id") Long productId){
           return productService.getProductById(productId);
       }
   }
   ```

   ```java
   package com.sangui.product.service.impl;
   
   
   import com.sangui.product.bean.Product;
   import com.sangui.product.service.ProductService;
   import org.springframework.stereotype.Service;
   
   import java.math.BigDecimal;
   
   /**
    * @Author: sangui
    * @CreateTime: 2025-09-22
    * @Description: 商品的 Service 类 的实现类
    * @Version: 1.0
    */
   @Service
   public class ProductServiceImpl implements ProductService {
       @Override
       public Product getProductById(Long productId) {
           // 暂且模拟数据库
           return new Product(productId,new BigDecimal("32.5"),"创可贴",2);
       }
   }
   ```

   ```java
   package com.sangui.product.bean;
   
   
   import lombok.AllArgsConstructor;
   import lombok.Data;
   import lombok.NoArgsConstructor;
   
   import java.math.BigDecimal;
   
   /**
    * @Author: sangui
    * @CreateTime: 2025-09-22
    * @Description: 商品类
    * @Version: 1.0
    */
   @Data
   @AllArgsConstructor
   @NoArgsConstructor
   public class Product {
       private Long id;
       private BigDecimal price;
       private String productName;
       private Integer number;
   }
   ```

   打开浏览器，输入：http://localhost:9000/product/3 （端口改为 9001 或 9002 均可），可以看到，后端返回了如下数据：

   ```json
   {
     "id": 3,
     "price": 32.5,
     "productName": "创可贴",
     "number": 2
   }
   ```

2. 独自编写好 order服务

   ```java
   package com.sangui.order.controller;
   
   
   import com.sangui.order.bean.Order;
   import com.sangui.order.service.OrderService;
   import jakarta.annotation.Resource;
   import org.springframework.web.bind.annotation.GetMapping;
   import org.springframework.web.bind.annotation.RequestParam;
   import org.springframework.web.bind.annotation.RestController;
   
   /**
    * @Author: sangui
    * @CreateTime: 2025-09-22
    * @Description:
    * @Version: 1.0
    */
   @RestController
   public class OrderController {
       @Resource
       private OrderService orderService;
   
       @GetMapping("/create")
       public Order createOrder(@RequestParam("productId") Long productId,
                                @RequestParam("userId") Long userId){
           return orderService.createOrder(productId,userId);
       }
   }
   ```

   ```java
   package com.sangui.order.service.impl;
   
   
   import com.sangui.order.bean.Order;
   import com.sangui.order.service.OrderService;
   import org.springframework.stereotype.Service;
   
   import java.math.BigDecimal;
   
   /**
    * @Author: sangui
    * @CreateTime: 2025-09-22
    * @Description:
    * @Version: 1.0
    */
   @Service
   public class OrderServiceImpl implements OrderService {
       @Override
       public Order createOrder(Long productId, Long userId) {
           Order order = new Order();
           order.setId(1011L);
           // TODO 总金额
           order.setTotalAmount(new BigDecimal("0"));
           order.setUserId(userId);
           order.setNickName("张三");
           order.setAddress("beijing");
           // TODO 远程查询商品列表
           order.setProductList(null);
           return order;
       }
   }
   ```

   ```java
   package com.sangui.order.bean;
   
   
   import lombok.Data;
   
   import java.math.BigDecimal;
   import java.util.List;
   
   /**
    * @Author: sangui
    * @CreateTime: 2025-09-22
    * @Description: 订单的实体类
    * @Version: 1.0
    */
   @Data
   public class Order {
       private Long id;
       private BigDecimal totalAmount;
       private Long userId;
       private String nickName;
       private String address;
       private List<Object> productList;
   }
   ```

   打开浏览器，输入：http://localhost:8000/create?productId=1&userId=2 （端口改为 8001 也可），可以看到，后端返回了如下数据：

   ```json
   {
     "id": 1011,
     "totalAmount": 0,
     "userId": 2,
     "nickName": "张三",
     "address": "beijing",
     "productList": null
   }
   ```

3. 远程调用

   + 独立 model 模块

     此时，我们发现，比如：在 order 模块中，我们创建不了 product 这个实体类，因为他们在不同的模块之中。现在，我们要在我们的 service 模块的同级目录上，新建一个普通 Java 模块，取名为 model，这里会写上所有通用的 model 实体类，这样，就可以删除各自服务之下的 bean 包了，把这些 model 统一移至 model 模块里。

     同时，因为 model 模块和 service 模块相对独立，就需要在 service 模块的 pom 文件中，加入 model 模块的依赖，如：

     ```xml
     <!--加入 model 依赖-->
     <dependency>
         <groupId>com.sangui</groupId>
         <artifactId>model</artifactId>
         <version>0.0.1-SNAPSHOT</version>
     </dependency>
     ```

     至此，各个 service 模块中的就可以使用公共的 model 实体类了。

   + 引入 RestTemplate 配置

     这里，还需要引入 SpringBoot 的 RestTemplate 对象，它可以是 Spring 框架中的一个同步 HTTP 客户端，用于执行 HTTP 请求，我们将其作为配置类，每个微服务的模块都可以加入，需要用时直接注入，如：

     ```java
     package com.sangui.product.config;
     
     
     import org.springframework.context.annotation.Bean;
     import org.springframework.context.annotation.Configuration;
     import org.springframework.web.client.RestTemplate;
     
     /**
      * @Author: sangui
      * @CreateTime: 2025-09-22
      * @Description:
      * @Version: 1.0
      */
     @Configuration
     public class ProductServiceConfig {
         @Bean
         public RestTemplate restTemplate(){
             return new RestTemplate();
         }
     }
     ```

     ```java
     package com.sangui.order.config;
     
     
     import org.springframework.context.annotation.Bean;
     import org.springframework.context.annotation.Configuration;
     import org.springframework.web.client.RestTemplate;
     
     /**
      * @Author: sangui
      * @CreateTime: 2025-09-22
      * @Description:
      * @Version: 1.0
      */
     @Configuration
     public class OrderServiceConfig {
         @Bean
         public RestTemplate restTemplate(){
             return new RestTemplate();
         }
     }
     ```

   + 真正的远程调用

     增加真正的远程调用方法：getProductFromRemote，在之前写好的 createOrder 方法中使用它。

     ```java
     package com.sangui.order.service.impl;
     
     
     import com.sangui.order.bean.Order;
     import com.sangui.order.service.OrderService;
     import com.sangui.product.bean.Product;
     import jakarta.annotation.Resource;
     import lombok.extern.slf4j.Slf4j;
     import org.springframework.beans.factory.annotation.Autowired;
     import org.springframework.cloud.client.ServiceInstance;
     import org.springframework.cloud.client.discovery.DiscoveryClient;
     import org.springframework.stereotype.Service;
     import org.springframework.web.client.RestTemplate;
     
     import java.math.BigDecimal;
     import java.util.Arrays;
     import java.util.List;
     
     /**
      * @Author: sangui
      * @CreateTime: 2025-09-22
      * @Description:
      * @Version: 1.0
      */
     @Slf4j
     @Service
     public class OrderServiceImpl implements OrderService {
         @Resource
         DiscoveryClient discoveryClient;
     
         @Resource
         RestTemplate restTemplate;
     
         @Override
         public Order createOrder(Long productId, Long userId) {
             // 调用远程获取商品的方法
             Product product = getProductFromRemote(productId);
     
             Order order = new Order();
             order.setId(1011L);
     
             // 使用商品获取总金额
             BigDecimal totalAmount = product.getPrice().multiply(new BigDecimal(product.getNumber()));
             order.setTotalAmount(totalAmount);
     
             order.setUserId(userId);
             order.setNickName("张三");
             order.setAddress("beijing");
     
             // 放入商品列表，我们模拟阶段，只放一个商品，修改 Order 实体类中的 productList 中 List 的数据类型为 Product
             order.setProductList(Arrays.asList(product));
             return order;
         }
     
         /**
          * 远程获取商品信息
          * @param productId 商品 Id
          * @return 商品信息
          */
         private Product getProductFromRemote(Long productId) {
             // 1、获取商品服务的所有实例，实例里包括其 IP + port
             List<ServiceInstance> serviceProductInstances = discoveryClient.getInstances("service-product");
     
             // 选择第一个实例
             ServiceInstance serviceProductInstance0 = serviceProductInstances.get(0);
             // 确定 url
             String url = "http://" + serviceProductInstance0.getHost() + ":" + serviceProductInstance0.getPort() + "/product/" + productId;
             // 上述 url 为 http://localhost:9000/product/{productId}
     
             // 2、发送远程请求
             log.info("远程请求 url:{}", url);
             return restTemplate.getForObject(url, Product.class);
         }
     }
     ```

至此，打开浏览器，输入：http://localhost:8000/create?productId=1&userId=2 （端口改为 8001 也可），可以看到，后端返回的数据变成了这样：

```json
{
  "id": 1011,
  "totalAmount": 65,
  "userId": 2,
  "nickName": "张三",
  "address": "beijing",
  "productList": [
    {
      "id": 1,
      "price": 32.5,
      "productName": "创可贴",
      "number": 2
    }
  ]
}
```

这样，我们就完成了一个极简单的微服务模拟项目，各个微服务之间可以互相访问调用。

请注意，我们可以从日志文件或者程序中可以看到，我们在每次都固定远程调用了第一个 service-product 的实例，但我们希望的是，他能够负载均衡得调用所有实例，那该怎么办呢？

## 2.6. Nacos ——负载均衡

Nacos 得负载均衡的核心就是，引入 `spring-cloud-starter-loadbalancer` 这个 jar 包。通过它提供的 LoadBalanceClient 组件，可以达到负载均衡的效果。下面我将详细解释。

+ **Step1 引入依赖**

  ```xml
  <!--用于负载均衡的依赖-->
  <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-loadbalancer</artifactId>
  </dependency>
  ```

+ **Step2 测试负载均衡**

  直接注入我们的 LoadBalancerClient，使用对象的 choose 方法，即可使用负载均衡。

  ```java
  package com.sangui.order;
  
  
  import jakarta.annotation.Resource;
  import org.junit.jupiter.api.Test;
  import org.springframework.boot.test.context.SpringBootTest;
  import org.springframework.cloud.client.ServiceInstance;
  import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
  
  /**
   * @Author: sangui
   * @CreateTime: 2025-09-22
   * @Description: 负载均衡的测试
   * @Version: 1.0
   */
  @SpringBootTest
  public class LoadBalancerTest {
      @Resource
      private LoadBalancerClient loadBalancerClient;
  
      @Test
      void loadBalancerTest() {
          for (int i = 0; i < 10; i++) {
              ServiceInstance serviceInstance = loadBalancerClient.choose("service-product");
              System.out.println("该实例 IP 为：" + serviceInstance.getHost() + "，端口号为：" + serviceInstance.getPort());
          }
  
      }
  }
  ```

  执行上述测试代码，我们会得到以下结果：

  ```
  该实例 IP 为：192.168.28.1，端口号为：9002
  该实例 IP 为：192.168.28.1，端口号为：9001
  该实例 IP 为：192.168.28.1，端口号为：9000
  该实例 IP 为：192.168.28.1，端口号为：9002
  该实例 IP 为：192.168.28.1，端口号为：9001
  该实例 IP 为：192.168.28.1，端口号为：9000
  该实例 IP 为：192.168.28.1，端口号为：9002
  该实例 IP 为：192.168.28.1，端口号为：9001
  该实例 IP 为：192.168.28.1，端口号为：9000
  该实例 IP 为：192.168.28.1，端口号为：9002
  ```

  可以看到，每次确实调用的是不一样的端口，让它负载均衡了！

+ **Step3 使用负载均衡**

  现在，我们改造之前的那个程序，让他变得负载均衡些，不再使用固定的实例。

  ```java
  /**
   * 远程获取商品信息
   * @param productId 商品 Id
   * @return 商品信息
   */
  private Product getProductFromRemote(Long productId) {
      // 1、获取商品服务的所有实例，实例里包括其 IP + port
      List<ServiceInstance> serviceProductInstances = discoveryClient.getInstances("service-product");
  
      // 选择第一个实例
      ServiceInstance serviceProductInstance0 = serviceProductInstances.get(0);
  
      ServiceInstance serviceProductInstance = loadBalancerClient.choose("service-product");
      // 确定 url
      String url = "http://" + serviceProductInstance0.getHost() + ":" + serviceProductInstance0.getPort() + "/product/" + productId;
      // 上述 url 为 http://localhost:9000/product/{productId}
  
      // 2、发送远程请求
      log.info("远程请求 url:{}", url);
      return restTemplate.getForObject(url, Product.class);
  }
  
  /**
   * 负载均衡得远程获取商品信息
   * @param productId 商品 Id
   * @return 商品信息
   */
  private Product getProductFromRemoteWithLoadBalance(Long productId) {
      // 1、负载均衡得获取商品服务的所有实例，实例里包括其 IP + port
      ServiceInstance serviceProductInstance = loadBalancerClient.choose("service-product");
  
      // 确定 url
      String url = "http://" + serviceProductInstance.getHost() + ":" + serviceProductInstance.getPort() + "/product/" + productId;
      // 上述 url 为 http://localhost:9000/product/{productId}
  
      // 2、发送远程请求
      log.info("远程请求 url:{}", url);
      return restTemplate.getForObject(url, Product.class);
  }
  ```

  getProductFromRemoteWithLoadBalance 方法就是我们改造之后的新方法，多次测试之后，看程序的输出日志，如下，可知我们已经成功负载均衡了。

  ```
  远程请求 url:http://192.168.28.1:9000/product/1
  远程请求 url:http://192.168.28.1:9002/product/1
  远程请求 url:http://192.168.28.1:9001/product/1
  远程请求 url:http://192.168.28.1:9000/product/1
  远程请求 url:http://192.168.28.1:9002/product/1
  远程请求 url:http://192.168.28.1:9001/product/1
  远程请求 url:http://192.168.28.1:9000/product/1
  远程请求 url:http://192.168.28.1:9002/product/1
  远程请求 url:http://192.168.28.1:9001/product/1
  远程请求 url:http://192.168.28.1:9000/product/1
  ```

+ **Step4 全新方法开启负载均衡**

  上述三个步骤，已经完成了负载均衡的需求，现在，有一种更简单的操作，就是直接我们配置 restTemplate 的类上，加上 @LoadBalanced 注解，即可开启均衡负载的需求。

  ```java
  package com.sangui.order.config;
  
  
  import org.springframework.cloud.client.loadbalancer.LoadBalanced;
  import org.springframework.context.annotation.Bean;
  import org.springframework.context.annotation.Configuration;
  import org.springframework.web.client.RestTemplate;
  
  /**
   * @Author: sangui
   * @CreateTime: 2025-09-22
   * @Description:
   * @Version: 1.0
   */
  @Configuration
  public class OrderServiceConfig {
      // 方法上加入注解，开启负载均衡
      @LoadBalanced
      @Bean
      public RestTemplate restTemplate(){
          return new RestTemplate();
      }
  }
  ```

  ```java
  /**
   * 基于注解的负载均衡远程获取商品信息
   * @param productId 商品 Id
   * @return 商品信息
   */
  private Product getProductFromRemoteWithLoadBalanceAnnotation(Long productId) {
  
      // 确定 url，这里直接写 service-product 这个微服务名就好，restTemplate 会自动负载均衡对应的微服务
      String url = "http://service-product/product/" + productId;
      // 上述 url 为 http://localhost:????/product/{productId}
  
      // 2、发送远程请求
      return restTemplate.getForObject(url, Product.class);
  }
  ```

  然后我们直接调用这里的 getProductFromRemoteWithLoadBalanceAnnotation 方法创建商品即可，这里我们不能用 log 告知我们 url 具体指向了哪里，因为它底层隐去了具体选择的实例。我们可以再商品服务里，商品创建的时候，输出一句话，看看是那个实例会输出我们的话，结果当然也是负载均衡的。

  而今后，用的也是基于注解的方式居多。

## 2.7. Nacos ——一个疑问

之前一直有一个疑问，分布式架构之所以会优于单体架构，其中一个原因就是分布式架构其中一个模块宕机了，不影响全局，会有别的同样的模块顶上，而单体架构就不一样，单体架构就一个实例，它宕机了，也就全没了。而我的疑问就是，我们现在的分布式架构依赖于 Nacos 注册中心，注册中心中会有全部的实例信息，从而可以避免单个实例宕机而影响整个程序，那么要是 Nacos 宕机了呢？不是也影响了整个程序吗，这就能说是优于单体架构了吗？

这其实引申出来一个经典面试题，就是，微服务的注册中心宕机了，远程调用还能成功吗？

我们看之前的程序，我们之前负载均衡得远程调用的 url 是："http://service-product/product/" + productId。这里看起来，因该有两个核心步骤

1. 请求注册中心，获取所有微服务的列表（之后自己会负载均衡得选择一个）
2. 给目标微服务发送一个我真正的请求

你看，每次我们发送一个请求，就需要额外给注册中心再发送一个请求，其实很费时间，所以在设计注册中心的时候，我们其实不是每次请求都会去访问注册中心的，当你请求过一次之后，就会有对应的实例缓存，之后的请求，就不会再额外像注册中心再请求了，因为响应的内容其实是一样的，当某个实例下线了，注册中心还会将这个缓存同步更换。我们是也可验证这个观点的，在执行程序过后，我们继续执行程序，但是现在把 Nacos 关闭，去看程序是否可以正确执行。

现在我们再看这个问题，微服务的注册中心宕机了，远程调用还能成功吗？

我们分情况讨论：

1. 之前调用过注册中心

   调用过注册中心，就证明，我们有这个实例缓存，大不了是不能再实时更新现存的实例了，远程调用大概率还是可以成功的。

2. 之前还没有调用过注册中心

   没调用过注册中心，说明是第一次发起远程调用，就不能通过远程调用。

## 2.8. Nacos ——配置中心的基础用法

+ **Step1 添加依赖**

  在 Service 模块的 pom 文件中引入 Nacos 的配置中心的依赖：

  ```xml
  <!--Nacos 配置中心的依赖-->
  <dependency>
      <groupId>com.alibaba.cloud</groupId>
      <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
  </dependency>
  ```

  注意，一旦加入这个依赖，若没有写 Step2 的配置 或 主动关闭，程序直接报错不能运行：

  ```
  17:02:01.409 [main] ERROR org.springframework.boot.diagnostics.LoggingFailureAnalysisReporter -- 
  
  ***************************
  APPLICATION FAILED TO START
  ***************************
  
  Description:
  
  No spring.config.import property has been defined
  
  Action:
  
  Add a spring.config.import=nacos: property to your configuration.
  	If configuration is not required add spring.config.import=optional:nacos: instead.
  	To disable this check, set spring.cloud.nacos.config.import-check.enabled=false.
  
  
  Process finished with exit code 1
  ```

  > 温馨提示，添加依赖后，若要主动关闭，需在 yaml 文件中加入以下配置：
  >
  > ```yaml
  > spring:
  >     cloud:
  >        nacos:
  >          config:
  >            import-check:
  >              enabled: false
  > ```

+ **Step2 添加配置**

  在各个需要使用配置的 yaml 文件中，写入如下配置，这些配置可以在项目启动后，导入 Nacos 的那个文件的配置

  ```yaml
  spring:
    config:
      # 添加 Nacos 配置，项目启动后，导入 service-order.yml 这个配置文件
      import:
        - nacos:service-order.yaml
  ```

+ **Step3 写具体配置**

  在 Nacos 的主页的配置管理的配置列表处，选择创建配置，会看到如下图所示内容，依次输入 Data ID，配置格式 和 配置内容。

  ![image-20250922165642687](README.assets/image-20250922165642687.png)

  Data ID 就是这个配置文件的名字，也是你在 SpringBoot 的 yaml 文件中写入想要导入的文件的名字。

  配置格数不多说，就是该文件的格式。

  格式内容就是具体的内容。

+ **Step4 测试导入**

  OrderController 中的 config 方法就是我的测试。
  
  ```java
  package com.sangui.order.controller;
  
  
  import com.sangui.order.bean.Order;
  import com.sangui.order.service.OrderService;
  import jakarta.annotation.Resource;
  import org.springframework.beans.factory.annotation.Value;
  import org.springframework.web.bind.annotation.GetMapping;
  import org.springframework.web.bind.annotation.RequestParam;
  import org.springframework.web.bind.annotation.RestController;
  
  /**
   * @Author: sangui
   * @CreateTime: 2025-09-22
   * @Description:
   * @Version: 1.0
   */
  @RestController
  public class OrderController {
      @Value("${order.timeout}")
      String orderTimeout;
      @Value("${order.auto-confirm}")
      String orderAutoConfirm;
  
      @Resource
      private OrderService orderService;
  
      @GetMapping("/create")
      public Order createOrder(@RequestParam("productId") Long productId,
                               @RequestParam("userId") Long userId) {
          return orderService.createOrder(productId, userId);
      }
  
      @GetMapping("/config")
      public String config() {
          return "orderTimeout=" + orderTimeout + ",orderAutoConfirm=" + orderAutoConfirm;
      }
  }
  
  ```
  
  至此，打开浏览器，输入：http://localhost:8000/config （端口改为 8001 也可），可以看到，返回的数据是这样：
  
  ```
  orderTimeout=30min,orderAutoConfirm=7d
  ```
  
  但是，当我们现在去更新 Nacos 上的数据时，发现浏览器并不能实时更新，它依旧是使用了旧的数据。此时，我们需要在对应的 Controller 类上加入 @RefreshScope 注解，以实时更新，如：
  
  ```java
  package com.sangui.order.controller;
  
  
  import com.sangui.order.bean.Order;
  import com.sangui.order.service.OrderService;
  import jakarta.annotation.Resource;
  import org.springframework.beans.factory.annotation.Value;
  import org.springframework.cloud.context.config.annotation.RefreshScope;
  import org.springframework.web.bind.annotation.GetMapping;
  import org.springframework.web.bind.annotation.RequestParam;
  import org.springframework.web.bind.annotation.RestController;
  
  /**
   * @Author: sangui
   * @CreateTime: 2025-09-22
   * @Description:
   * @Version: 1.0
   */
  @RefreshScope
  @RestController
  public class OrderController {
      @Value("${order.timeout}")
      String orderTimeout;
      @Value("${order.auto-confirm}")
      String orderAutoConfirm;
  
      @Resource
      private OrderService orderService;
  
      @GetMapping("/create")
      public Order createOrder(@RequestParam("productId") Long productId,
                               @RequestParam("userId") Long userId) {
          return orderService.createOrder(productId, userId);
      }
  
      @GetMapping("/config")
      public String config() {
          return "orderTimeout=" + orderTimeout + ",orderAutoConfirm=" + orderAutoConfirm;
      }
  }
  ```

## 2.9. Nacos ——配置中心的动态刷新

上一小节中，我们使用了配置中心，使用的是 @Value 注解，一旦需要导入的变量多了，这个代码就不方便了。使用 @ConfigurationProperties 可以实现无感动态刷新功能。

我们可以定义一个专门存放 Properties 的类：

```java
package com.sangui.order.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: sangui
 * @CreateTime: 2025-09-22
 * @Description: 订单的配置文件类
 * @Version: 1.0
 */
// 批量绑定的前缀
@ConfigurationProperties(prefix = "order")
@Component
@Data
public class OrderProperties {

    String timeout;
    // 配置文件中是 auto-confirm 也没关系，会自动驼峰映射成 autoConfirm
    String autoConfirm;
}
```

这里的导入，无需上一章节的 @RefreshScope 注解，即可自动刷新。使用的代码如下：

```java
package com.sangui.order.controller;


import com.sangui.order.bean.Order;
import com.sangui.order.properties.OrderProperties;
import com.sangui.order.service.OrderService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: sangui
 * @CreateTime: 2025-09-22
 * @Description:
 * @Version: 1.0
 */
// @RefreshScope
@RestController
public class OrderController {
    //    @Value("${order.timeout}")
    //    String orderTimeout;
    //    @Value("${order.auto-confirm}")
    //    String orderAutoConfirm;
    
    @Resource
    OrderProperties orderProperties;

    @Resource
    private OrderService orderService;

    @GetMapping("/create")
    public Order createOrder(@RequestParam("productId") Long productId,
                             @RequestParam("userId") Long userId) {
        return orderService.createOrder(productId, userId);
    }

    @GetMapping("/config")
    public String config() {
        return "orderTimeout=" + orderProperties.getTimeout() + ",orderAutoConfirm=" + orderProperties.getAutoConfirm();
    }
}
```

访问浏览器，是可以看到我们的实时更新的数据的。我们更推荐使用这种方式来进行动态刷新。

## 2.10. Nacos ——配置中心的配置监听

Nacos 可以通过 NaocosConfigManager 监听配置文件值的变化。

加入监听器，这个监听器会在我们程序启动后就一直监听

```java
package com.sangui.order;


import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @Author: sangui
 * @CreateTime: 2025-09-21
 * @Description: 订单微服务的主入口程序
 * @Version: 1.0
 */
@EnableDiscoveryClient
@SpringBootApplication
public class OrderMainApplication {
    /**
     * 我们的整个 web 程序启动后，就会执行这个 applicationRunner 里面的方法。
     * @param nacosConfigManager 注入的对象，用于配置监听
     * @return 具体的方法
     */
    @Bean
    ApplicationRunner applicationRunner(NacosConfigManager nacosConfigManager) {
        // 1.项目启动就去监听配置文件的变化
        return args->{
            // System.out.println("============");

            // 拿到配置服务
            ConfigService configService = nacosConfigManager.getConfigService();
            // 添加监听器，监听 yaml 配置文件，注意，这里也可以监控非 Nacos 的其他配置文件
            configService.addListener("service-order.yaml", "DEFAULT_GROUP", new Listener() {
                @Override
                public Executor getExecutor() {
                    return Executors.newFixedThreadPool(4);
                }

                @Override
                public void receiveConfigInfo(String s) {
                    // 2.发生变化后就拿到变化值
                    System.out.println("service-order.yaml 中改变了，改变内容为：" + s);
                    // 3.发送邮件
                    System.out.println("邮件通知（模拟）......");
                }
            });
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(OrderMainApplication.class, args);
    }
}
```

当我们改变 Nacos 中的配置文件的文件具体内容之后，后端输出了以下内容，证明它监听到了。

```
service-order.yaml 中改变了，改变内容为：order:
  timeout: 3min
  auto-confirm: 3d
邮件通知（模拟）......
```

## 2.11. Nacos ——配置冲突问题

若 Nacos 中的数据集和 application.yaml 中有相同的配置项，哪个会生效？

我们可以直接测试，在 application.yaml 中加入新的配置：

```yaml
order:
  timeout: asd
  auto-confirm: 1jkdas
```

而在 Nacos 配置中心中的数据是这样的：

```yaml
order:
  timeout: 3min
  auto-confirm: 3d
```

打开浏览器，输入：http://localhost:8000/config （端口改为 8001 也可），可以看到，返回的数据是这样：

```
orderTimeout=3min,orderAutoConfirm=3d
```

证明了，当这两个地方都配置了相同的数据项时，会生效 Nacos 配置中心的。

它遵循的是，外部优先。项目启动时，会将所以的配置项合并（包括外部的，内部的），有冲突的数据项，就会合并优先度高的数据，整个整体合并之后，就是我们的配置项，会存入环境变量当中，然后，我们的程序会从环境变量中获取配置值。

此外，还遵循先导入的优先，假如我们的数据是这样的：

```yaml
spring:
  config:
    import:
      - nacos:service-order1.yaml
      - nacos:service-order2.yaml
```

那么若 1 和 2 的配置有冲突，则会选择 1 中的值。

## 2.12. Nacos ——配置中心的数据隔离

需求：假设我们的项目有多套环境，比如 test 、dev 和 prod，即 测试、开发和生产环境，项目中每个微服务的配置信息在每套环境上的值可能不一样，要求每个环境中，配置文件的配置都不一样，项目需要根据不同的环境，切换相对应的配置。

如果要完成以上需求，其中的难点是如何：

- 区分多套环境
- 区分多种微服务
- 区分多种配置
- 按需加载配置

Nacos 的解决方案：

- 用名称空间区分多套环境
- 用 Group 区分多种微服务
- 用 Data-id 区分多种配置
- 使用 SpringBoot 激活对应环境的配置

切换环境时，只需要这样，在 下面的 namespace 处切换，group 写在对应的配置文件后面

```yaml
spring:
  profiles:
    # 代表现在是 test 环境
    active: test
  cloud:
    nacos:
      config:
        # 标记 namespace，和上边的环境绑定，是 test，若上边未指定，则默认为 dev
        namespace: ${spring.profiles.active:dev}
        import-check:
          # 使用这种切换环境的方式时，要把检查配置关闭
          enabled: false

---
 spring:
  config:
    import:
      - nacos:service-order1.yaml?group=order
      - nacos:service-order2.yaml?group=order
    activate:
      # 上边的 import 仅在 test 环境时激活
      on-profile: test
      
---
spring:
  config:
    import:
      - nacos:service-order1.yaml?group=order
      - nacos:service-order2.yaml?group=order
      - nacos:service-order3.yaml?group=order
    activate:
      # 上边的 import 仅在 dev 环境时激活
      on-profile: dev
```

# 3. OpenFeign

# 4. Sentinel

# 5. Gateway

# 6. Seata

