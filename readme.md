# hunter-spring-boot-starter
提供以下功能和内置对象
1. 通用返回类Result

2. redis分布式锁

3. redis多数据源支持

4. quartz定时器自动装配

5. 全局异常处理

6. Jackson、yaml工具类

7. RestTemplate工具类

8. 线程池自动装配

9. swagger 自动装配

   
---
# 依赖
1. 所需依赖：springboot-starter、springboot-starter-web
2. 包含依赖：
    > 项目已经包含了这些依赖，无需重复引入。
    
    依赖|描述|版本
    --|--|--
    cglib|动态代理|3.2.12
    commons-lang3|apache工具|3.7
    commons-lang| 工具集| 2.5 
    lombok|简化代码|1.16.20
    commons-pool2 ||2.7.0

# 快速开始
1. 引入上述所需的依赖包  
```$xslt
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter</artifactId>
    <version>xxx</version>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <version>xxx</version>
</dependency>

```
2. 引入本项目  
```$xslt
    <groupId>com.github.gxhunter</groupId>
    <artifactId>hunter-spring-boot-starter</artifactId>
    <version>1.0.5</version>
```
# 功能描述
> 介绍本starter提供的一些通用工具、自动化装配方案等。
##  通用返回类com.github.gxhunter.vo.Result<T>
* 描述  
 在企业开发中，Controller一般需要返回通用的一个对象，一般包含status（返回码）、data（数据）、message（提示信息），这里提供了我本人常用的一个通用Result解决方案
 * 字段描述


字段|返回值|描述  
 :--|:--:|--:  
 status |int|状态码
 message|string|返回信息
 data   |T     |携带数据
 success|boolean|是否成功

 * 静态方法

 方法名称|描述
--|--
success(T data)|成功并返回数据
success()| 成功不携带数据
successMsg(String msg)|成功并返回信息
failed(String str)|失败，并提示信息，code使用默认
failed(T data,IErrorCode code)|失败，携带数据和提示信息和code
failed(T data,String message)|失败，并携带数据和提示信息
failed()|失败，不携带信息
---



## 分布式锁

* 核心注解 `com.github.gxhunter.lock.Lock`

* 注解参数

  ```java
     /**
       * <p>
       * 同一
      /**
       * <p>
       * 同一个应用，同一个线程可重入。
       * 要锁定的key中包含的属性,不指定时锁定当前方法
       * 指定key时将覆盖默认key生成策略，
       * 支持spel表达式，也支持纯文本字符串
       * 固定前缀：{keyPrex}{split}
       * 默认的key策略：固定前缀+方法全路径
       * 自行指定key时：固定前缀+指定的key(多个key使用分隔符分割，且每个key支持spel语法)
       * </p>
       *
       * @see AbstractLockTemplate#keyPrex
       * @see AbstractLockTemplate#SPLIT
       */
      String[] keys() default {};
  
      /**
       * 超时时间，单位毫秒
       * 默认30秒
       */
      long expireTime() default 30 * 1000;
  
      /**
       * 获取不到锁的等待时间，单位毫秒
       * 默认10秒
       */
      long retryTimes() default 10 * 1000;
  
      /**
       * 是否延迟释放
       *
       * @return false:不延迟，方法执行结束就释放。true:方法执行结束后不释放，等待超时
       */
      boolean delay() default false;
  
      /**
       * 是否可重入（同个应用同个线程）
       * @return
       */
      boolean reentrant() default true;个应用，同一个线程可重入。
       * 要锁定的key中包含的属性,不指定时锁定当前方法
       * 指定key时将覆盖默认key生成策略，
       * 支持spel表达式，也支持纯文本字符串
       * 固定前缀：{keyPrex}{split}
       * 默认的key策略：固定前缀+方法全路径
       * 自行指定key时：固定前缀+指定的key(多个key使用分隔符分割，且每个key支持spel语法)
       * </p>
       *
       * @see AbstractLockTemplate#keyPrex
       * @see AbstractLockTemplate#SPLIT
       */
      String[] keys() default {};
  ```

  

* 使用方法

  ```
   /**
       * 注解加在目标方法上即可，根据业务需求添加参数
       * @return
       * @see Lock
       */
      @Lock
      public IResult testLock(){
          return success(Thread.currentThread().getId());
      }
  ```



## redis多数据源

* 介绍

  spring-data-redis默认提供的redis装配和redisTemplate都只支持单数据源，单数据源配置如下：

  ```yaml
  spring:
    redis:
      host: 192.168.60.150
      port: 6379
  ```

* 多数据源，配置非常简单

  ```
  hunter:
    redis:
      source:
  #      第一个数据源，名称自定义
        work0:
          host: 192.168.60.150
          port: 6379
  #      第一个数据源，名称自定义
        work1:
          host: 192.168.60.150
          port: 6380
  ```

* 使用

  使用方法非常简单，需要在原来的基础上添加Qualifier注解  并指定名称即可

  ```
      @Qualifier("work0")
      @Autowired
      RedisTemplate work0;
  
      @Qualifier("work1")
      @Autowired
      RedisTemplate work1;
  ```



## quartz-starter

* 介绍

  quartz的定时器功能比jdk原生的timer和spring-schedule都更加强大的多，但是每次新增一个定时器都有手动添加 `JobDetail`和`Triggers`到容器，非常麻烦。

* 配置

  1. 引入maven依赖

     ```
        <dependency>
                 <groupId>org.springframework.boot</groupId>
                 <artifactId>spring-boot-starter-quartz</artifactId>
             </dependency>
     ```

     

  2. 在yml配置(参考)

     ```yml
     spring:  
       quartz:
         job-store-type: jdbc #数据库方式
         jdbc:
           initialize-schema: never #不初始化表结构
         properties:
           org:
             quartz:
               scheduler:
                 instanceId: AUTO #默认主机名和时间戳生成实例ID,可以是任何字符串，但对于所有调度程序来说，必须是唯一的 对应qrtz_scheduler_state INSTANCE_NAME字段
                 #instanceName: clusteredScheduler #quartzScheduler
               jobStore:
                 class: org.quartz.impl.jdbcjobstore.JobStoreTX #持久化配置
                 driverDelegateClass: org.quartz.impl.jdbcjobstore.StdJDBCDelegate #我们仅为数据库制作了特定于数据库的代理
                 useProperties: false #以指示JDBCJobStore将JobDataMaps中的所有值都作为字符串，因此可以作为名称 - 值对存储而不是在BLOB列中以其序列化形式存储更多复杂的对象。从长远来看，这是更安全的，因为您避免了将非String类序列化为BLOB的类版本问题。
                 tablePrefix: qrtz_  #数据库表前缀
                 misfireThreshold: 60000 #在被认为“失火”之前，调度程序将“容忍”一个Triggers将其下一个启动时间通过的毫秒数。默认值（如果您在配置中未输入此属性）为60000（60秒）。
                 clusterCheckinInterval: 5000 #设置此实例“检入”*与群集的其他实例的频率（以毫秒为单位）。影响检测失败实例的速度。
                 isClustered: true #打开群集功能
               threadPool: #连接池
                 class: org.quartz.simpl.SimpleThreadPool
                 threadCount: 10
                 threadPriority: 5
                 threadsInheritContextClassLoaderOfInitializingThread: true
     ```

* 使用

  ```java
  /**
   * 必须继承{@link AbstractJob}，添加{@link QuartzJob}注解
   * 最简单的quartz demo就完成了，是不是很简单
   */
  @QuartzJob(cron = "0/5 * * * * ?")
  public class TestJob extends AbstractJob{
      @Override
      public void run(JobExecutionContext context) throws JobExecutionException{
          System.out.println("quartz定时器运行");
      }
  }
  ```

  

 ##  Jackson、yaml工具类 com.github.gxhunter.util.BeanMapperUtil

 * **描述**  
    Jackson是spring默认的json序列化工具，但是与fastjson不同，没有内置很方便的方法可以在对象和javabean之间互转，大多数情况你可能会考虑fastjson作为json工具类，但这样有个问题，Jackson的注解在fastjson不再适用，为了解决这个问题，你可能还要侵入式地添加fastjson的注解。
    在同一个项目使用两个json工具也是不提倡的做法，所以这里提供了一个Jackson的封装工具JsonUtil。

 * 通过springboot yml进行配置
 ```$xslt

spring:
  jackson:
    serialization:
      WRITE_DATES_AS_TIMESTAMPS: false
      FAIL_ON_EMPTY_BEANS: false
    deserialization:
      FAIL_ON_UNKNOWN_PROPERTIES: false
    default-property-inclusion: non_null
 ```
* 静态方法
    1. 对象转为字符串
    ```$xslt
    /**
     * 对象转json
     */
    public <T> String stringify(T obj)
    ```
    
    2. 对象转为字符串并格式化
    ```$xslt
    /**
     * 对象转json
     */
    public <T> String stringifyPretty(T obj)
    ```
    
    3. 字符串 转为java对象
    ```$xslt
        /**
         * 字符串转为java对象
         *
         * @param str   json字符串
         * @param clazz java对象类型
         * @param <T>   java对象类型
         * @return
         */
        public <T> T parse(String str, Class<T> clazz)
    ```
    
* 使用方法

    ```
    //        获取json工具
            BeanMapperUtil jsonMapper = BeanMapperFactory.getJsonMapper();
    //        获取yaml工具
            BeanMapperUtil yamlMapper = BeanMapperFactory.getYamlMapper();
            
    //        json字符串转为java对象，yaml同理
            Map parse = jsonMapper.parse("{a:1}",Map.class);
    //        对象转为json，yaml同理
            String stringify = jsonMapper.stringify(new Object());
    //        对象转字符串后写入到磁盘文件，yaml同理
            jsonMapper.write(new Object(),new File("E:/test.txt"));
    ```

    

## Redis工具类
com.github.gxhunter.springdata.IRedisClient
基于Resttemplate做的二次封装

引入spring-boot-data-redis-starter即可使用

## restTemplate
* 使用okhttp代替默认的httpclient作为底层实现
* 具体配置
    在yml中
    ```
    hunter:
        spring:    
            rest:
                readTimeout: 读取超时(默认：5000ms)
                connectTimeout: 连接超时(默认：3000ms)
                writeTimeout: 写入超时(默认：5000ms)
    ```
## 线程池
在yml中配置（可选）
```$xslt
hunter:
    spring: 
        thread-pool:
        core-pool-size: 默认20 池中所保存的线程数，包括空闲线程。
        maximum-pool-size: 默认100 池中允许的最大线程数。
        keep-alive-time: 默认30 当线程数大于核心时，此为终止前多余的空闲线 程等待新任务的最长时间。
```
使用
```$xslt
@Autowired
private ExecutorService mPoolExecutor;
```
## swagger 支持
* 描述  
开发中，我们一般需要swagger来自动生成restapi文档，省去了在postman填写各类参数和url的麻烦，而swagger2官方目前并没有提供自动化装配starter，为了使用swagger2，你不得不编写bean和configuration，为了解决这个繁琐的问题本starter提供了swagger2的自动化装配。
1. 在pom中引入相关依赖，如
```$xslt
<dependency>
    <groupId>com.github.xiaoymin</groupId>
    <artifactId>swagger-bootstrap-ui</artifactId>
    <version>1.8.6</version>
</dependency>
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger2</artifactId>
    <version>2.9.2</version>
</dependency>
```
> 建议结合swagger-bootstart-ui使用
2. 在yml中做相关配置,如
```$xslt
hunter:
    spring:
        swagger:
        base-package: 扫描路径
        version: 版本
        description: 详细信息
        license: 授权协议
        title: 标题
        license-url: 授权协议url
        host: 127.0.0.1
        enabled: 是否开启，默认为false 关闭
        contact:
            email: 开发者邮箱
            name: 开发者名称
            url: 开发者url
```


## 待续...



 
