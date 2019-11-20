# SpringCloudStudy-Practical
本项目是SpringCloud实战学习，集成了eureka、fegin、zuul、分布式事务tx-lcn、swagger、Spring Security、SpringBoot Admin、Oauth2（redis和mysql两种存储方式）、SpringCloud Config、SpringCloud Bus

项目分为两部分，服务端springcloudserver和客户端springcloudclient

------



GitHub地址： [https://github.com/wangliu1102/SpringCloudStudy-Practical](https://github.com/wangliu1102/SpringCloudStudy-Practical.git)

# 

# 具体配置文件，参考项目源码  

​    服务端和客户端分开

# 父类

  最开始导入如下依赖：

 

```
    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
        <spring-cloud.version>Greenwich.SR2</spring-cloud.version>
        <spring-boot.version>2.1.6.RELEASE</spring-boot.version>
    </properties>   
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
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-dependencies</artifactId>
                <version>${spring-boot.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
    </dependencies>
    <build>
        <!--finalName填入父项目名称 springcloudserver/springcloudclient-->
        <finalName>springcloudserver</finalName>
        <resources>
            <resource>
                <directory>src/main/resources</directory>
                <filtering>true</filtering>
            </resource>
        </resources>
    </build>
```

# Eureka

Eureka是什么：Eureka是Netflix的一个子模块，也是核心模块之一。Eureka是一个基于REST的服务，用于定位服务，以实现云端中间层服务发现和故障转移。Netflix在设计Eureka时遵守的就是AP原则。服务注册与发现对于微服务架构来说是非常重要的，有了服务发现与注册，只需要使用服务的标识符，就可以访问到服务，而不需要修改服务调用的配置文件了。功能类似于dubbo的注册中心，比如Zookeeper。

Eureka的基本架构： 

- Spring Cloud 封装了 Netflix 公司开发的 Eureka 模块来实现服务注册和发现(请对比Zookeeper)。
- Eureka 采用了 C-S 的设计架构。Eureka Server 作为服务注册功能的服务器，它是服务注册中心。
- 而系统中的其他微服务，使用 Eureka 的客户端连接到 Eureka Server并维持心跳连接。这样系统的维护人员就可以通过 Eureka Server 来监控系统中各个微服务是否正常运行。SpringCloud 的一些其他模块（比如Zuul）就可以通过 Eureka Server 来发现系统中的其他微服务，并执行相关的逻辑。

请注意和Dubbo的架构对比

![img](2c2154f6-4be6-4bb8-9275-0c4fe7e11497/128/index_files/31f40d44-80d8-43a2-ae97-dade08bf1dce.jpg)![img](2c2154f6-4be6-4bb8-9275-0c4fe7e11497/128/index_files/98639013-b79b-4811-a640-d48e4f6c8f2e.jpg)Eureka包含两个组件：Eureka Server和Eureka Client

Eureka Server提供服务注册服务，各个节点启动后，会在EurekaServer中进行注册，这样EurekaServer中的服务注册表中将会存储所有可用服务节点的信息，服务节点的信息可以在界面中直观的看到。

Eureka Client是一个Java客户端，用于简化Eureka Server的交互，客户端同时也具备一个内置的、使用轮询(round-robin)负载算法的负载均衡器。在应用启动后，将会向Eureka Server发送心跳(默认周期为30秒)。如果Eureka Server在多个心跳周期内没有接收到某个节点的心跳，EurekaServer将会从服务注册表中把这个服务节点移除（默认90秒）。

三大角色：

- Eureka Server 提供服务注册和发现；
- Service Provider服务提供方将自身服务注册到Eureka，从而使服务消费方能够找到；
- Service Consumer服务消费方从Eureka获取注册服务列表，从而能够消费服务。 

**eureka自我保护**

故障现象： 

![img]( 2c2154f6-4be6-4bb8-9275-0c4fe7e11497/128/index_files/22536531-7db0-48a2-af56-9246f8545920.png)

![img](2c2154f6-4be6-4bb8-9275-0c4fe7e11497/128/index_files/1cbc4511-a3fa-4ad9-a1d2-6f52d69d0a28.jpg)

导致原因：一句话：某时刻某一个微服务不可用了，eureka不会立刻清理，依旧会对该微服务的信息进行保存。

自我保护模式：默认情况下，如果EurekaServer在一定时间内没有接收到某个微服务实例的心跳，EurekaServer将会注销该实例（默认90秒）。但是当网络分区故障发生时，微服务与EurekaServer之间无法正常通信，以上行为可能变得非常危险了——因为微服务本身其实是健康的，此时本不应该注销这个微服务。Eureka通过“自我保护模式”来解决这个问题——当EurekaServer节点在短时间内丢失过多客户端时（可能发生了网络分区故障），那么这个节点就会进入自我保护模式。一旦进入该模式，EurekaServer就会保护服务注册表中的信息，不再删除服务注册表中的数据（也就是不会注销任何微服务）。当网络故障恢复后，该Eureka Server节点会自动退出自我保护模式。

在自我保护模式中，Eureka Server会保护服务注册表中的信息，不再注销任何服务实例。当它收到的心跳数重新恢复到阈值以上时，该Eureka Server节点就会自动退出自我保护模式。它的设计哲学就是宁可保留错误的服务注册信息，也不盲目注销任何可能健康的服务实例。一句话讲解：好死不如赖活着。

 综上，自我保护模式是一种应对网络异常的安全保护措施。它的架构哲学是宁可同时保留所有微服务（健康的微服务和不健康的微服务都会保留），也不盲目注销任何健康的微服务。使用自我保护模式，可以让Eureka集群更加的健壮、稳定。

 在Spring Cloud中，可以使用eureka.server.enable-self-preservation = false 禁用自我保护模式。

## 1、服务端

父项目下新建子Module，名称为eureka-server

导入相关依赖

 

```
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
        </dependency>
```

开启注解

 

```
@EnableEurekaServer
```

   ![img](/a6a8e6ec-bf20-4a66-9d7b-4fbd37fb1d26/128/index_files/27c09413-3aa8-4938-8aee-ee718121f6cc.png)

 使用多个配置文件启动多个eureka服务，需要设置配置文件名称，把共有部分放在application.yml中，独有部分放在自己文件中，并在启动参数Program arguments中设置

 

```
--spring.profiles.active=eureka7002
```

![img](/a6a8e6ec-bf20-4a66-9d7b-4fbd37fb1d26/128/index_files/aac61653-c86a-40a1-9b37-cd5a0d10d9f1.png)

新建配置文件application.yml、application-eureka7001、application-eureka7002.yml

这里我需要两个eureka服务端，所以配置了两个端口的eureka服务

application.yml存放共有部分：

 

```
eureka:
  client:
    register-with-eureka: false     #false表示不向注册中心注册自己。
    fetch-registry: false     #false表示自己端就是注册中心，我的职责就是维护服务实例，并不需要去检索服务
    service-url:
      defaultZone: http://eureka7001.com:7001/eureka/,http://eureka7002.com:7002/eureka/
```

application-eureka7001和application-eureka7002.yml存放独有部分：

 

```
server:
  port: 7001
eureka:
  instance:
    hostname: eureka7001.com #端口为7001的eureka服务端的实例名称
```

 

```
server:
  port: 7002
eureka:
  instance:
    hostname: eureka7002.com #端口为7002的eureka服务端的实例名称
```

eureka.instance.hostname配置的实例名称需要在host文件中配置：

![img](/a6a8e6ec-bf20-4a66-9d7b-4fbd37fb1d26/128/index_files/dbb572dc-66f5-4105-9747-6342ab44cfda.png)

启动Eureka服务端后，可以在浏览器中访问：

http://eureka7001.com:7001/

http://eureka7002.com:7002/

## 2、Eureka开启Security登录校验

Eureka服务端eureka-server中导入相关依赖

 

```
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-security</artifactId>
        </dependency>
```

修改application.yml配置文件，客户端注册进服务中心，配置文件中eureka.service-url.defaultZone也必须添加用户名和密码：

 

```
eureka:
  client:
    register-with-eureka: false     #false表示不向注册中心注册自己。
    fetch-registry: false     #false表示自己端就是注册中心，我的职责就是维护服务实例，并不需要去检索服务
    service-url:
      #url中添加用户名和密码
      defaultZone: http://admin:123456@eureka7001.com:7001/eureka/,http://admin:123456@eureka7002.com:7002/eureka/
spring:
  security:
    user:
      name: admin  #用户名
      password: 123456 #密码
```

新版（Spring Cloud 2.0 以上）的security默认启用了csrf检验，要在eureka-server端配置security的csrf检验为false，否则eureka 客户端无法注册到eureka

配置WebSecurityConfig

 

```
/**
 * 新版（Spring Cloud 2.0 以上）的security默认启用了csrf检验，要在eurekaServer端配置security的csrf检验为false
 *
 * @author 王柳
 * @date 2019/11/7 11:16
 */
@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()//关闭csrf
                .authorizeRequests().anyRequest().authenticated().and().httpBasic();
    }
}
```

![img](/a6a8e6ec-bf20-4a66-9d7b-4fbd37fb1d26/128/index_files/4f59fe97-aec4-45aa-9131-582b0bb0ea29.png)

## 3、客户端

因为新建的微服务都需要注册进注册中心Eureka中，所以不在子Module中引入依赖，直接在父POM中引入，所有子Module都能使用

父POM文件导入相关依赖，不需要写注解@EnableEurekaClient

注意：客户端需要导入spring-boot-starter-web包，不然启动不起来。

 

```
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
```

## 

配置文件application.yml需要添加注册信息：

 

```
eureka:
  client: #客户端注册进eureka服务列表内
    service-url:
      defaultZone: http://admin:123456@eureka7001.com:7001/eureka/,http://admin:123456@eureka7002.com:7002/eureka/
```

**actuator与注册微服务信息完善**：

后面**actuator**还可以同SpringBoot Admin结合使用，对微服务进行监控和管理，比如健康检查、审计、统计和HTTP追踪等

考虑每个微服务都需要actuator监控信息，所以直接在父POM文件中引入相关依赖

 

```
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
```

修改application.yml文件，这里贴的是student服务的配置信息，其他类似：

 

```
eureka:
  client: #客户端注册进eureka服务列表内
    service-url:
      defaultZone: http://admin:123456@eureka7001.com:7001/eureka/,http://admin:123456@eureka7002.com:7002/eureka/
  instance:
    instance-id: student8002 #自定义服务名称信息
    prefer-ip-address: true #访问路径可以显示IP地址
info: #点击链接会跳转显示/actuator/info信息
  app.name: springcloud-client
  company.name: www.wangliu.com
  build.artifactId: '@project.artifactId@'
  build.version: '@project.version@'
```

![img](/a6a8e6ec-bf20-4a66-9d7b-4fbd37fb1d26/128/index_files/7941b082-0820-4f5f-b022-bacc3dd375ab.png)

![img](/a6a8e6ec-bf20-4a66-9d7b-4fbd37fb1d26/128/index_files/59b2f537-35a3-4cda-a5ac-c7a6c66ce1c7.png)

# Fegin

Feign是一个声明式WebService客户端。使用Feign能让编写Web Service客户端更加简单, 它的使用方法是定义一个接口，然后在上面添加注解，同时也支持JAX-RS标准的注解。Feign也支持可拔插式的编码器和解码器。Spring Cloud对Feign进行了封装，使其支持了Spring MVC标准注解和HttpMessageConverters。Feign可以与Eureka和Ribbon组合使用以支持负载均衡。

Feign能干什么：Feign旨在使编写Java Http客户端变得更容易。

Feign集成了Ribbon：通过feign只需要定义服务绑定接口且以声明式的方法，优雅而简单的实现了服务调用。

在springcloud-client业务相关的客户端中使用，考虑到业务客户端各微服务之间进行通信，故在父POM中添加相关依赖，使用时只需在启动类中添加相关注解即可，插拔即用的方式。

父POM文件导入依赖：

 

```
    <!--Feign相关-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-openfeign</artifactId>
        </dependency>
```

调用方开启注解

 

```
@EnableFeignClients
```

![img](/a6a8e6ec-bf20-4a66-9d7b-4fbd37fb1d26/128/index_files/ec955d82-3f57-4355-9e70-8e99bdf72ed4.png)

比如student学生微服务中需要调用course课程微服务来添加课程，只需要在student微服务的启动类上添加上述注解，即可开启Fegin通信。

**要使用hystrix服务熔断和降级功能，请在调用方的配置文件配置如下：**

 

```
feign:
  hystrix:
    enabled: true
```

例如：student学生微服务中在添加学生的时候需要调用course课程微服务来添加课程

新建CourseFegin接口，引入注解@FeignClient，只需指明微服务名称以及熔断处理回调方法即可

 

```
/**
 * @FeignClient注解中name指明微服务名称，fallback指明熔断回调机制
 *
 * Created by 王柳
 * Date 2019/10/2 16:04
 * version:1.0
 */
@FeignClient(name = "course", fallback = CourseFeginFallback.class)
public interface CourseFegin {
    /**
     * @description  指明controller层的url地址和请求方式
     * @author 王柳
     * @date 2019/11/8 15:22
     * @params [param]
     */
    @RequestMapping(value = "/course/saveCourse", method = RequestMethod.POST)
    Map saveCourse(Map param);
}
```

新建CourseFeginFallback熔断处理回调类，注意：需要添加@Component注解。

 

```
/**
 * Created by 王柳
 * Date 2019/10/2 16:05
 * version:1.0
 */
@Component
@Slf4j
public class CourseFeginFallback implements CourseFegin {
    @Override
    public Map saveCourse(Map param) {
        log.info("-------------saveCourse发生了熔断!");
        return null;
    }
}
```

student中注入CourseFegin，就可以在student微服务中调用course微服务进行数据的增删改查

 

```
@Service
@Slf4j
public class StudentServcieImpl extends ServiceImpl<StudentMapper, Student> implements IStudentService {
    @Autowired
    private CourseFegin courseFegin;
    
    ...
}
```

# Zuul

Zuul包含了对请求的路由和过滤两个最主要的功能：

其中路由功能负责将外部请求转发到具体的微服务实例上，是实现外部访问统一入口的基础。而过滤器功能则负责对请求的处理过程进行干预，是实现请求校验、服务聚合等功能的基础。Zuul和Eureka进行整合，将Zuul自身注册为Eureka服务治理下的应用，同时从Eureka中获得其他微服务的消息，也即以后的访问微服务都是通过Zuul跳转后获得。

注意：Zuul服务最终还是会注册进Eureka。

 **提供=代理+路由+过滤三大功能**

导入相关依赖

 

```
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-zuul</artifactId>
        </dependency>
```

开启注解

 

```
@EnableZuulProxy
```

![img](/a6a8e6ec-bf20-4a66-9d7b-4fbd37fb1d26/128/index_files/11df6556-5dfe-4c1d-ac86-b1c3a8147492.png)

修改application.yml配置文件，zuul也需要注册进注册中心，所以也需要添加Eureka客户端相关配置

 

```
server:
  port: 9527
spring:
  application:
    name: gateway
#prefix表示访问前缀，ignored-services忽略所有请求，不包括zuul.routes指定的路径,
#zuul:
#  prefix: /abc
#  ignored-services: "*"
# routes to serviceId 这里边是通过serviceId来绑定地址，当在路径后添加/api-a/   则是访问service-A对应的服务。
# ** 表示多层级，*表示单层级
zuul:
  routes:
    defalutApi:
      path: /api/**
      serviceId: student
    apiB:
      path: /admin/**
      serviceId: course
    oa:
      path: /oa/**
      serviceId: oa
    member:
      path: /member/**
      serviceId: member
      sensitiveHeaders: "*"
    auth:
      path: /auth/**
      serviceId: oauth2
      sensitiveHeaders: "*"
  retryable: false
  ignored-services: "*"
  ribbon:
    eager-load:
      enabled: true
  host:
    connect-timeout-millis: 3000
    socket-timeout-millis: 3000
  add-proxy-headers: true
# Zuul超时
# 对于通过serviceId路由的服务,需要设置两个参数：ribbon.ReadTimeout 和ribbon.SocketTimeout
# 对于通过url路由的服务,需要设置两个参数：zuul.host.socket-timeout-millis或zuul.host.connect-timeout-millis
ribbon:
      ReadTimeout: 3000  # 单位毫秒数
  SocketTimeout: 3000
  MaxAutoRetries: 1
  MaxAutoRetriesNextServer: 2
  eureka:
    enabled: true
hystrix:
  command:
    default:
      execution:
        timeout:
          enabled: true
        isolation:
          thread:
            timeoutInMilliseconds: 24000
eureka:
  client:
    service-url:
      defaultZone: http://admin:123456@eureka7001.com:7001/eureka,http://admin:123456@eureka7002.com:7002/eureka
  instance:
      instance-id: gateway9527 #自定义服务名称信息
      prefer-ip-address: true #访问路径可以显示IP地址
info:
  app.name: springcloud-server
  company.name: www.wangliu.com
  build.artifactId: '@project.artifactId@'
  build.version: '@project.version@'
```

解决客户端调用跨域问题，可以在代码中添加相关配置信息

 

```
/**
 * @Description 解决客户端调用跨域问题
 * @Author 王柳
 * @Date 2019/10/11 8:54
 */
@Component
@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setMaxAge(18000L);
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(source);
    }
}
```

Zuul还有过滤器的功能。可以自定义路由过滤器，添加自定义过滤规则

 

```
/**
 * @Description 自定义路由过滤器
 * @Author 王柳
 * @Date 2019/10/11 8:55
 */
@Slf4j
@Component
public class WebFilter extends ZuulFilter {
    /**
     * 该函数需要返回一个字符串来代表过滤器的类型，而这个类型就是在http请求过程中定义的各个阶段
     * pre: 可以在请求被路由之前被调用
     * routing: 在路由请求时被调用
     * post: 在routing和error 过滤器之后被调用
     * error: 处理请求时发生错误时被调用
     *
     * @return
     */
    @Override
    public String filterType() {
        return "pre";
    }
    /**
     * 通过int的值来定义过滤器的执行顺序，数值越小优先级越高
     *
     * @return
     */
    @Override
    public int filterOrder() {
        return 0;
    }
    /**
     * 返回一个boolean值来判断该过滤器是否要执行，我们可以通过此方法指定过滤器的有效范围。
     *
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return false;
    }
    /**
     * 过滤器的具体逻辑，在该函数中，我们可以实现自定义的过滤器逻辑，来确定是否要拦截当前请求，不对其进行后续的路由，
     * 或是在请求路由返回结果之后对处理的结果进行一些加工等
     *
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        //todo...自定义过滤规则
        return null;
    }
}
```

# TX-LCN分布式事务

## 1、服务端TM

导入依赖

 

```
    <properties>
        <codingapi.txlcn.version>5.0.2.RELEASE</codingapi.txlcn.version>
    </properties>
```

 

```
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>com.codingapi.txlcn</groupId>
            <artifactId>txlcn-tc</artifactId>
            <version>${codingapi.txlcn.version}</version>
        </dependency>
        <dependency>
            <groupId>com.codingapi.txlcn</groupId>
            <artifactId>txlcn-tm</artifactId>
            <version>${codingapi.txlcn.version}</version>
        </dependency>
        <dependency>
            <groupId>com.codingapi.txlcn</groupId>
            <artifactId>txlcn-txmsg-netty</artifactId>
            <version>${codingapi.txlcn.version}</version>
        </dependency>
```

开启注解

 

```
@EnableTransactionManagerServer
```

  ![img](/a6a8e6ec-bf20-4a66-9d7b-4fbd37fb1d26/128/index_files/2b57b68c-68c2-47a5-9916-486e978e0ec9.png)

  

配置文件只能使用application.propreties格式的文件，不能使用application.yml  

需要redis环境

需要创建MySQL数据库, 名称为: tx-manager

创建数据表：

 

```
CREATE TABLE `t_tx_exception`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `group_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `unit_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `mod_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `transaction_state` tinyint(4) NULL DEFAULT NULL,
  `registrar` tinyint(4) NULL DEFAULT NULL,
  `remark` varchar(4096) NULL DEFAULT  NULL,
  `ex_state` tinyint(4) NULL DEFAULT NULL COMMENT '0 未解决 1已解决',
  `create_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;
```

修改配置文件application.yml

 

```
spring.application.name=TransactionManager
server.port=7970
# JDBC 数据库配置
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/tx-manager?characterEncoding=UTF-8&serverTimezone=GMT%2B8
spring.datasource.username=root
spring.datasource.password=123456
#开启驼峰功能
mybatis-plus.configuration.map-underscore-to-camel-case=true
#允许JDBC 生成主键
mybatis-plus.configuration.use-generated-keys=true
# 数据库方言
spring.jpa.database-platform=org.hibernate.dialect.MySQL5InnoDBDialect
# 第一次运行可以设置为: create, 为TM创建持久化数据库表
spring.jpa.hibernate.ddl-auto=update
# TM监听IP. 默认为 127.0.0.1
tx-lcn.manager.host=127.0.0.1
# TM监听Socket端口. 默认为 ${server.port} - 100
tx-lcn.manager.port=8070
# 心跳检测时间(ms). 默认为 300000
tx-lcn.manager.heart-time=300000
# 分布式事务执行总时间(ms). 默认为36000
tx-lcn.manager.dtx-time=8000
# 参数延迟删除时间单位ms  默认为dtx-time值
tx-lcn.message.netty.attr-delay-time=${tx-lcn.manager.dtx-time}
# 事务处理并发等级. 默认为机器逻辑核心数5倍
tx-lcn.manager.concurrent-level=160
# TM后台登陆密码，默认值为codingapi
tx-lcn.manager.admin-key=codingapi
# 分布式事务锁超时时间 默认为-1，当-1时会用tx-lcn.manager.dtx-time的时间
tx-lcn.manager.dtx-lock-time=${tx-lcn.manager.dtx-time}
# 雪花算法的sequence位长度，默认为12位.
tx-lcn.manager.seq-len=12
# 异常回调开关。开启时请制定ex-url
tx-lcn.manager.ex-url-enabled=false
# 事务异常通知（任何http协议地址。未指定协议时，为TM提供内置功能接口）。默认是邮件通知
tx-lcn.manager.ex-url=/provider/email-to/wangliu.ah@qq.com
# 开启日志,默认为false
tx-lcn.logger.enabled=true
#tx-lcn.logger.enabled=false
tx-lcn.logger.driver-class-name=${spring.datasource.driver-class-name}
tx-lcn.logger.jdbc-url=${spring.datasource.url}
tx-lcn.logger.username=${spring.datasource.username}
tx-lcn.logger.password=${spring.datasource.password}
# redis 的设置信息. 线上请用Redis Cluster
spring.redis.host=47.99.241.160
spring.redis.port=6379
spring.redis.password=ahhs2019
spring.redis.database=13
#若用tx-lcn.manager.ex-url=/provider/email-to/xxx@xx.xxx 这个配置，配置管理员邮箱信息(如QQ邮箱)，需要导入邮件服务依赖spring-boot-starter-mail
spring.mail.host=smtp.qq.com
spring.mail.port=587
spring.mail.username=wangliu.ah@qq.com
spring.mail.password=ydiekcpezegmjicc
#作为Eureka客户端注册进服务中心
eureka.client.serviceUrl.defaultZone=http://admin:123456@eureka7001.com:7001/eureka,http://admin:123456@eureka7002.com:7002/eureka
eureka.instance.instance-id=TransactionManager7970
eureka.instance.prefer-ip-address=true
info.app.name=springcloud-server
info.company.name=www.wangliu.com
info.build.artifactId=${project.artifactId}
info.build.version=${project.version}
```

TM后台访问地址：http://项目实际地址:端口号

![img](/a6a8e6ec-bf20-4a66-9d7b-4fbd37fb1d26/128/index_files/91a2de05-6962-4ac8-898d-3b5140accdfb.png)

## **2、客户端TC**

考虑到客户端需要互相调用，都需要分布式事务，故在父POM文件中导入相关依赖

父POM文件导入依赖

 

```
    <properties>
        <codingapi.txlcn.version>5.0.2.RELEASE</codingapi.txlcn.version>
    </properties>
```

 

```
        <dependency>
            <groupId>com.codingapi.txlcn</groupId>
            <artifactId>txlcn-tc</artifactId>
            <version>${codingapi.txlcn.version}</version>
        </dependency>
        <dependency>
            <groupId>com.codingapi.txlcn</groupId>
            <artifactId>txlcn-txmsg-netty</artifactId>
            <version>${codingapi.txlcn.version}</version>
        </dependency>
```

修改配置文件application.yml

 

```
#设置日志等级为debug等级，这样方便追踪排查问题
logging:
  level:
    com:
      codingapi: debug
tx-lcn:
  client:
    manager-address: 127.0.0.1:8070
```

**或者**

 

```
#设置日志等级为debug等级，这样方便追踪排查问题
logging:
  level:
    com:
      codingapi: debug
tm:
  manager:
    url: http://127.0.0.1:8070/tx/manager/
```

调用方和被调用方都要开启注解

 

```
@EnableDistributedTransaction
```

![img](/a6a8e6ec-bf20-4a66-9d7b-4fbd37fb1d26/128/index_files/863f7aad-e2c1-43c0-9e77-fea74c91e9bf.png)

**无论是调用方，还是被调用方，都需要设置开启分布式事务，如果有任何一方不开启，则分布式事务不生效**

被调用方服务熔断（fegin），应手动给调用方抛出异常，以便回滚分布式事务。（只有调用方抛出异常才能回滚）

![img](/a6a8e6ec-bf20-4a66-9d7b-4fbd37fb1d26/128/index_files/4518d0a5-13d4-4890-b9b5-5c2376225122.png)

通过注解**@LcnTransaction**在方法上开启分布式事务，通过注解**@Transactional**开启本地事务，例如：

调用方：

 

```
/**
 * Created by 王柳
 * Date 2019/10/2 15:49
 * version:1.0
 */
@Service
@Slf4j
public class StudentServcieImpl extends ServiceImpl<StudentMapper, Student> implements IStudentService {
    @Autowired
    private CourseFegin courseFegin;
    @Resource
    private StudentMapper studentMapper;
    @LcnTransaction
    @Transactional
    @Override
    public Map saveStudent(Map param) throws Exception {
        Map map = new HashMap();
        map.put("code", "-1");
        Map course = (Map) param.get("course");
        Map student = (Map) param.get("student");
        Student student1 = new Student();
        student1.setUserName((String) student.get("name"));
        student1.setAge(Integer.valueOf((String) student.get("age")));
        studentMapper.insert(student1);
        log.info("学生服务----->>saveStudent");
        Map courseMap = courseFegin.saveCourse(course);
        log.info("课程服务----->>saveCourse------>> courseMap: " + courseMap);
        if (courseMap == null){
            throw new Exception("课程服务发生熔断，调用课程服务失败，开始回退课程服务--->>选课");
        }
        Boolean exceFlag = (Boolean) student.get("exceFlag");
        if (exceFlag) {
            throw new RuntimeException();
        }
        map.put("code", "1");
        return map;
    }
}
```

被调用方：

 

```
/**
 * Created by 王柳
 * Date 2019/10/2 15:38
 * version:1.0
 */
@Service
@Slf4j
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements ICourseService {
    @Resource
    private CourseMapper courseMapper;
    @LcnTransaction
    @Transactional
    @Override
    public Map saveCourse(Map param) {
        Map map = new HashMap();
        map.put("code", "-1");
        Boolean exceFlag = (Boolean) param.get("exceFlag");
        if (exceFlag) {
            throw new RuntimeException();
        } else {
            Course course = new Course();
            course.setName((String) param.get("name"));
            courseMapper.insert(course);
            map.put("code", "1");
        }
        return map;
    }
}
```

# SpringCloud Config

分布式系统面临的---配置问题：微服务意味着要将单体应用中的业务拆分成一个个子服务，每个服务的粒度相对较小，因此系统中会出现大量的服务。由于每个服务都需要必要的配置信息才能运行，所以一套集中式的、动态的配置管理设施是必可不少的。SpringCloud提供了COnfigServer来解决这个问题，我们每一个微服务自己带着一个application.yml，上百个配置文件的管理。

是什么：

![img](/2c2154f6-4be6-4bb8-9275-0c4fe7e11497/128/index_files/61a2d716-a054-41f8-ad92-949ee1c1e59e.jpg)

SpringCloud Config为微服务架构中的微服务提供集中化的外部配置支持，配置服务器为各个不同微服务应用的所有环境提供了一个中心化的外部配置。

怎么玩：

SpringCloud Config分为服务端和客户端两部分。

服务端也称为分布式配置中心，它是一个独立的微服务应用，用来连接配置服务器并为客户端提供获取配置信息，加密/解密信息等访问接口。

客户端则是通过指定的配置中心来管理应用资源，以及与业务相关的配置内容，并在启动的时候从配置中心获取和加载配置信息，配置服务器默认采用git来存储配置信息，这样就有助于对环境配置进行版本管理，并且可以通过git客户端工具来方便的管理和访问配置内容。

能干吗：

- 集中管理配置文件；
- 不同环境不同配置，动态化的配置更新，分环境部署比如dev/test/prod/beta/release；
- 运行期间动态调整配置，不再需要在每个服务部署的机器上编写配置文件，服务会向配置中心统一拉取配置自己的信息；
- 当配置发生变动时，服务不需要重启即可感知到配置的变化并应用新的配置；
- 将配置信息以REST接口的形式暴露。

可参考：https://github.com/wangliu1102/SpringCloudStudy-atguigu 中的：SpringCloud Config分布式配置中心。



 配置到GitHub，配置文件放在Git仓库存储，可参考下面章节中的**SpringCloud Bus。**如下是将配置放在微服务中统一管理，配置文件放在本地存储，类似于公共类放在公共包里一样。 



## 1、服务端

导入相关依赖

 

```
        <!--配置中心-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-config-server</artifactId>
        </dependency>
```

开启注解

```
@EnableConfigServer
```

![img](/a6a8e6ec-bf20-4a66-9d7b-4fbd37fb1d26/128/index_files/e92b66c1-dcd7-4c07-9f88-f52ad528dca6.png)

新建bootstrap.yml

  application.yml是用户级的资源配置项。

  bootstrap.yml是系统级的，优先级更高。

Spring Cloud会创建一个Bootstrap Context，作为Spring应用的Application Context的父上下文。初始化的时候，Bootstrap Context负责从外部源加载配置属性并解析配置。这两个上下文共享一个从外部获取的Environment。Bootstrap属性有高优先级，默认情况下，它们不会被本地配置覆盖。Bootstrap Context和Application Context有着不同的约定，所以新增一个bootstrap.yml文件，保证Bootstrap Context和Application Context配置的分离。

这里我们不使用GitHub来管理配置文件，而是通过config微服务来管理配置文件，如下会把其他微服务的配置文件配置在resources->config文件夹下：

 

```
server:
  port: 3344
spring:
  application:
    name: cloud-config
  profiles:
    active: native
  # 配置中心
  cloud:
    config:
      server:
        native:
          search-locations: classpath:/config/
eureka:
  client:
    service-url:
      defaultZone: http://admin:123456@eureka7001.com:7001/eureka,http://admin:123456@eureka7002.com:7002/eureka
  instance:
    instance-id: cloud-config3344 #自定义服务名称信息
    prefer-ip-address: true #访问路径可以显示IP地址
info:
  app.name: springcloud-server
  company.name: www.wangliu.com
  build.artifactId: '@project.artifactId@'
  build.version: '@project.version@'
# 暴露监控端点
management:
  endpoints:
    web:
      exposure:
        include: '*'
  endpoint: #health endpoint是否必须显示全部细节。默认情况下, /actuator/health 是公开的，并且不显示细节
    health:
      show-details: always
```

## 2、客户端

考虑到很多微服务的配置文件都需要统计被集中管理在config微服务中，在父POM文件下配置客户端依赖，其他微服务就可以使用客户端配置了。

在父POM文件下导入如下依赖：

 

```
        <!--配置中心客户端-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-config</artifactId>
        </dependency>
```

新建bootstrap.yml，添加配置中心相关配置。

 

```
spring:
  # 配置中心
  cloud:
    config:
      fail-fast: true  #是否启动快速失败功能，功能开启则优先判断config server是否正常
      name: ${spring.application.name}
      profile: ${spring.profiles.active}
      discovery: #配置服务发现
        enabled: true #是否启动服务发现
        service-id: cloud-config #服务发现(eureka)中，配置中心(config server)的服务名
  profiles:
    active: dev # 影响在config微服务下的resources->config文件夹下新建的配置文件名称
  main:
    allow-bean-definition-overriding: true #允许重名的bean可以被覆盖
```

在config微服务下的resources->config文件夹下，添加：服务名-dev.yml文件。然后再其中添加该客户端微服务的其他相关配置。

注意：如果不想使用Config配置中心管理配置文件，需要在客户端新建bootstrap.yml文件，添加如下配置：

 

```
spring:
  cloud:
    config:
      enabled: false #禁用config配置，否则报Could not locate PropertySource: I/O error on GET request for "http://localhost:8888
```

例如，我在zuul微服务和oauth2微服务中添加了配置中心相关配置，使用config来管理配置文件。而eureka-server服务注册中心中不使用config来管理配置文件。

![img](/a6a8e6ec-bf20-4a66-9d7b-4fbd37fb1d26/128/index_files/c3144c81-5771-4902-aee1-226363cad764.png)





# SpringCloud Bus + RabbitMQ/Kafka

SpringCloud Bus 结合SpringCloud config可以动态更新配置。

**步骤如下**:

​    首先，在配置中进行更新配置文件信息，它就会自动触发post发送bus/refresh；

​    然后服务端就会将更新的配置并且发送给Spring Cloud Bus；

​    继而Spring Cloud bus接到消息之后并通知给使用该配置的客户端；

​    最后使用该配置的客户端收到通知后，就会获取最新的配置进行更新；

## **1、开发准备**

​    RabbitMQ 的安装教程：[RabbitMQ的环境安装及配置(Windows)](./RabbitMQ的环境安装及配置(Windows).md)。

​    Kafka 的安装教程：[Kafka安装使用教程(Linux)](./Kafka安装使用教程(Linux).md)。

Spring Cloud Bus 主要的使用的MQ的是RabbitMQ和Kafka。至于使用的话就可以根据情况来进行选择，主要使用的是哪个MQ就用哪一个就行了。这里我们就用RabbitMQ作为示例来进行讲解，Kafka的使用也差不多，也无在乎配置更改而已。

## 2、服务端

新建config-bus微服务，基本配置和config一样。

引入SpringCloud Bus依赖

 

```
     <!--actuator监控信息完善-->
     <!--<dependency>
         <groupId>org.springframework.boot</groupId>
         <artifactId>spring-boot-starter-actuator</artifactId>
     </dependency>-->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-bus-amqp</artifactId>
    </dependency>
```

注: spring-boot-starter-actuator是必须的，不然是无法在服务端进行配置刷新请求的。如果是使用的kafka的话，只需将spring-cloud-starter-bus-amqp改成spring-cloud-starter-bus-kafka即可。

这里我们把spring-boot-starter-actuator放在了父POM文件中。

修改bootstrap.yml配置文件，添加bus和RabbitMQ相关配置，并暴露监控端点（不然是无法在服务端进行配置刷新请求的）。

如果是kafka的话，不用添加RabbitMQ相关配置，添加kafka的配置信息spring.kafka.bootstrap-servers，填写kafka的地址和端口即可。

 

```
spring:
  kafka:
    bootstrap-servers: 192.168.1.172:9092   #配置 kafka 服务器的地址和端口
```

**这里必须使用Git仓库来管理配置文件**：

Git仓库地址：https://github.com/wangliu1102/microservicecloud-config

 

```
server:
  port: 3355
spring:
  application:
    name: cloud-config-bus
#  profiles:
#    active: native # 读取本地文件，开启就不会从Git上拉取配置文件
  # 配置中心
  cloud:
    config:
      server:
        git:
          uri: https://github.com/wangliu1102/microservicecloud-config
          search-paths: /springcloud_bus #git仓库地址下的相对地址 多个用逗号","分割
          force-pull: true #强制拉入Git存储库
          # 访问git仓库的用户密码 如果Git仓库为公开仓库，可以不填写用户名和密码，如果是私有仓库需要填写
#          username: ******
#          password: ******
    bus:
      enabled: true #是否启用springcloud config bus
      trace:
        enabled: true # 开启跟踪总线事件
  #rabbitmq配置
  rabbitmq:
    host: 127.0.0.1
    port: 5672
    username: guest
    password: guest
eureka:
  client:
    service-url:
      defaultZone: http://admin:123456@eureka7001.com:7001/eureka,http://admin:123456@eureka7002.com:7002/eureka
  instance:
    instance-id: cloud-config-bus3355 #自定义服务名称信息
    prefer-ip-address: true #访问路径可以显示IP地址
info:
  app.name: springcloud-server
  company.name: www.wangliu.com
  build.artifactId: '@project.artifactId@'
  build.version: '@project.version@'
# 暴露监控端点
management:
  endpoints:
    web:
      exposure:
        include: '*'
  endpoint: #health endpoint是否必须显示全部细节。默认情况下, /actuator/health 是公开的，并且不显示细节
    health:
      show-details: always
```

![img](/a6a8e6ec-bf20-4a66-9d7b-4fbd37fb1d26/128/index_files/06089180-d751-42ef-9127-a89a32f5ff8e.png)

## 3、客户端

这里使用了student微服务来操作。考虑客户端需要使用SpringCloud Config来管理配置文件，我们在父POM文件中引入依赖

 

```
        <!--配置中心客户端-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-config</artifactId>
        </dependency>
        <!--SpringCloud Bus + RabbitMQ-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-bus-amqp</artifactId>
        </dependency>
```

修改bootstrap.yml，如下：

 

```
server:
  port: 8002
spring:
  application:
    name: student
  # 配置中心
  cloud:
    config:
      fail-fast: true  #是否启动快速失败功能，功能开启则优先判断config server是否正常
      name: ${spring.application.name}
      profile: ${spring.profiles.active}
      discovery: #配置服务发现
        enabled: true #是否启动服务发现
        #        service-id: cloud-config #服务发现(eureka)中，配置中心(config server)的服务名
        service-id: cloud-config-bus #服务发现(eureka)中，配置中心(config server)的服务名
      label: master #获取配置文件的分支，默认是master。如果是是本地获取的话，则无用
  profiles:
    active: dev
  main:
    allow-bean-definition-overriding: true
eureka:
  client: #客户端注册进eureka服务列表内
    service-url:
      defaultZone: http://admin:123456@eureka7001.com:7001/eureka/,http://admin:123456@eureka7002.com:7002/eureka/
  instance:
    instance-id: student8002 #自定义服务名称信息
    prefer-ip-address: true #访问路径可以显示IP地址
info:
  app.name: springcloud-client
  company.name: www.wangliu.com
  build.artifactId: '@project.artifactId@'
  build.version: '@project.version@'
```

修改student-dev.yml配置文件，添加bus和RabbitMQ相关配置，并 暴露监控端点。该配置文件时通过config-bus，来保存在GitHub仓库中的。

 

```
spring:
  cloud:
    bus:
      trace:
        enabled: true # 开启跟踪总线事件
  #rabbitmq配置
  rabbitmq:
    host: 127.0.0.1
    port: 5672
    username: guest
    password: guest
  datasource:
    # 数据库驱动
    driver-class-name: com.mysql.cj.jdbc.Driver
    # druid连接池
    type: com.alibaba.druid.pool.DruidDataSource
    url: jdbc:mysql://localhost:3306/clouddb01?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT%2B8
    username: root
    password: 123456
    # 数据源其他配置
    druid:
      # 初始化时建立物理连接的个数。初始化发生在显示调用init方法，或者第一次getConnection时
      initial-size: 5
      # 最小连接池数量
      min-idle: 5
      # 最大连接池数量
      max-active: 20
      # 获取连接时最大等待时间，单位毫秒。配置了maxWait之后，缺省启用公平锁，并发效率会有所下降，如果需要可以通过配置useUnfairLock属性为true使用非公平锁。
      max-wait: 60000
      # 有两个含义：
      # 1) Destroy线程会检测连接的间隔时间
      # 2) testWhileIdle的判断依据，详细看testWhileIdle属性的说明
      time-between-eviction-runs-millis: 60000
      #Destory线程中如果检测到当前连接的最后活跃时间和当前时间的差值大于minEvictableIdleTimeMillis，则关闭当前连接。
      min-evictable-idle-time-millis: 300000
      # 用来检测连接是否有效的sql，要求是一个查询语句。如果validationQuery为null，testOnBorrow、testOnReturn、testWhileIdle都不会其作用。在mysql中通常为select 'x'，在oracle中通常为select 1 from dual
      validation-query: SELECT 'x'
      # 建议配置为true，不影响性能，并且保证安全性。申请连接的时候检测，如果空闲时间大于timeBetweenEvictionRunsMillis，执行validationQuery检测连接是否有效。
      test-while-idle: true
      # 申请连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能。
      test-on-borrow: false
      # 归还连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能
      test-on-return: false
      # 是否缓存preparedStatement，也就是PSCache。PSCache对支持游标的数据库性能提升巨大，比如说oracle。在mysql5.5以下的版本中没有PSCache功能，建议关闭掉。5.5及以上版本有PSCache，建议开启。
      pool-prepared-statements: true
      # 指定每个连接上PSCache的大小
      max-pool-prepared-statement-per-connection-size: 20
      # 配置监控统计拦截的filters，去掉后监控界面sql无法统计，'wall'用于防火墙
      # 属性类型是字符串，通过别名的方式配置扩展插件，常用的插件有：监控统计用的filter:stat、日志用的filter:log4j、防御sql注入的filter:wall
      filters: stat,wall
      # 合并多个DruidDataSource的监控数据
      use-global-data-source-stat: true
      # 通过connectProperties属性来打开mergeSql功能；慢SQL记录
      connect-properties: druid.stat.mergeSql=true;druid.stat.slowSqlMillis=500
mybatis-plus:
  mapper-locations: classpath*:com/wl/springcloud/**/mapping/*.xml
  global-config:
    banner: false # 是否控制台 print mybatis-plus 的 LOGO
    db-config:
      # 主键类型 auto:数据库ID自增 input:用户输入ID id_worker:全局唯一ID(数字类型唯一ID) uuid:全局唯一ID(UUID) id_worker_str:全局唯一ID(字符串类型唯一ID) none:
      id-type: auto
      #字段策略 default:默认,ignored:"忽略判断",not_null:"非 NULL 判断",not_empty:"非空判断"
      field-strategy: not_null
      # 默认数据库表下划线命名
      table-underline: true
      # 逻辑删除配置
      # 逻辑删除全局值（1表示已删除，这也是Mybatis Plus的默认配置）
      logic-delete-value: 1
      # 逻辑未删除全局值（0表示未删除，这也是Mybatis Plus的默认配置）
      logic-not-delete-value: 0
      db-type: mysql
  configuration:
    map-underscore-to-camel-case: true # 开启驼峰命名转换
feign:
  hystrix:
    enabled: true
#tx-lcn:
#  # 是否启动LCN负载均衡策略(优化选项，开启与否，功能不受影响)
#  ribbon:
#    loadbalancer:
#      dtx:
#        enabled: true
#  client:
#    # tx-manager 的配置地址，可以指定TM集群中的任何一个或多个地址
#    # tx-manager 下集群策略，每个TC都会从始至终<断线重连>与TM集群保持集群大小个连接。
#    # TM方，每有TM进入集群，会找到所有TC并通知其与新TM建立连接。
#    # TC方，启动时按配置与集群建立连接，成功后，会再与集群协商，查询集群大小并保持与所有TM的连接
#    manager-address: 127.0.0.1:8070
#    # 调用链长度等级，默认值为3（优化选项。系统中每个请求大致调用链平均长度，估算值。）
#    chain-level: 3
#    # 该参数为tc与tm通讯时的最大超时时间，单位ms。该参数不需要配置会在连接初始化时由tm返回。
#    tm-rpc-timeout: 2000
#    # 该参数为分布式事务的最大时间，单位ms。该参数不允许TC方配置，会在连接初始化时由tm返回。
#    dtx-time: 8000
#    # 该参数为雪花算法的机器编号，所有TC不能相同。该参数不允许配置，会在连接初始化时由tm返回。
#    machine-id: 1
#    # 该参数为事务方法注解切面的orderNumber，默认值为0.
#    dtx-aspect-order: 0
#    # 该参数为事务连接资源方法切面的orderNumber，默认值为0.
#    resource-order: 0
#  # 该参数是分布式事务框架存储的业务切面信息。采用的是h2数据库。绝对路径。该参数默认的值为{user.dir}/.txlcn/{application.name}-{application.port}
#  aspect:
#    log:
#      file-path: logs/.txlcn/demo-8080
#  # 是否开启日志记录。当开启以后需要配置对应logger的数据库连接配置信息。
#  logger:
#    enabled: false
#    driver-class-name: ${spring.datasource.driver-class-name}
#    jdbc-url: ${spring.datasource.url}
#    username: ${spring.datasource.username}
#    password: ${spring.datasource.password}
#tm:
#  manager:
#    url: http://127.0.0.1:8070/tx/manager/
#设置日志等级为debug等级，这样方便追踪排查问题
logging:
  level:
    com:
      codingapi: debug
tx-lcn:
  client:
    manager-address: 127.0.0.1:8070
# 暴露监控端点
management:
  endpoints:
    web:
      exposure:
        include: '*'
  endpoint:
    health:
      show-details: always
word: wangliu # 用来测试SpringCloud Bus拉取更新配置的字段
```

在student微服务中添加注解@RefreshScope，通过@Value来获取配置文件中word的内容：

 

```
/**
 * Created by 王柳
 * Date 2019/10/2 15:49
 * version:1.0
 */
@Api(tags = "学生服务")
@Controller
@RequestMapping("/student")
@Slf4j
@RefreshScope
public class StudentController {
    @Value("${word}")
    private String word;
    @Autowired
    private IStudentService studentService;
    @ApiOperation(value = "保存学生和选课")
    @RequestMapping(value = "/saveStudentAndCourse", method = RequestMethod.POST)
    @ResponseBody
    public Object saveStudentAndCourse(@RequestBody Map<String, Object> requestMap) {
        log.info("进入学生服务----->>添加学生");
        Map map = new HashMap();
        try {
            map = studentService.saveStudent(requestMap);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code", "-1");
            map.put("msg", "保存失败");
        }
        return map;
    }
    @ApiOperation(value = "查询学生列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Object listCourse() {
        log.info("进入学生服务----->>学生列表");
        log.info("word: " + word);
        return studentService.list();
    }
}
```

分别启动eureka-7001、eureka-7002、config-bus、lcn-tm(student依赖的微服务)、student微服务

使用postman调用学生服务student中的list请求：

 

```
localhost:8002/student/list
```

查看控制台内容：

![img](/a6a8e6ec-bf20-4a66-9d7b-4fbd37fb1d26/128/index_files/8a0641c1-dde7-4e53-8d0f-7e0df916d3d5.png)

修改Git仓库上的student-dev.yml配置文件中的word：

 

```
word: hello
```

使用Postman工具进行发起POST请求，地址是服务端config-bus的地址和端口。

使用POST请求如下地址:

 

```
http://localhost:3355actuator/bus-refresh
```

可以看到控制台有修改：

![img](/a6a8e6ec-bf20-4a66-9d7b-4fbd37fb1d26/128/index_files/c9d5dfc2-93e0-4997-8226-166f51ef0808.png)

然后再次调用学生服务student中的list请求。控制台查看：

![img](/a6a8e6ec-bf20-4a66-9d7b-4fbd37fb1d26/128/index_files/9ea40ed9-1617-477c-b942-248bd44c21c0.png)

# 





# swagger

考虑到客户端都需要swagger，故在父POM文件中导入相关依赖

父POM文件中导入依赖

 

```
        <!--swagger-->
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger2</artifactId>
            <version>${springfox-swagger2.version}</version>
        </dependency>
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger-ui</artifactId>
            <version>${springfox-swagger-ui.version}</version>
        </dependency>
        <!--guava必须指定一个版本，因为swagger和lcn的swagger版本不一致，会导致包冲突-->
        <dependency>
            <groupId>com.google.guava</groupId>
            <artifactId>guava</artifactId>
            <version>20.0</version>
        </dependency>
```

开启注解

 

```
@EnableSwagger2
```

![img](/a6a8e6ec-bf20-4a66-9d7b-4fbd37fb1d26/128/index_files/029d19d2-008b-4e0e-a19c-f04be28f5614.png)

增加配置类：

 

```
/**
 * @author 王柳
 * @date 2019/10/14 8:48
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    /**
     * @description 创建API应用
     * apiInfo() 增加API相关信息
     * 通过select()函数返回一个ApiSelectorBuilder实例,用来控制哪些接口暴露给Swagger来展现，
     * 本例采用指定扫描的包路径来定义指定要建立API的目录。
     * @author 王柳
     * @date 2019/10/14 8:49
     * @params []
     */
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.wl.springcloud"))
                .paths(PathSelectors.any())
                .build();
    }
    /**
     * @description 创建该API的基本信息（这些基本信息会展现在文档页面中）
     * 访问地址：http://项目实际地址/swagger-ui.html
     * @author 王柳
     * @date 2019/10/14 8:52
     * @params []
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Spring Boot中使用Swagger2构建RESTful APIs")
                .version("1.0")
                .build();
    }
}
```

Controller类中使用：

注意：@RequestMapping要标明请求的类型method

 

```
/**
 * Created by 王柳
 * Date 2019/10/2 15:49
 * version:1.0
 */
@Api(tags = "学生服务")
@Controller
@RequestMapping("/student")
@Slf4j
public class StudentController {
    @Autowired
    private IStudentService studentService;
    @ApiOperation(value="保存学生和选课")
    @RequestMapping(value = "/saveStudentAndCourse", method = RequestMethod.POST)
    @ResponseBody
    public Object saveStudentAndCourse(@RequestBody Map<String, Object> requestMap) {
        log.info("进入学生服务----->>添加学生");
        Map map = new HashMap();
        try {
            map = studentService.saveStudent(requestMap);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code", "-1");
            map.put("msg", "保存失败");
        }
        return map;
    }
    @ApiOperation(value="查询学生列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Object listCourse() {
        log.info("进入学生服务----->>学生列表");
        return studentService.list();
    }
}
```

访问地址：http://项目实际地址/swagger-ui.html

![img](/a6a8e6ec-bf20-4a66-9d7b-4fbd37fb1d26/128/index_files/17ff1128-e716-49ae-8571-daf08a0b29df.png)

# Druid连接池

导入相关依赖

 

```
<dependency>
   <groupId>com.alibaba</groupId>
   <artifactId>druid-spring-boot-starter</artifactId>
   <version>1.1.10</version>
</dependency>
```

修改application.yml，部分说明写在注释了：

 

```
spring:
  datasource:
    # 数据库驱动
    driver-class-name: com.mysql.cj.jdbc.Driver
    # druid连接池
    type: com.alibaba.druid.pool.DruidDataSource
    url: jdbc:mysql://localhost:3306/clouddb01?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT%2B8
    username: root
    password: 123456
    # 数据源其他配置 druid-spring-boot-starter省去了@Configuration在代码中配置
    druid:
      # 初始化时建立物理连接的个数。初始化发生在显示调用init方法，或者第一次getConnection时
      initial-size: 5
      # 最小连接池数量
      min-idle: 5
      # 最大连接池数量
      max-active: 20
      # 获取连接时最大等待时间，单位毫秒。配置了maxWait之后，缺省启用公平锁，并发效率会有所下降，如果需要可以通过配置useUnfairLock属性为true使用非公平锁。
      max-wait: 60000
      # 有两个含义：
      # 1) Destroy线程会检测连接的间隔时间
      # 2) testWhileIdle的判断依据，详细看testWhileIdle属性的说明
      time-between-eviction-runs-millis: 60000
      #Destory线程中如果检测到当前连接的最后活跃时间和当前时间的差值大于minEvictableIdleTimeMillis，则关闭当前连接。
      min-evictable-idle-time-millis: 300000
      # 用来检测连接是否有效的sql，要求是一个查询语句。如果validationQuery为null，testOnBorrow、testOnReturn、testWhileIdle都不会其作用。在mysql中通常为select 'x'，在oracle中通常为select 1 from dual
      validation-query: SELECT 'x'
      # 建议配置为true，不影响性能，并且保证安全性。申请连接的时候检测，如果空闲时间大于timeBetweenEvictionRunsMillis，执行validationQuery检测连接是否有效。
      test-while-idle: true
      # 申请连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能。
      test-on-borrow: false
      # 归还连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能
      test-on-return: false
      # 是否缓存preparedStatement，也就是PSCache。PSCache对支持游标的数据库性能提升巨大，比如说oracle。在mysql5.5以下的版本中没有PSCache功能，建议关闭掉。5.5及以上版本有PSCache，建议开启。
      pool-prepared-statements: true
      # 指定每个连接上PSCache的大小
      max-pool-prepared-statement-per-connection-size: 20
      # 配置监控统计拦截的filters，去掉后监控界面sql无法统计，'wall'用于防火墙
      # 属性类型是字符串，通过别名的方式配置扩展插件，常用的插件有：监控统计用的filter:stat、日志用的filter:slf4j、防御sql注入的filter:wall
      filters: stat,wall,slf4j
      # 合并多个DruidDataSource的监控数据
      use-global-data-source-stat: true
      # 通过connectProperties属性来打开mergeSql功能；慢SQL记录
      connect-properties: druid.stat.mergeSql=true;druid.stat.slowSqlMillis=500
      # 配置DruidStatFilter
      web-stat-filter:
        enabled: true
        url-pattern: "/*"
        exclusions: "*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*"
      # 配置DruidStatViewServlet
      stat-view-servlet:
        url-pattern: "/druid/*"
        # IP白名单(没有配置或者为空，则允许所有访问)
        allow: 127.0.0.1,192.168.253.128
        # IP黑名单 (存在共同时，deny优先于allow)
        deny: 192.168.1.73
        #  禁用HTML页面上的“Reset All”功能
        reset-enable: false
        # 登录名
        login-username: admin
        # 登录密码
        login-password: 123456
```

访问：http://项目实际地址:端口号/druid/

登录名/密码：admin/123456 (写在了配置文件中)

![img](/a6a8e6ec-bf20-4a66-9d7b-4fbd37fb1d26/128/index_files/9d161e2e-895f-4816-9db2-30a48fef1ac3.png)

# SpringBoot Admin

导入相关依赖，导入security是为了登录验证

 

```
<properties>
        <spring-boot-admin.version>2.1.6</spring-boot-admin.version>
    </properties>
    <dependencies>
        <!--Tomcat: java.lang.IllegalStateException: Calling [asyncError()] is not valid for a request with Async state [MUST_DISPATCH]-->
        <!-- 使用jetty来替换Tomcat解决上述错误-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
            <exclusions>
                <exclusion>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-tomcat</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-jetty</artifactId>
        </dependency>
        <!--监控服务端-->
        <dependency>
            <groupId>de.codecentric</groupId>
            <artifactId>spring-boot-admin-starter-server</artifactId>
            <version>${spring-boot-admin.version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-security</artifactId>
        </dependency>
        
        <!--邮件服务，都用的话可以放在父POM文件中-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-mail</artifactId>
        </dependency>
    </dependencies>
```

修改配置文件application.yml

 

```
server:
  port: 8769
spring:
  application:
    name: admin-monitor
  #登录SpringBoot Admin后台监控的用户名和密码
  security:
    user:
      name: admin
      password: admin
  #集成邮件通知，当监控的微服务down掉或up起来了，都会发送邮件通知
  mail:
    host: smtp.qq.com
    username: wangliu.ah@qq.com
    password: ydiekcpezegmjicc
  boot:
    admin:
      notify:
        mail:
          to: wangliu.ah@qq.com
          from: wangliu.ah@qq.com
#哪个客户端要监控所有端点，就添加下面代码，否则只监控到很少的信息，例如oauth2服务
management: #暴露actuator的所有端点
  endpoints:
    web:
      exposure:
        include: "*"
  endpoint: #health endpoint是否必须显示全部细节。默认情况下, /actuator/health 是公开的，并且不显示细节
    health:
      show-details: always
eureka:
  client:
    registry-fetch-interval-seconds: 5  #表示eureka client间隔多久去拉取服务注册信息，默认为30秒
    service-url:
      defaultZone: http://admin:123456@eureka7001.com:7001/eureka,http://admin:123456@eureka7002.com:7002/eureka
  instance:
    lease-renewal-interval-in-seconds: 10  #表示eureka client发送心跳给server端的频率
    health-check-url-path: /actuator/health  #健康检查页面的URL路径
    instance-id: admin-monitor8769 #自定义服务名称信息
    prefer-ip-address: true #访问路径可以显示IP地址
      #注册给eureka时告诉eureka自己的密码，否则报错
    metadata-map:
      user.name: ${spring.security.user.name}
      user.password: ${spring.security.user.password}
info:
  app.name: springcloud-server
  company.name: www.wangliu.com
  build.artifactId: '@project.artifactId@'
  build.version: '@project.version@'
```

开启注解

 

```
@EnableAdminServer
```

![img](/a6a8e6ec-bf20-4a66-9d7b-4fbd37fb1d26/128/index_files/5d42b9c5-053b-4f31-9245-b5e99d8e41aa.png)

增加Security相关配置：

 

```
/**
 * @author 王柳
 * @date 2019/11/7 15:22
 */
@Configuration
public class SecuritySecureConfig extends WebSecurityConfigurerAdapter {
    private final String adminContextPath;
    public SecuritySecureConfig(AdminServerProperties adminServerProperties) {
        this.adminContextPath = adminServerProperties.getContextPath();
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setTargetUrlParameter("redirectTo");
        successHandler.setDefaultTargetUrl(adminContextPath + "/monitor");
        http.authorizeRequests()
                .antMatchers(adminContextPath + "/assets/**").permitAll()
                .antMatchers(adminContextPath + "/login").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage(adminContextPath + "/login").successHandler(successHandler).and()
                .logout().logoutUrl(adminContextPath + "/logout").and()
                .httpBasic().and()
                .csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .ignoringAntMatchers(
                        adminContextPath + "/instances",
                        adminContextPath + "/actuator/**"
                );
    }
}
```

**注意：SpringBoot Admin 注册进Eureka后，也可以监控到注册进Eureka的其他微服务**。

访问：http://项目实际地址:端口号

![img](/a6a8e6ec-bf20-4a66-9d7b-4fbd37fb1d26/128/index_files/27a81a73-5e1e-4dc9-85e0-75d41fce3c5d.png)

# OAuth2 

参考：[SpringCloud+SpringBoot+OAuth2+Spring Security+Redis实现的微服务统一认证授权](https://blog.csdn.net/WYA1993/article/details/85050120)

具体源码参考： [https://github.com/wangliu1102/SpringCloudStudy-Practical](https://github.com/wangliu1102/SpringCloudStudy-Practical.git)

## 构建授权服务oauth2

导入依赖

 

```
<properties>
    <druid.version>1.1.10</druid.version>
</properties>   
<dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-oauth2</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-security</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <scope>runtime</scope>
        </dependency>
        <!--druid-->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid-spring-boot-starter</artifactId>
            <version>${druid.version}</version>
        </dependency>
</dependencies>
<build>
        <resources>
            <resource>
                <!-- xml放在java目录下需要该配置，否则报not found错误-->
                <directory>src/main/java</directory>
                <includes>
                    <include>**/*.xml</include>
                </includes>
            </resource>
            <!--指定资源的位置（xml放在resources下，可以不用指定）-->
            <resource>
                <directory>src/main/resources</directory>
                <filtering>true</filtering>
            </resource>
        </resources>
    </build>
```

修改application.yml配置文件

 

```
server:
  port: 8081
spring:
  application:
    name: oauth2
  redis:
    database: 5
    host: 47.99.241.160
    port: 6379
    password: ahhs2019
    jedis:
      pool:
        max-active: 8
        max-idle: 8
        min-idle: 0
  datasource:
    # 数据库驱动
    driver-class-name: com.mysql.cj.jdbc.Driver
    # druid连接池
    type: com.alibaba.druid.pool.DruidDataSource
    url: jdbc:mysql://localhost:3306/clouddb01?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT%2B8
    username: root
    password: 123456
    # 数据源其他配置 druid-spring-boot-starter省去了@Configuration在代码中配置
    druid:
      # 初始化时建立物理连接的个数。初始化发生在显示调用init方法，或者第一次getConnection时
      initial-size: 5
      # 最小连接池数量
      min-idle: 5
      # 最大连接池数量
      max-active: 20
      # 获取连接时最大等待时间，单位毫秒。配置了maxWait之后，缺省启用公平锁，并发效率会有所下降，如果需要可以通过配置useUnfairLock属性为true使用非公平锁。
      max-wait: 60000
      # 有两个含义：
      # 1) Destroy线程会检测连接的间隔时间
      # 2) testWhileIdle的判断依据，详细看testWhileIdle属性的说明
      time-between-eviction-runs-millis: 60000
      #Destory线程中如果检测到当前连接的最后活跃时间和当前时间的差值大于minEvictableIdleTimeMillis，则关闭当前连接。
      min-evictable-idle-time-millis: 300000
      # 用来检测连接是否有效的sql，要求是一个查询语句。如果validationQuery为null，testOnBorrow、testOnReturn、testWhileIdle都不会其作用。在mysql中通常为select 'x'，在oracle中通常为select 1 from dual
      validation-query: SELECT 'x'
      # 建议配置为true，不影响性能，并且保证安全性。申请连接的时候检测，如果空闲时间大于timeBetweenEvictionRunsMillis，执行validationQuery检测连接是否有效。
      test-while-idle: true
      # 申请连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能。
      test-on-borrow: false
      # 归还连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能
      test-on-return: false
      # 是否缓存preparedStatement，也就是PSCache。PSCache对支持游标的数据库性能提升巨大，比如说oracle。在mysql5.5以下的版本中没有PSCache功能，建议关闭掉。5.5及以上版本有PSCache，建议开启。
      pool-prepared-statements: true
      # 指定每个连接上PSCache的大小
      max-pool-prepared-statement-per-connection-size: 20
      # 配置监控统计拦截的filters，去掉后监控界面sql无法统计，'wall'用于防火墙
      # 属性类型是字符串，通过别名的方式配置扩展插件，常用的插件有：监控统计用的filter:stat、日志用的filter:slf4j、防御sql注入的filter:wall
      filters: stat,wall,slf4j
      # 合并多个DruidDataSource的监控数据
      use-global-data-source-stat: true
      # 通过connectProperties属性来打开mergeSql功能；慢SQL记录
      connect-properties: druid.stat.mergeSql=true;druid.stat.slowSqlMillis=500
      # 配置DruidStatFilter
      web-stat-filter:
        enabled: true
        url-pattern: "/*"
        exclusions: "*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*"
      # 配置DruidStatViewServlet
      stat-view-servlet:
        url-pattern: "/druid/*"
        # IP白名单(没有配置或者为空，则允许所有访问)
        allow: 127.0.0.1,192.168.253.128
        # IP黑名单 (存在共同时，deny优先于allow)
        deny: 192.168.1.73
        #  禁用HTML页面上的“Reset All”功能
        reset-enable: false
        # 登录名
        login-username: admin
        # 登录密码
        login-password: 123456
mybatis-plus:
  mapper-locations: classpath*:com/wl/springcloud/**/mapping/*.xml
  global-config:
    banner: false # 是否控制台 print mybatis-plus 的 LOGO
    db-config:
      # 主键类型 auto:数据库ID自增 input:用户输入ID id_worker:全局唯一ID(数字类型唯一ID) uuid:全局唯一ID(UUID) id_worker_str:全局唯一ID(字符串类型唯一ID) none:
      id-type: auto
      #字段策略 default:默认,ignored:"忽略判断",not_null:"非 NULL 判断",not_empty:"非空判断"
      field-strategy: not_null
      # 默认数据库表下划线命名
      table-underline: true
      # 逻辑删除配置
      # 逻辑删除全局值（1表示已删除，这也是Mybatis Plus的默认配置）
      logic-delete-value: 1
      # 逻辑未删除全局值（0表示未删除，这也是Mybatis Plus的默认配置）
      logic-not-delete-value: 0
      db-type: mysql
  configuration:
    map-underscore-to-camel-case: true # 开启驼峰命名转换
eureka:
  client: #客户端注册进eureka服务列表内
    service-url:
      defaultZone: http://admin:123456@eureka7001.com:7001/eureka/,http://admin:123456@eureka7002.com:7002/eureka/
  instance:
    instance-id: oauth2_8081 #自定义服务名称信息
    prefer-ip-address: true #访问路径可以显示IP地址
info:
  app.name: springcloud-server
  company.name: www.wangliu.com
  build.artifactId: '@project.artifactId@'
  build.version: '@project.version@'
#用于SpringBoot Admin监控
management: #暴露actuator的所有端点
  endpoints:
    web:
      exposure:
        include: "*"
  endpoint: #health endpoint是否必须显示全部细节。默认情况下, /actuator/health 是公开的，并且不显示细节
    health:
      show-details: always
```

配置认证服务器AuthorizationServerConfig ，并添加@Configuration和@EnableAuthorizationServer注解，其中ClientDetailsServiceConfigurer配置在内存中，当然也可以从数据库读取，后面介绍配置在mysql中

 

```
/**
 * 〈OAuth2认证服务器〉
 *
 * @author 王柳
 * @date 2019/11/7 9:43
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;
    @Autowired
    private MyUserDetailService userDetailService;
    @Bean
    public TokenStore tokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }
    @Primary
    @Bean
    public TokenStore jdbcTokenStore() {
        return new JdbcTokenStore(dataSource);
    }
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                .allowFormAuthenticationForClients()
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }
    public static void main(String[] args) {
        // 配置在数据库中，使用BCryptPasswordEncoder时，oauth_client_details表中的client_secret需要加密后存储
        System.out.println(new BCryptPasswordEncoder().encode("123456"));
    }
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 配置在数据库中
//        clients.withClientDetails(clientDetails());
        // 配置在内存中
        clients.inMemory()
                .withClient("android")
                .scopes("read")
                // 使用BCryptPasswordEncoder时需要加密
                .secret(new BCryptPasswordEncoder().encode("123456"))
                .authorizedGrantTypes("password", "authorization_code", "refresh_token")
                .and()
                .withClient("webapp")
                .scopes("read")
                .authorizedGrantTypes("implicit")
                .and()
                .withClient("browser")
                .authorizedGrantTypes("refresh_token", "password")
                .scopes("read");
    }
    @Bean
    public ClientDetailsService clientDetails() {
        return new JdbcClientDetailsService(dataSource);
    }
    @Bean
    public WebResponseExceptionTranslator webResponseExceptionTranslator() {
        return new MssWebResponseExceptionTranslator();
    }
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // 配置在数据库中
//        endpoints.tokenStore(jdbcTokenStore())
                // 配置在内存中
        endpoints.tokenStore(tokenStore())
                .userDetailsService(userDetailService)
                .authenticationManager(authenticationManager);
        endpoints.tokenServices(defaultTokenServices());
        //认证异常翻译
        // endpoints.exceptionTranslator(webResponseExceptionTranslator());
    }
    /**
     * <p>注意，自定义TokenServices的时候，需要设置@Primary，否则报错，</p>
     *
     * @return
     */
    @Primary
    @Bean
    public DefaultTokenServices defaultTokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        // 配置在数据库中
//        tokenServices.setTokenStore(jdbcTokenStore());
        // 配置在内存中
        tokenServices.setTokenStore(tokenStore());
        tokenServices.setSupportRefreshToken(true);
        // token有效期自定义设置，默认12小时
        tokenServices.setAccessTokenValiditySeconds(60 * 60 * 12);
        // refresh_token默认30天
        tokenServices.setRefreshTokenValiditySeconds(60 * 60 * 24 * 7);
        return tokenServices;
    }
}
```

在上述配置中，认证的token是存到redis里的，如果你这里使用了Spring5.0以上的版本的话，使用默认的RedisTokenStore认证时会报如下异常：

 

```
nested exception is java.lang.NoSuchMethodError: org.springframework.data.redis.connection.RedisConnection.set([B[B)V
```

原因是spring-data-redis 2.0版本中set(String,String)被弃用了，要使用RedisConnection.stringCommands().set(…)，所有我自定义一个RedisTokenStore，代码和RedisTokenStore一样，只是把所有conn.set(…)都换成conn..stringCommands().set(…)，测试后方法可行。

 

```
/**
 * 〈重写RedisTokenStore〉
 * spring5.0修改了一些方法，导致不兼容，需要重写全部的set()为stringCommands().set()
 *
 * @author 王柳
 * @date 2019/11/7 9:43
 */
public class RedisTokenStore implements TokenStore {
    private static final String ACCESS = "access:";
    private static final String AUTH_TO_ACCESS = "auth_to_access:";
    private static final String AUTH = "auth:";
    private static final String REFRESH_AUTH = "refresh_auth:";
    private static final String ACCESS_TO_REFRESH = "access_to_refresh:";
    private static final String REFRESH = "refresh:";
    private static final String REFRESH_TO_ACCESS = "refresh_to_access:";
    private static final String CLIENT_ID_TO_ACCESS = "client_id_to_access:";
    private static final String UNAME_TO_ACCESS = "uname_to_access:";
    private final RedisConnectionFactory connectionFactory;
    private AuthenticationKeyGenerator authenticationKeyGenerator = new DefaultAuthenticationKeyGenerator();
    private RedisTokenStoreSerializationStrategy serializationStrategy = new JdkSerializationStrategy();
    private String prefix = "";
    public RedisTokenStore(RedisConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }
    public void setAuthenticationKeyGenerator(AuthenticationKeyGenerator authenticationKeyGenerator) {
        this.authenticationKeyGenerator = authenticationKeyGenerator;
    }
    public void setSerializationStrategy(RedisTokenStoreSerializationStrategy serializationStrategy) {
        this.serializationStrategy = serializationStrategy;
    }
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
    private RedisConnection getConnection() {
        return this.connectionFactory.getConnection();
    }
    private byte[] serialize(Object object) {
        return this.serializationStrategy.serialize(object);
    }
    private byte[] serializeKey(String object) {
        return this.serialize(this.prefix + object);
    }
    private OAuth2AccessToken deserializeAccessToken(byte[] bytes) {
        return (OAuth2AccessToken) this.serializationStrategy.deserialize(bytes, OAuth2AccessToken.class);
    }
    private OAuth2Authentication deserializeAuthentication(byte[] bytes) {
        return (OAuth2Authentication) this.serializationStrategy.deserialize(bytes, OAuth2Authentication.class);
    }
    private OAuth2RefreshToken deserializeRefreshToken(byte[] bytes) {
        return (OAuth2RefreshToken) this.serializationStrategy.deserialize(bytes, OAuth2RefreshToken.class);
    }
    private byte[] serialize(String string) {
        return this.serializationStrategy.serialize(string);
    }
    private String deserializeString(byte[] bytes) {
        return this.serializationStrategy.deserializeString(bytes);
    }
    @Override
    public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
        String key = this.authenticationKeyGenerator.extractKey(authentication);
        byte[] serializedKey = this.serializeKey(AUTH_TO_ACCESS + key);
        byte[] bytes = null;
        RedisConnection conn = this.getConnection();
        try {
            bytes = conn.get(serializedKey);
        } finally {
            conn.close();
        }
        OAuth2AccessToken accessToken = this.deserializeAccessToken(bytes);
        if (accessToken != null) {
            OAuth2Authentication storedAuthentication = this.readAuthentication(accessToken.getValue());
            if (storedAuthentication == null || !key.equals(this.authenticationKeyGenerator.extractKey(storedAuthentication))) {
                this.storeAccessToken(accessToken, authentication);
            }
        }
        return accessToken;
    }
    @Override
    public OAuth2Authentication readAuthentication(OAuth2AccessToken token) {
        return this.readAuthentication(token.getValue());
    }
    @Override
    public OAuth2Authentication readAuthentication(String token) {
        byte[] bytes = null;
        RedisConnection conn = this.getConnection();
        try {
            bytes = conn.get(this.serializeKey("auth:" + token));
        } finally {
            conn.close();
        }
        OAuth2Authentication auth = this.deserializeAuthentication(bytes);
        return auth;
    }
    @Override
    public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken token) {
        return this.readAuthenticationForRefreshToken(token.getValue());
    }
    public OAuth2Authentication readAuthenticationForRefreshToken(String token) {
        RedisConnection conn = getConnection();
        try {
            byte[] bytes = conn.get(serializeKey(REFRESH_AUTH + token));
            OAuth2Authentication auth = deserializeAuthentication(bytes);
            return auth;
        } finally {
            conn.close();
        }
    }
    @Override
    public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
        byte[] serializedAccessToken = serialize(token);
        byte[] serializedAuth = serialize(authentication);
        byte[] accessKey = serializeKey(ACCESS + token.getValue());
        byte[] authKey = serializeKey(AUTH + token.getValue());
        byte[] authToAccessKey = serializeKey(AUTH_TO_ACCESS + authenticationKeyGenerator.extractKey(authentication));
        byte[] approvalKey = serializeKey(UNAME_TO_ACCESS + getApprovalKey(authentication));
        byte[] clientId = serializeKey(CLIENT_ID_TO_ACCESS + authentication.getOAuth2Request().getClientId());
        RedisConnection conn = getConnection();
        try {
            conn.openPipeline();
            conn.stringCommands().set(accessKey, serializedAccessToken);
            conn.stringCommands().set(authKey, serializedAuth);
            conn.stringCommands().set(authToAccessKey, serializedAccessToken);
            if (!authentication.isClientOnly()) {
                conn.rPush(approvalKey, serializedAccessToken);
            }
            conn.rPush(clientId, serializedAccessToken);
            if (token.getExpiration() != null) {
                int seconds = token.getExpiresIn();
                conn.expire(accessKey, seconds);
                conn.expire(authKey, seconds);
                conn.expire(authToAccessKey, seconds);
                conn.expire(clientId, seconds);
                conn.expire(approvalKey, seconds);
            }
            OAuth2RefreshToken refreshToken = token.getRefreshToken();
            if (refreshToken != null && refreshToken.getValue() != null) {
                byte[] refresh = serialize(token.getRefreshToken().getValue());
                byte[] auth = serialize(token.getValue());
                byte[] refreshToAccessKey = serializeKey(REFRESH_TO_ACCESS + token.getRefreshToken().getValue());
                conn.stringCommands().set(refreshToAccessKey, auth);
                byte[] accessToRefreshKey = serializeKey(ACCESS_TO_REFRESH + token.getValue());
                conn.stringCommands().set(accessToRefreshKey, refresh);
                if (refreshToken instanceof ExpiringOAuth2RefreshToken) {
                    ExpiringOAuth2RefreshToken expiringRefreshToken = (ExpiringOAuth2RefreshToken) refreshToken;
                    Date expiration = expiringRefreshToken.getExpiration();
                    if (expiration != null) {
                        int seconds = Long.valueOf((expiration.getTime() - System.currentTimeMillis()) / 1000L)
                                .intValue();
                        conn.expire(refreshToAccessKey, seconds);
                        conn.expire(accessToRefreshKey, seconds);
                    }
                }
            }
            conn.closePipeline();
        } finally {
            conn.close();
        }
    }
    private static String getApprovalKey(OAuth2Authentication authentication) {
        String userName = authentication.getUserAuthentication() == null ? "" : authentication.getUserAuthentication().getName();
        return getApprovalKey(authentication.getOAuth2Request().getClientId(), userName);
    }
    private static String getApprovalKey(String clientId, String userName) {
        return clientId + (userName == null ? "" : ":" + userName);
    }
    @Override
    public void removeAccessToken(OAuth2AccessToken accessToken) {
        this.removeAccessToken(accessToken.getValue());
    }
    @Override
    public OAuth2AccessToken readAccessToken(String tokenValue) {
        byte[] key = serializeKey(ACCESS + tokenValue);
        byte[] bytes = null;
        RedisConnection conn = getConnection();
        try {
            bytes = conn.get(key);
        } finally {
            conn.close();
        }
        OAuth2AccessToken accessToken = deserializeAccessToken(bytes);
        return accessToken;
    }
    public void removeAccessToken(String tokenValue) {
        byte[] accessKey = serializeKey(ACCESS + tokenValue);
        byte[] authKey = serializeKey(AUTH + tokenValue);
        byte[] accessToRefreshKey = serializeKey(ACCESS_TO_REFRESH + tokenValue);
        RedisConnection conn = getConnection();
        try {
            conn.openPipeline();
            conn.get(accessKey);
            conn.get(authKey);
            conn.del(accessKey);
            conn.del(accessToRefreshKey);
            // Don't remove the refresh token - it's up to the caller to do that
            conn.del(authKey);
            List<Object> results = conn.closePipeline();
            byte[] access = (byte[]) results.get(0);
            byte[] auth = (byte[]) results.get(1);
            OAuth2Authentication authentication = deserializeAuthentication(auth);
            if (authentication != null) {
                String key = authenticationKeyGenerator.extractKey(authentication);
                byte[] authToAccessKey = serializeKey(AUTH_TO_ACCESS + key);
                byte[] unameKey = serializeKey(UNAME_TO_ACCESS + getApprovalKey(authentication));
                byte[] clientId = serializeKey(CLIENT_ID_TO_ACCESS + authentication.getOAuth2Request().getClientId());
                conn.openPipeline();
                conn.del(authToAccessKey);
                conn.lRem(unameKey, 1, access);
                conn.lRem(clientId, 1, access);
                conn.del(serialize(ACCESS + key));
                conn.closePipeline();
            }
        } finally {
            conn.close();
        }
    }
    @Override
    public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication) {
        byte[] refreshKey = serializeKey(REFRESH + refreshToken.getValue());
        byte[] refreshAuthKey = serializeKey(REFRESH_AUTH + refreshToken.getValue());
        byte[] serializedRefreshToken = serialize(refreshToken);
        RedisConnection conn = getConnection();
        try {
            conn.openPipeline();
            conn.stringCommands().set(refreshKey, serializedRefreshToken);
            conn.stringCommands().set(refreshAuthKey, serialize(authentication));
            if (refreshToken instanceof ExpiringOAuth2RefreshToken) {
                ExpiringOAuth2RefreshToken expiringRefreshToken = (ExpiringOAuth2RefreshToken) refreshToken;
                Date expiration = expiringRefreshToken.getExpiration();
                if (expiration != null) {
                    int seconds = Long.valueOf((expiration.getTime() - System.currentTimeMillis()) / 1000L)
                            .intValue();
                    conn.expire(refreshKey, seconds);
                    conn.expire(refreshAuthKey, seconds);
                }
            }
            conn.closePipeline();
        } finally {
            conn.close();
        }
    }
    @Override
    public OAuth2RefreshToken readRefreshToken(String tokenValue) {
        byte[] key = serializeKey(REFRESH + tokenValue);
        byte[] bytes = null;
        RedisConnection conn = getConnection();
        try {
            bytes = conn.get(key);
        } finally {
            conn.close();
        }
        OAuth2RefreshToken refreshToken = deserializeRefreshToken(bytes);
        return refreshToken;
    }
    @Override
    public void removeRefreshToken(OAuth2RefreshToken refreshToken) {
        this.removeRefreshToken(refreshToken.getValue());
    }
    public void removeRefreshToken(String tokenValue) {
        byte[] refreshKey = serializeKey(REFRESH + tokenValue);
        byte[] refreshAuthKey = serializeKey(REFRESH_AUTH + tokenValue);
        byte[] refresh2AccessKey = serializeKey(REFRESH_TO_ACCESS + tokenValue);
        byte[] access2RefreshKey = serializeKey(ACCESS_TO_REFRESH + tokenValue);
        RedisConnection conn = getConnection();
        try {
            conn.openPipeline();
            conn.del(refreshKey);
            conn.del(refreshAuthKey);
            conn.del(refresh2AccessKey);
            conn.del(access2RefreshKey);
            conn.closePipeline();
        } finally {
            conn.close();
        }
    }
    @Override
    public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken refreshToken) {
        this.removeAccessTokenUsingRefreshToken(refreshToken.getValue());
    }
    private void removeAccessTokenUsingRefreshToken(String refreshToken) {
        byte[] key = serializeKey(REFRESH_TO_ACCESS + refreshToken);
        List<Object> results = null;
        RedisConnection conn = getConnection();
        try {
            conn.openPipeline();
            conn.get(key);
            conn.del(key);
            results = conn.closePipeline();
        } finally {
            conn.close();
        }
        if (results == null) {
            return;
        }
        byte[] bytes = (byte[]) results.get(0);
        String accessToken = deserializeString(bytes);
        if (accessToken != null) {
            removeAccessToken(accessToken);
        }
    }
    @Override
    public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String userName) {
        byte[] approvalKey = serializeKey(UNAME_TO_ACCESS + getApprovalKey(clientId, userName));
        List<byte[]> byteList = null;
        RedisConnection conn = getConnection();
        try {
            byteList = conn.lRange(approvalKey, 0, -1);
        } finally {
            conn.close();
        }
        if (byteList == null || byteList.size() == 0) {
            return Collections.<OAuth2AccessToken>emptySet();
        }
        List<OAuth2AccessToken> accessTokens = new ArrayList<OAuth2AccessToken>(byteList.size());
        for (byte[] bytes : byteList) {
            OAuth2AccessToken accessToken = deserializeAccessToken(bytes);
            accessTokens.add(accessToken);
        }
        return Collections.<OAuth2AccessToken>unmodifiableCollection(accessTokens);
    }
    @Override
    public Collection<OAuth2AccessToken> findTokensByClientId(String clientId) {
        byte[] key = serializeKey(CLIENT_ID_TO_ACCESS + clientId);
        List<byte[]> byteList = null;
        RedisConnection conn = getConnection();
        try {
            byteList = conn.lRange(key, 0, -1);
        } finally {
            conn.close();
        }
        if (byteList == null || byteList.size() == 0) {
            return Collections.<OAuth2AccessToken>emptySet();
        }
        List<OAuth2AccessToken> accessTokens = new ArrayList<OAuth2AccessToken>(byteList.size());
        for (byte[] bytes : byteList) {
            OAuth2AccessToken accessToken = deserializeAccessToken(bytes);
            accessTokens.add(accessToken);
        }
        return Collections.<OAuth2AccessToken>unmodifiableCollection(accessTokens);
    }
}
```

配置资源服务器

 

```
/**
 * 资源认证服务器
 * ResourceServerConfig 用于保护oauth相关的endpoints，同时主要作用于用户的登录(form login,Basic auth)
 * @author 王柳
 * @date 2019/11/7 9:43
 */
@Configuration
@EnableResourceServer
@Order(3)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .and()
                .requestMatchers().antMatchers("/api/**")
                .and()
                .authorizeRequests()
                .antMatchers("/api/**").authenticated()
                .and()
                .httpBasic();
    }
}
```

配置Spring Security

 

```
/**
 * 〈security配置〉
 * SecurityConfig 用于保护oauth要开放的资源，同时主要作用于client端以及token的认证(Bearer auth)
 *
 * @author 王柳
 * @date 2019/11/7 9:52
 */
@Configuration
@EnableWebSecurity
@Order(2)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private MyUserDetailService userDetailService;
    @Bean
    public PasswordEncoder passwordEncoder() {
        //密码验证为了方便我使用了不加密的方式，重写了PasswordEncoder，实际开发还是建议使用BCryptPasswordEncoder
        return new BCryptPasswordEncoder();
//        return new NoEncryptPasswordEncoder();
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.requestMatchers().antMatchers("/oauth/**")
                .and()
                .authorizeRequests()
                .antMatchers("/oauth/**").authenticated()
                .and()
                .csrf().disable();
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder());
    }
    /**
     * 不定义没有password grant_type,密码模式需要AuthenticationManager支持
     *
     * @return
     * @throws Exception
     */
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
```

可以看到ResourceServerConfig 是比SecurityConfig 的优先级低的。

二者的关系：

- ResourceServerConfig 用于保护oauth相关的endpoints，同时主要作用于用户的登录(form login,Basic auth)
- SecurityConfig 用于保护oauth要开放的资源，同时主要作用于client端以及token的认证(Bearer auth)

所以我们让SecurityConfig优先于ResourceServerConfig，且在SecurityConfig 不拦截oauth要开放的资源，在ResourceServerConfig 中配置需要token验证的资源，也就是我们对外提供的接口。所以这里对于所有微服务的接口定义有一个要求，就是全部以/api开头。

如果这里不这样配置的话，在你拿到access_token去请求各个接口时会报 invalid_token的提示。

另外，由于我们自定义认证逻辑，所以需要重写UserDetailService

 

```
/**
 * 〈自定义UserDetailService〉
 *
 * @author 王柳
 * @date 2019/11/7 9:43
 */
@Service("userDetailService")
public class MyUserDetailService implements UserDetailsService {
    @Resource
    private MemberMapper memberMapper;
    @Override
    public UserDetails loadUserByUsername(String memberName) throws UsernameNotFoundException {
        Member member = memberMapper.findByMemberName(memberName);
        if (member == null) {
            throw new UsernameNotFoundException(memberName);
        }
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        // 可用性 :true:可用 false:不可用
        boolean enabled = true;
        // 过期性 :true:没过期 false:过期
        boolean accountNonExpired = true;
        // 有效性 :true:凭证有效 false:凭证无效
        boolean credentialsNonExpired = true;
        // 锁定性 :true:未锁定 false:已锁定
        boolean accountNonLocked = true;
        for (Role role : member.getRoles()) {
            //角色必须是ROLE_开头，可以在数据库中设置
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getRoleName());
            grantedAuthorities.add(grantedAuthority);
            //获取权限
            for (Permission permission : role.getPermissions()) {
                GrantedAuthority authority = new SimpleGrantedAuthority(permission.getUri());
                grantedAuthorities.add(authority);
            }
        }
        // 使用BCryptPasswordEncoder时需要加密
        User user = new User(member.getMemberName(), new BCryptPasswordEncoder().encode(member.getPassword()),
                enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, grantedAuthorities);
        return user;
    }
}
```

密码验证为了方便我使用了不加密的方式，重写了PasswordEncoder，实际开发还是建议使用BCryptPasswordEncoder。这里我使用了BCryptPasswordEncoder，使用NoEncryptPasswordEncoder也可以，只是需要在BCryptPasswordEncoder加密注释的地方把加密代码去掉即可。

 

```
/**
 * 〈自定义无加密密码验证〉
 *
 * @author Curise
 * @create 2018/12/13
 * @since 1.0.0
 */
public class NoEncryptPasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence charSequence) {
        return (String) charSequence;
    }
    @Override
    public boolean matches(CharSequence charSequence, String s) {
        return s.equals((String) charSequence);
    }
}
```

定义一个Controller，提供两个接口，/api/member用来获取当前用户信息，/api/exit用来注销当前用户

 

```
/**
 * 〈会员Controller〉
 *
 * @author 王柳
 * @date 2019/11/7 10:21
 */
@RestController
@RequestMapping("/api")
public class MemberController {
    @Autowired
    private MyUserDetailService userDetailService;
    @Autowired
    private ConsumerTokenServices consumerTokenServices;
    @GetMapping("/member")
    public Principal user(Principal member) {
        return member;
    }
    @DeleteMapping(value = "/exit")
    public Result revokeToken(String access_token) {
        Result result = new Result();
        if (consumerTokenServices.revokeToken(access_token)) {
            result.setCode(ResultCode.SUCCESS.getCode());
            result.setMessage("注销成功");
        } else {
            result.setCode(ResultCode.FAILED.getCode());
            result.setMessage("注销失败");
        }
        return result;
    }
}
```

## 构建会员服务member

引入依赖

 

```
<dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-oauth2</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-security</artifactId>
        </dependency>
</dependencies>
```

配置资源服务器

 

```
/**
 * 〈OAuth资源服务配置〉
 *
 * @author wangliu
 * @date 2018/12/14
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .and()
                .requestMatchers().antMatchers("/api/**")
                .and()
                .authorizeRequests()
                .antMatchers("/api/**").authenticated()
                .and()
                .httpBasic();
    }
}
```

修改配置文件application.yml

 

```
spring:
  application:
    name: member
server:
  port: 1201
eureka:
  client:
    service-url:
      defaultZone: http://admin:123456@eureka7001.com:7001/eureka/,http://admin:123456@eureka7002.com:7002/eureka/
  instance:
    prefer-ip-address: true
    instance-id: member1201
info:
  app.name: springcloud-client
  company.name: www.wangliu.com
  build.artifactId: '@project.artifactId@'
  build.version: '@project.version@'
security:
  oauth2:
    resource:
      id: member
      user-info-uri: http://localhost:9527/auth/api/member
      prefer-token-info: false
```

MemberApplication启动类配置，引入注解@EnableGlobalMethodSecurity(prePostEnabled = true)

 

```
@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MemberApplication {
    public static void main(String[] args) {
        SpringApplication.run(MemberApplication.class, args);
    }
}
```

提供对外接口

 

```
@RestController
@RequestMapping("/api")
public class MemberController {
    @GetMapping("hello")
    @PreAuthorize("hasAnyAuthority('hello')")
    public String hello(){
        return "hello";
    }
    @GetMapping("current")
    public Principal user(Principal principal) {
        return principal;
    }
    @GetMapping("query")
    @PreAuthorize("hasAnyAuthority('query')")
    public String query() {
        return "具有query权限";
    }
}
```

## 配置网关服务zuul

zuul微服务中引入依赖

 

```
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-oauth2</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-security</artifactId>
        </dependency>
```

修改配置文件application.yml，添加auth和member相关路由，添加OAuth2配置

 

```
server:
  port: 9527
spring:
  application:
    name: gateway
#prefix表示访问前缀，ignored-services忽略所有请求，不包括zuul.routes指定的路径,
#zuul:
#  prefix: /abc
#  ignored-services: "*"
# routes to serviceId 这里边是通过serviceId来绑定地址，当在路径后添加/api-a/   则是访问service-A对应的服务。
# ** 表示多层级，*表示单层级
zuul:
  routes:
    defalutApi:
      path: /api/**
      serviceId: student
    apiB:
      path: /admin/**
      serviceId: course
    oa:
      path: /oa/**
      serviceId: oa
    member:
      path: /member/**
      serviceId: member
      sensitiveHeaders: "*"
    auth:
      path: /auth/**
      serviceId: oauth2
      sensitiveHeaders: "*"
  retryable: false
  ignored-services: "*"
  ribbon:
    eager-load:
      enabled: true
  host:
    connect-timeout-millis: 3000
    socket-timeout-millis: 3000
  add-proxy-headers: true
# Zuul超时
# 对于通过serviceId路由的服务,需要设置两个参数：ribbon.ReadTimeout 和ribbon.SocketTimeout
# 对于通过url路由的服务,需要设置两个参数：zuul.host.socket-timeout-millis或zuul.host.connect-timeout-millis
ribbon:
  ReadTimeout: 3000  # 单位毫秒数
  SocketTimeout: 3000
  MaxAutoRetries: 1
  MaxAutoRetriesNextServer: 2
  eureka:
    enabled: true
hystrix:
  command:
    default:
      execution:
        timeout:
          enabled: true
        isolation:
          thread:
            timeoutInMilliseconds: 24000
#---------------------OAuth2---------------------
security:
  oauth2:
    client:
      access-token-uri: http://localhost:${server.port}/auth/oauth/token
      user-authorization-uri: http://localhost:${server.port}/auth/oauth/authorize
      client-id: web
    resource:
      user-info-uri:  http://localhost:${server.port}/auth/api/member
      prefer-token-info: false
#---------------------OAuth2---------------------
eureka:
  client:
    service-url:
      defaultZone: http://admin:123456@eureka7001.com:7001/eureka,http://admin:123456@eureka7002.com:7002/eureka
  instance:
      instance-id: gateway9527 #自定义服务名称信息
      prefer-ip-address: true #访问路径可以显示IP地址
info:
  app.name: springcloud-server
  company.name: www.wangliu.com
  build.artifactId: '@project.artifactId@'
  build.version: '@project.version@'
```

ZuulApplication主启动类引入注解@EnableOAuth2Sso

 

```
/**
 * @Description
 * @Author 王柳
 * @Date 2019/10/11 8:51
 */
@SpringBootApplication
@EnableZuulProxy
@EnableOAuth2Sso
public class ZuulApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZuulApplication.class, args);
    }
}
```

Spring Security配置

 

```
/**
 * 〈Security配置〉
 *
 * @author 王柳
 * @date 2018/12/13
 */
@Configuration
@EnableWebSecurity
@Order(99)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
    }
}
```

接下来依次启动eureka-server、member、oauth2、zuul微服务

先发送一个请求测试一下未认证的效果

![img](/a6a8e6ec-bf20-4a66-9d7b-4fbd37fb1d26/128/index_files/7282839c-a829-4dde-ae28-d88bab366f95.png)

获取认证

![img](/a6a8e6ec-bf20-4a66-9d7b-4fbd37fb1d26/128/index_files/13828fcf-f5ca-451d-9100-cc89829b27bf.png)

使用access_token请求auth服务下的用户信息接口

![img](/a6a8e6ec-bf20-4a66-9d7b-4fbd37fb1d26/128/index_files/a5817d31-8c92-42cc-bb34-5e21726b2c64.png)

使用access_token请求member服务下的用户信息接口

![img](/a6a8e6ec-bf20-4a66-9d7b-4fbd37fb1d26/128/index_files/103f3748-613c-464a-bfcd-830e79b3c1cb.png)

请求member服务的query接口

![img](/a6a8e6ec-bf20-4a66-9d7b-4fbd37fb1d26/128/index_files/e1baf39f-84f8-4ec4-bdff-308db6498b15.png)

请求member服务的hello接口，数据库里并没有给用户hello权限

![img](/a6a8e6ec-bf20-4a66-9d7b-4fbd37fb1d26/128/index_files/ab58aee5-1a5d-4552-ba4b-30cef18d7afd.png)

刷新token

![img](/a6a8e6ec-bf20-4a66-9d7b-4fbd37fb1d26/128/index_files/b38f418b-2eba-408b-95c3-90814868cd63.png)

注销

![img](/a6a8e6ec-bf20-4a66-9d7b-4fbd37fb1d26/128/index_files/b7f1a5fb-1936-4708-9d5e-35328ee7236e.png)

Postman请求已经导出，在db文件夹下

![img](/a6a8e6ec-bf20-4a66-9d7b-4fbd37fb1d26/128/index_files/ecb04e45-d70d-4d73-9609-b95f8cc9fc92.png)

## 问题：获取认证时返回401

如下：

 

```
{
    "timestamp": "2019-08-13T03:25:27.161+0000",
    "status": 401,
    "error": "Unauthorized",
    "message": "Unauthorized",
    "path": "/oauth/token"
}
```

原因是在发起请求的时候没有添加Basic Auth认证，如下图：

![img](/a6a8e6ec-bf20-4a66-9d7b-4fbd37fb1d26/128/index_files/66251acd-e6cb-49fc-85fe-10bcb4e739d0.png)

添加Basic Auth认证后会在headers添加一个认证消息头

![img](/a6a8e6ec-bf20-4a66-9d7b-4fbd37fb1d26/128/index_files/31b01d06-33a1-486d-8ab5-5c9ce0e26eda.png)

添加Basic Auth认证的信息在代码中有体现：

![img](/a6a8e6ec-bf20-4a66-9d7b-4fbd37fb1d26/128/index_files/e0999a39-65ab-4946-aad8-fe2bdc1dd07c.png)

## 问题：加密模式获取认证时返回Bad client credentials

SecurityConfig中

![img](/a6a8e6ec-bf20-4a66-9d7b-4fbd37fb1d26/128/index_files/e6da11c5-07a5-4ad3-a3f0-1916f4b00883.png)

AuthorizationServerConfig中

![img](/a6a8e6ec-bf20-4a66-9d7b-4fbd37fb1d26/128/index_files/cb32f0c4-fc3e-4d38-b745-9d1699e4640a.png)

MyUserDetailService中

![img](/a6a8e6ec-bf20-4a66-9d7b-4fbd37fb1d26/128/index_files/d0512e8f-2bfe-4dc7-8879-d414f659116d.png)

## **客户端信息和token信息从MySQL数据库中获取**

现在客户端信息都是存在内存中的，生产环境肯定不可以这么做，要支持客户端的动态添加或删除，所以我选择把客户端信息存到MySQL中。

首先，创建数据表，数据表的结构官方已经给出，地址在

https://github.com/spring-projects/spring-security-oauth/blob/master/spring-security-oauth2/src/test/resources/schema.sql 

其次，需要修改一下sql脚本，把主键的长度改为128，LONGVARBINARY类型改为blob，调整后的sql脚本在db文件夹中

![img](/a6a8e6ec-bf20-4a66-9d7b-4fbd37fb1d26/128/index_files/6d63117d-b157-4c19-90cf-2616223ef815.png)

然后在数据库创建数据表，将客户端信息添加到oauth_client_details表中

**注意**：**如果使用了BCryptPasswordEncoder加密，client_secret需要加密后存储**

![img](/a6a8e6ec-bf20-4a66-9d7b-4fbd37fb1d26/128/index_files/76dec7ab-e9f6-40cc-bde1-33b6df299eef.png)

修改AuthorizationServerConfig中的三处配置

![img](/a6a8e6ec-bf20-4a66-9d7b-4fbd37fb1d26/128/index_files/956f673a-e573-47ce-b28b-7b7c9df98235.png)

![img](/a6a8e6ec-bf20-4a66-9d7b-4fbd37fb1d26/128/index_files/a7a3d606-efa3-4590-8490-64d7beb84d6c.png)

![img](/a6a8e6ec-bf20-4a66-9d7b-4fbd37fb1d26/128/index_files/226b101f-d8ad-4ca2-94d1-10e6e068ef5c.png)

使用postman调用后

查看数据表，发现token数据已经存到表里了

![img](/a6a8e6ec-bf20-4a66-9d7b-4fbd37fb1d26/128/index_files/22a92cd9-66eb-4836-8859-05bd95a7d040.png)

![img](/a6a8e6ec-bf20-4a66-9d7b-4fbd37fb1d26/128/index_files/97169e96-254f-4720-83d1-a8c9b22cbf76.png)

# 面试问题

## 1、什么是微服务？

微服务强调的是服务的大小，它关注的是某一个点，是具体解决某一个问题/提供落地对应服务的一个服务应用,狭意的看,可以看作Eclipse里面的一个个微服务工程/或者Module。

## 2、微服务之间是如何独立通讯的？

微服务架构是⼀种架构模式或者说是一种架构风格，它提倡将单⼀应⽤程序划分成⼀组⼩的服务，每个服务运行在其独立的自己的进程中，服务之间互相协调、互相配合，为⽤户提供最终价值。服务间采⽤轻量级的通信机制互相协作（通常是基于HTTP协议的RESTful API）。每个服务都围绕着具体业务进⾏构建，并且能够被独⽴的部署到⽣产环境、类⽣产环境等。另外，应当尽量避免统⼀的、集中式的服务管理机制，对具体的⼀个服务⽽⾔，应根据业务上下⽂，选择合适的语⾔、⼯具对其进⾏构建。可以有一个非常轻量级的集中式管理来协调这些服务，可以使用不同的语言来编写服务，也可以使用不同的数据存储。

## 3、SpringCloud和Dubbo有哪些区别？

![img](/2c2154f6-4be6-4bb8-9275-0c4fe7e11497/128/index_files/c3953348-069c-4a49-88eb-b0c6ef310306.jpg)

- 通信机制（本质区别）：Dubbo基于RPC远程过程调用，微服务SpringCloud基于Http的RESTful API调用；

严格来说，这两种方式各有优劣。虽然从一定程度上来说，后者牺牲了服务调用的性能，但也避免了上面提到的原生RPC带来的问题。而且REST相比RPC更为灵活，服务提供方和调用方的依赖只依靠一纸契约，不存在代码级别的强依赖，这在强调快速演化的微服务环境下，显得更加合适。

-  品牌机与组装机的区别

很明显，Spring Cloud的功能比DUBBO更加强大，涵盖面更广，而且作为Spring的拳头项目，它也能够与Spring Framework、Spring Boot、Spring Data、Spring Batch等其他Spring项目完美融合，这些对于微服务而言是至关重要的。使用Dubbo构建的微服务架构就像组装电脑，各环节我们的选择自由度很高，但是最终结果很有可能因为一条内存质量不行就点不亮了，总是让人不怎么放心，但是如果你是一名高手，那这些都不是问题；而Spring Cloud就像品牌机，在Spring Source的整合下，做了大量的兼容性测试，保证了机器拥有更高的稳定性，但是如果要在使用非原装组件外的东西，就需要对其基础有足够的了解。

-  社区支持与更新力度

最为重要的是，DUBBO停止了5年左右的更新，虽然2017.7重启了。对于技术发展的新需求，需要由开发者自行拓展升级（比如当当网弄出了DubboX），这对于很多想要采用微服务架构的中小软件组织，显然是不太合适的，中小公司没有这么强大的技术能力去修改Dubbo源码+周边的一整套解决方案，并不是每一个公司都有阿里的大牛+真实的线上生产环境测试过。

##  

## 4、SpringBoot和SpringCloud，请你谈谈对他们的理解？

- SpringBoot专注于快速方便的开发单个个体微服务。SpringCloud是关注全局的微服务协调整理治理框架，它将SpringBoot开发的一个个单体微服务整合并管理起来，为各个微服务之间提供，配置管理、服务发现、断路器、路由、微代理、事件总线、全局锁、决策竞选、分布式会话等等集成服务

-  SpringBoot可以离开SpringCloud独立使用开发项目，但是SpringCloud离不开SpringBoot，属于依赖的关系。
-  SpringBoot专注于快速、方便的开发单个微服务个体，SpringCloud关注全局的服务治理框架。

## 5、什么是服务熔断？什么是服务降级？

服务熔断：熔断机制是应对雪崩效应的一种微服务链路保护机制。当扇出链路的某个微服务不可用或者响应时间太长时，会进行服务的降级，进而熔断该节点微服务的调用，快速返回"错误"的响应信息。当检测到该节点微服务调用响应正常后恢复调用链路。在SpringCloud框架里熔断机制通过Hystrix实现。Hystrix会监控微服务间调用的状况，当失败的调用到一定阈值，缺省是5秒内20次调用失败就会启动熔断机制。熔断机制的注解是@HystrixCommand。

服务降级：整体资源快不够了，忍痛将某些服务先关掉，待渡过难关，再开启回来。服务降级处理是在客户端实现完成的，与服务端没有关系。

## 6、微服务的优缺点分别是什么？说下你在项目开发中碰到的坑。

优点：

   每个服务足够内聚，足够小，代码容易理解这样能聚焦一个指定的业务功能或业务需求；

  开发简单、开发效率提高，一个服务可能就是专一的只干一件事；

  微服务能够被小团队单独开发，这个小团队是2到5人的开发人员组成；

  微服务是松耦合的，是有功能意义的服务，无论是在开发阶段或部署阶段都是独立的；

  微服务能使用不同的语言开发；

  易于和第三方集成，微服务允许容易且灵活的方式集成自动部署，通过持续集成工具，如Jenkins,Hudson,bamboo；

  微服务易于被一个开发人员理解，修改和维护，这样小团队能够更关注自己的工资成果。无需通过合作才能体现价值；

  微服务允许你利用融合最新技术；

  微服务只是业务逻辑的代码，不会和HTML,CSS或其他界面组件混合；

  每个微服务都有自己的存储能力，可以有自己的数据库。也可以有统一的数据库。

缺点：

  开发人员要处理分布式系统的复杂性；

  多服务运维难度，随着服务的增加，运维的压力也在增大；

  系统部署依赖；

  服务间通信成本；

  数据一致性；

  系统集成测试；

  性能监控。

## 7、你所知道的微服务技术栈有哪些？请列举一二。

| 微服务条目                             | 落地技术                                                     | 备注 |
| -------------------------------------- | ------------------------------------------------------------ | ---- |
| 服务开发                               | SpringBoot、Spring、SpringMvc                                |      |
| 服务配置与管理                         | Netflix公司的Archaius、阿里的Diamond等                       |      |
| 服务注册与发现                         | Eureka、Consul、Zookeeper等                                  |      |
| 服务调用                               | Rest、RPC、gRPC                                              |      |
| 服务熔断器                             | Hystrix、Envoy等                                             |      |
| 负载均衡                               | Ribbon、Nginx等                                              |      |
| 服务接口调用(客户端调用服务的简化工具) | Feign等                                                      |      |
| 消息队列                               | Kafka、RabbitMQ、ActiveMQ等                                  |      |
| 服务配置中心管理                       | SpringCloudConfig、Chef等                                    |      |
| 服务路由（API网关）                    | Zuul等                                                       |      |
| 服务监控                               | Zabbix、Nagios、Metrics、Spectator等                         |      |
| 全链路追踪                             | Zipkin，Brave、Dapper等                                      |      |
| 服务部署                               | Docker、OpenStack、Kubernetes等                              |      |
| 数据流操作开发包                       | SpringCloud Stream（封装与Redis,Rabbit、Kafka等发送接收消息） |      |
| 事件消息总线                           | Spring Cloud Bus                                             |      |
| 。。。。。。                           |                                                              |      |

## 8、Eureka和Zookeeper都可以提供服务注册与发现的功能，请说说两个的区别？

**作为服务注册中心，Eureka比Zookeeper好在哪里：**

![img](/2c2154f6-4be6-4bb8-9275-0c4fe7e11497/128/index_files/b6f8be03-cd3b-4653-bf1c-aef682d878a0.jpg)

著名的CAP理论指出，一个分布式系统不可能同时满足C（一致性）、A（可用性）和P（分区容错性）。由于分区容错性P是分布式系统中必须要保证的，因此我们只能在A和C之间进行权衡。

Eureka遵守AP原则，Zookeeper遵守CP原则。

Zookeeper：当向注册中心查询服务列表时，我们可以容忍注册中心返回的是几分钟以前的注册信息，但不能接受服务之间down掉不可用。也就是说，服务注册功能对可用性的要求要高于一致性。但是Zookeeper会出现这样一种情况，但master节点因为网络故障与其他节点失去联系时，剩余节点会重新进行leader选举。问题在于，选举leader的实际太长，30~120s，且选举期间整个Zookeeper集群都是不可用的，这就导致在选举期间注册服务瘫痪。在云部署的环境下，因网络问题使得Zookeeper集群失去master节点是较大概率会发生的事，虽然服务能够最终恢复，但是漫长的选举时间导致的注册长期不可用是不能容忍的。

Eureka：Eureka看明白了这一点，因此在设计时就优先保证可用性。Eureka各个节点都是平等的，几个节点挂掉不会影响正常节点的工作，剩余的节点依然可以提供注册和查询服务。而Eureka的客户端在向某个Eureka注册时如果发现连接失败，则会自动切换至其他节点，只要有一台Eureka还在，就能保证注册服务可用（保证可用性），只不过查到的信息可能不是最新的（不保证强一致性）。除此之外，Eureka还有一种自我保护机制，如果在15分钟内超过85%的节点都没有正常的心跳，那么Eureka就任务客户端与注册中心出现了网络故障，此时会出现以下几种情况：

（1）、Eureka不再从注册列表中移除因为长时间没收到心跳而应该过期的服务

（2）、Eureka仍然能够接受新服务的注册和查询请求，但是不会被同步到其他节点上（即保证当前节点依然可用）

（3）、当网络稳定时，当前实例新的注册信息会被同步到其他节点中

因此，Eureka可用很好的应对因网络故障导致部分节点失去联系的情况，而不会像Zookeeper那样使整个注册服务瘫痪。
