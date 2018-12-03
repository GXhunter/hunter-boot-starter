# hunter-spring-boot-starter
提供以下内置工具和对象
1. 通用返回类Result
2. 全局异常处理
3. Jackson工具类
4. StringUtil工具
5. Redis工具类
6. RestTemplate工具类
---
## 依赖
1. springboot
~~2. mybatis-plus~~
## 快速开始
1. 下载源码  
`git clone git@github.com:GXhunter/hunter-boot-starter.git`
2. 安装到本地仓库/部署到私服  
`mvn clean install`或`mvn clean deploy`
3. 引入上述依赖包  
4. 引入本项目  
```$xslt
    <groupId>com.github.gxhunter</groupId>
    <artifactId>hunter-spring-boot-starter</artifactId>
    <version>version</version>
```
##  通用返回类com.github.gxhunter.dto.Result<T>
 * 字段描述
 1. status  状态码
 2. message 返回信息
 3. data    携带数据
 * 方法描述
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
 7.failed()
 失败，不携带信息
 
 ##  Jackson工具类 com.github.gxhunter.util.JsonUtil
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
* 分类捕获的异常
    1. ApiException 
    由服务端手动throw的异常，把具体信息[code]返回给前端
    
    2. MethodArgumentNotValidException
    参数校验失败,返回如下格式
    ```$xslt
        {
          "status": "1001",
          "message": "参数校验失败",
          "data": {
            "subitemName": "不能为null",
          },
          "success": false
        }
    ```
    
    3. Exception
    其他异常，返回
    ```$xslt
        {
          "status": "1999",
          "message": "网络超时",
          "success": false
        }
    ```
* 自定义异常处理器
    
    不想使用内置的异常处理器，继承
    com.github.gxhunter.exception.ExceptionResolver
    重写对应的方法即可
## Redis工具类
com.github.gxhunter.service.IRedisClient
基于Resttemplate做的二次封装

引入spring-boot-data-redis-starter即可使用

## restTemplate
* 使用okhttp代替默认的httpclient作为底层实现
* 具体配置
    在yml中
    ```
    ribbon:
        readTimeout: 读取超时(默认：5000ms)
        connectTimeout: 连接超时(默认：3000ms)
        writeTimeout: 写入超时(默认：5000ms)
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
继承com.github.gxhunter.enums.IErrorCode

## 未完待续...



 
