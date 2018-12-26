# hunter-spring-boot-starter
提供以下内置工具和对象
1. 通用返回类Result
2. 全局异常处理
3. Jackson工具类
4. StringUtil工具
5. Redis工具类
6. RestTemplate工具类
7. 线程池自动装配
8. swagger 自动装配
9. DefaultWebMVC注解
---
# 依赖
1. 所需依赖：springboot-starter、springboot-starter-web
2. 包含依赖：

    依赖|描述
    --|--
    commons-lang3|apache工具
    commons-lang| 工具集
    lombok|简化代码
    guava |googlet提供的工具

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
    <version>仓库最新版本</version>
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
 1. success(T data)
 成功并返回数据
 2. success()
 成功不携带数据
 3. successMsg(String msg)
 成功并返回信息
 
 4. failed(String str)
 失败，并提示信息，code使用默认
 5. failed(T data,IErrorCode code)
 失败，携带数据和提示信息和code
 6. failed(T data,String message)
 失败，并携带数据和提示信息
 7. failed()
 失败，不携带信息
 
 ##  Jackson工具类 com.github.gxhunter.util.JsonUtil
 * **描述**  
    Jackson是spring默认的json序列化工具，但是与fastjson不同，没有内置很方便的方法可以在对象和javabean之间互转，大多数情况你可能会考虑fastjson作为json工具类，但这样有个问题，Jackson的注解在fastjson不再适用，为了解决这个问题，你可能还要侵入式地添加fastjson的注解。
    在同一个项目使用两个json工具也是不提倡的做法，所以这里提供了一个Jackson的封装工具JsonUtil。

 * 默认配置
 ```$xslt
static{
        //只序列化不为null的字段
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //取消默认转换timestamps形式
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,false);
        //忽略空Bean转json的错误
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
        //忽略 在json字符串中存在，但是在java对象中不存在对应属性的情况。防止错误
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
}
```
* 静态方法
    1. 对象转为Json字符串
    ```$xslt
    /**
     * 对象转json
     */
    public static <T> String toJSON(T obj)
    ```
    
    2. 对象转为json数组
    ```$xslt
    /**
     * 对象转json
     */
    public static <T> String toJSON(T obj)
    ```
    
    3. JSON转为java对象
    ```$xslt
    /**
     * JSON转为java对象
     * @param str   json字符串
     * @param clazz java对象类型
     * @param <T>   java对象类型
     * @return
     */
    public static <T> T parse(String str,Class<T> clazz)
    ```
    
## 全局异常处理器 com.github.gxhunter.exception    
* **描述**  
    在企业项目中，后端的错误不能把具体异常抛给前端，需要做异常捕获、分类等。并且根据异常类型返回不同的错误码（通用返回类Result中的status），
    这里默认捕获了所有异常，并对常见的几个异常做分类，使之返回不同的status。
* 分类捕获的异常
    1. **ApiException** 
    自定义的异常，由后端手动抛出，再被异常处理器捕获，把具体信息[code]返回给前端，比如你在java中这样写  
    `throw new ApiException("服务端抛出异常");`  
    前端接收到的结果：
    ```
    {
        status:1000,
        message:"服务端抛出异常",
        success: false
    }
    ```
    
    2. MethodArgumentNotValidException  
    服务端使用jsr303注解作参数校验,校验不通过时，会自动抛出MethodArgumentNotValidException异常，异常处理器捕获了这类校验异常，把校验失败的原因、字段返回给前端，校验失败的status为1001
    ```$xslt
        {
          "status": "1001",
          "message": "参数校验失败",
          "data": {
            "校验失败的字段": "失败原因",
          },
          "success": false
        }
    ```
    
    3. Exception
    其他异常没有被分类，统一返回一下1999状态码
    ```$xslt
        {
          "status": "1999",
          "message": "网络超时",
          "success": false
        }
    ```
* 自定义异常处理器
    
    不想使用内置的异常处理器，或者内置的异常处理器不能满足你的业务需求，只需继承
    com.github.gxhunter.exception.ExceptionResolver
    重写对应的方法即可
* 关于 status  
在上面的常见status是内置的，**具体看下文的ResultEnum介绍**，你也可以实现IResponseCode扩展    
## Redis工具类
com.github.gxhunter.service.IRedisClient
基于Resttemplate做的二次封装

引入spring-boot-data-redis-starter即可使用

## restTemplate
* 使用okhttp代替默认的httpclient作为底层实现
* 具体配置
    在yml中
    ```
    rest:
        readTimeout: 读取超时(默认：5000ms)
        connectTimeout: 连接超时(默认：3000ms)
        writeTimeout: 写入超时(默认：5000ms)
    ```    
## 线程池
在yml中配置（可选）
```$xslt
thread-pool:
  core-pool-size: 默认20 池中所保存的线程数，包括空闲线程。
  maximum-pool-size: 默认100 池中允许的最大线程数。
  keep-alive-time: 默认30 当线程数大于核心时，此为终止前多余的空闲线 程等待新任务的最长时间。
```
使用
```$xslt
@Autowired
private ThreadPoolExecutor mPoolExecutor;
```
## swagger 支持
* 描述  
开发中，我们一般需要swagger来自动生成restapi文档，省去了在postman填写各类参数和url的麻烦，而swagger2官方目前并没有提供自动化装配starter，为了使用swagger2，你不得不编写bean和configuration，为了解决这个繁琐的问题本starter提供了swagger2的自动化装配。
1. 在pom中引入相关依赖，如
```$xslt
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger-ui</artifactId>
    <version>2.9.2</version>
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
## 返回码枚举
com.github.gxhunter.enums.ResultEnum
#### 内置
```$xslt
    SUCCESS(0,"成功"),

    QUERY_FAILURE(1,"查询不到任何内容"),
    CREATE_FAILURE(2,"新建失败"),
    UPDATE_FAILURE(3,"修改失败"),
    DELETE_FAILURE(4,"删除失败"),


    DEFAULT_ERROR(1000,"操作失败"),
    METHOD_ARGUMENT_VALID_FAIL(1001,"参数校验失败"),
    UNKNOW_ERROR(1999,"网络超时"),
```
#### 扩展
默认提供的ResultEnum只是最基础的几个情况，可能无法满足覆盖业务需求，你可以编写一个枚举继承com.github.gxhunter.enums.IResponseCode实现扩展。

## @DefaultWebMVC
* 默认配置一些Javabean的序列化与反序列化格式、时区、过滤的url
* 使用方法：再启动类加上 @DefaultWebMVC注解
 1. json与java互转格式
 
    java对象|json对象
    :--|:--:
    Date|yyyy-MM-dd HH:mm:ss
    LocalDateTime|yyyy-MM-dd HH:mm:ss
    LocalDate|yyyy-MM-dd
    LocalTime|HH:mm:ss
    Long|string
    null|不序列化
> 配置时区为GMT+8
 2. 不拦截swagger相关url
    
## 待续...



 
