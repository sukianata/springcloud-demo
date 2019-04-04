# 微服务框架及常用组件
* **配置中心(config-server)**
* **Eureka(服务注册中心)** 
* **Ribbon(负载均衡)**  
* **Hystrix(服务容错保护)**  
* **Feign**  
* **Zuul(网关)**  
  
    
### **一、配置中心**  
&ensp;&ensp;**Spring Cloud Config**支持动态的从网络或本地获取配置文件。  
  
&ensp;&ensp;所需依赖:  

	<dependency>
    	<groupId>org.springframework.cloud</groupId>
    	<artifactId>spring-cloud-config-server</artifactId>
    </dependency>  
      
>启动类上需要加注解@EnableConfigServer  
  
&ensp;&ensp;服务端配置：  
<pre>
server:
  port: 8759 #指定服务端口
spring:
  application:
    name: config-server
  profiles:
    active: native #表明是本地环境
  cloud:
    config:
      server:
        native:
          search-locations: classpath:/config #申明本地配置文件位置	
</pre>  
&ensp;&ensp;客户端与之相关配置：  
<pre>
spring:
  cloud:
    config:
      name: eureka-client1 # 需要读取的资源名称 注意没有yml,对应命名规则中的{application}
      profile: dev # 对应命名规则中的{profile}
      label:  master #对应命名规则中的{label}
      uri:  http://localhost:8759 #配置中心的地址
</pre>   
>1.注意此处的配置要放在bootstrap.yml或bootstrap.properties中，bootstrap.yml用来引导项目的启动，指引项目从配置中心区获取配置  
>2、配置文件的命名规则一般是{application}-{profile}-{label}.yml
  
&ensp;&ensp;客户端依赖：  
 
	<dependency>
    	<groupId>org.springframework.cloud</groupId>
    	<artifactId>spring-cloud-starter-config</artifactId>
    </dependency>  

&ensp;&ensp;配置网络配置的方式为：  
<pre> 
spring:
  application:
    name: config-server
  cloud:
    config:
      server:
        git:
          uri: https://github.com/**
          search-paths: config-repo 
</pre>
### **二、Eureka(服务注册中心)**  
&ensp;&ensp;Eureka的主要作用是实现服务治理(注册与发现）。  
  
&ensp;&ensp;服务端所需依赖：  
  
	<dependency>
    	<groupId>org.springframework.cloud</groupId>
    	<artifactId>spring-cloud-starter-eureka-server</artifactId>
    </dependency> 
>启动类上需要注解：@EnableEurekaServer  
  
&ensp;&ensp;服务端相关配置：  
<pre>
server:
  port: 8760
eureka:
  instance:
    hostname: localhost #eureka服务端的实例名称
  client:
    register-with-eureka: false #是否向Eureka注册
    fetch-registry: false #是否向Eureka查找有哪些服务
    service-url:
      defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/ #与Eureka交互的地址接口，客户端地址要与此保持一致
</pre>
&ensp;&ensp;客户端相关依赖：  

	<dependency>
    	<groupId>org.springframework.cloud</groupId>
   		<artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>
   
>启动类上需要添加注解@EnableEurekaClient  

&ensp;&ensp;客户端相关配置:

<pre>
spring:
  profiles: dev
  application:
    name: eureka-client #向Eureka注册服务后该值会作为服务名
server:
  port: 8762
#  servlet:
#    context-path: /eureka-client 配置该属性时 访问时要带项目名

#eureka
eureka:
  client:
    register-with-eureka: true #是否向Eureka注册服务
    service-url:
      defaultZone: http://localhost:8760/eureka/ #与Eureka交互地址，要与服务端保持一致
</pre>

>访问 <http://localhost:8760/eureka/apps> 会以xml的形式返回服务列表信息  

### **三、Ribbon(负载均衡)**   
&ensp;&ensp;Spring Cloud 服务支持Ribbon+RestTemplate和Feign的形式;  

&ensp;&ensp;Spring Cloud Ribbon 是一个基于HTTP和TCP客户端负载均衡工具；  

&ensp;&ensp;所需依赖：  

	<dependency>
    	<groupId>org.springframework.cloud</groupId>
    	<artifactId>spring-cloud-starter-netflix-ribbon</artifactId>
    </dependency>  
  
>1.启动类加上@EnableEurekaClient或@EnableDiscoveryClient  
>2.有关这两个注解的区别戳 [spring cloud服务发现注解之@EnableDiscoveryClient与@EnableEurekaClient](https://blog.csdn.net/u012734441/article/details/78256256?locationNum=1&fps=1)  
>3.总结一句话就是若采用Eureka做服务发现组件就用@ EnableEurekaClient，其它的用@EnableDiscoveryClient  

启动类中代码块：  
  
```java    
@Bean
@LoadBalanced //开启负载均衡
RestTemplate restTemplate(){
    return new RestTemplate();
}
```  

### **四、Hystrix(服务容错保护)**   
&ensp;&ensp;Hystrix作为服务容错保护组件具备**服务降级、服务熔断、线程和信号隔离、请求缓存、请求合并以及服务监控**等诸多功能，该demo可以演示服务降级，服务熔断，请求缓存功能；  
  
&ensp;&ensp;所需依赖：  
  
	<dependency>
    	<groupId>org.springframework.cloud</groupId>
    	<artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
    </dependency>
>启动类上加@EnableCircuitBreaker开启hystrix支持  
  
&ensp;&ensp;hystrix默认超时时间为1s,设置超时时间方式为：  
  
<pre>
#hystrix配置
hystrix:
  command:
    default:
      execution:
        isolation:
          thread:
            timeoutInMilliseconds: 5000
</pre>  
  
>服务降级可以在service层采用HystrixCommand注解，  
>例如：  
>@HystrixCommand(fallbackMethod = "timeOutMsg")    
>其中timeOutMsg为方法名，且必须和原方法在同一个类中并具有相同的参数列表  

### **五、Feign**   
* 通过restTemplate调用其它服务的API时，必须在请求中进行参数的拼接，若有多个参数则会效率低下；  
  
* Feign整合了**Hystrix和Ribbon**，也就是支持Hystrix的服务容错保护以及Ribbon的负载均衡功能；  
  
&ensp;&ensp;所需依赖：  
  
	<dependency>
    	<groupId>org.springframework.cloud</groupId>
    	<artifactId>spring-cloud-starter-feign</artifactId>
    </dependency>
>启动类上加@EnableFeignClients注解，以便支持feign 
&ensp;&ensp;相关配置： 
   
<pre>
#hystrix配置
hystrix:
  command:
    default:
      execution:
        isolation:
          thread:
            timeoutInMilliseconds: 6200
#ribbon
ribbon:
  ReadTimeout: 5000
  ConnectTimeout: 2000
#feign
feign:
  hystrix:
    enabled: true #开启hystrix熔断策略
</pre>

>注意：feign集成了ribbon和hystrix，ribbon有一个重试机制，故若对ribbon和hystrix的超时时间进行设置时，时间阀值一般ribbon小于hystrix,不行先重试，重试了再不行则断开；   
 
&ensp;&ensp;**那么具体如何使用呢**？  
&ensp;&ensp;可以在service层写一个接口类，用@FeignClient注解修饰，同时可以指定服务降级时处理的类，例如：  
  
	@FeignClient(value="eureka-client",fallback = FeignCallBackServiceImpl.class)  
其中，FeignCallBackServiceImpl.class必须是该接口的实现，eureka-client 时eureka服务中心的服务名，在service声明
方法上方再用mvc注解的形式如@RequestMapping指定服务中要访问的方法名；  
> 注意：方法上方除了能用RequestMapping外，至于能否用@GetMapping,@PostMapping等有待测试；  

### **六、Zuul(网关)**  
 &ensp;&ensp;Zuul网关可以用来实现**反向代理**和**负载均衡**;支持接口映射，以免暴露真实接口地址，同时同一个接口名称可以映射多个真实服务实例，以实现负载均衡；  
   
 &ensp;&ensp;所需依赖：  
    
    <dependency>
    	<groupId>org.springframework.cloud</groupId>
    	<artifactId>spring-cloud-starter-netflix-zuul</artifactId>
    </dependency>  
>启动类上加注解：@EnableZuulProxy  
  
 &ensp;&ensp;相关示例配置： 
<pre>
zuul:
  routes: #zuul 会缓存请求 当某一服务断开后，要过一段时间才会切换到另一服务
    api-a: #此处随便定义
      path: /api-a/**
      serviceId: EUREKA-CLIENT #方法一：对应Eureka中的服务名
    api-a-url: # routes to url  这里是绑定具体的ip地址
      path: /api-a-url/**  #方法二：所有/api-a-url/**请求都会映射到http://localhost:8761/上
      url: http://localhost:8761/
  host:  #Zuul中默认就已经集成了Ribbon负载均衡和Hystix熔断机制 Hystrix 默认时间较短 容易造成ZuulException
    connect-timeout-millis: 10000
    socket-timeout-millis: 60000
</pre>    

**ZuulFilter的使用**  

实现自定义的过滤器可以通过继承ZuulFilter来实现，需要重写四个方法。方法部分说明见demo中注解。
  
### **七、总结**   
* 同一个项目可能同时扮演多个角色，比如既是Config客户端，又是Eureka客户端，还是Zuul,所以依赖引入和配置需要根据实际情况配合使用，灵活使用；  
* SpringCloud生态中，各个不同版本间存在很多兼容性问题中，例如：F版本SpringCloud的Zuul和SpringBoot 2.1.x不兼容，有时遇见exception时可以考虑往这个方向尝试；
* 依赖引入和配置问题，spring配置文件的属性颇多，可以上官网[https://spring.io/docs](https://spring.io/docs)查看API来查看属性的含义以及需要哪些属性，参考[Spring Boot配置文件规则以及使用方法官方文档查找以及Spring项目的官方文档查找方法](https://www.cnblogs.com/EasonJim/p/7522224.html)
