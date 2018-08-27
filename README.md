# daily-trial 日常积累尝试

> 此项目采用了spring boot当前最新版本，主要是为了整合日常工作中发现的新内容，然后自己感兴趣的部分也进行一下融合整理，应该会不定期更新，spring boot的版本应该不会太变。



## 项目初始搭建

​	项目初始搭建时就是为了尝试spring boot使用yml的方式整合mybatis，最初的时候遇见了几个问题，如下》》》》》》》》》》》》》》》》》》》》

### 通用Mapper的查询结果无法映射到实体

​	最初项目中引入了很多个类似的依赖，特别是关于MyBatis的，下面两个，是当前项目中都用到了的。

```xml
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
    <version>1.3.2</version>
</dependency>

<!-- 通用mapper插件 -->
<dependency>
    <groupId>tk.mybatis</groupId>
    <artifactId>mapper-spring-boot-starter</artifactId>
    <version>2.0.2</version>
</dependency>
```

而在写mybatis的config类时，`@MapperScan`注解所需导入的是`tk.mybatis`的，并非`org.mybatis`，这个错误困扰了我好久好久，当然可能也不是很常见，记录一下吧。



### 使用Spring Boot的方式进行MyBatis的配置

​	以前使用Spring整合MyBatis的时候，很多配置都写在xml中，比如驼峰转换，而且个人有很强的强迫症，不愿意再使用xml，因此，这些配置，如何进行注入呢，其实在yml文件中，mybatis.configuration下面很多的配置信息，这里面的都可以进行注入到`org.apache.ibatis.session.Configuration`中，方式如下：

```java
@Configuration
public class MybatisConfiguration {

	.....
	
    @Bean
    @ConfigurationProperties(prefix = "mybatis.configuration")
    public org.apache.ibatis.session.Configuration globalConfiguration() {
        return new org.apache.ibatis.session.Configuration();
    }
	
	.....

}
```



### 加载自定义的yml文件

​	可能是从SSM那里带来的坏习惯，喜欢将不同的配置放在不同的文件下，这点不太好，而且有个朋友也说我傻逼，纠结的地方不对，这个是在我要放弃的时候，突然找到的，首先，Spring Boot是提供了默认加载工厂`DefaultPropertySourceFactory`，自己可以根据进行扩展，如下：代码是从网上拿的，具体还没参透。

```java
public class YmlPropertyFactory extends DefaultPropertySourceFactory {

    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource encodedResource) throws IOException {
        if (encodedResource == null) {
            return super.createPropertySource(name, encodedResource);
        } else {
            return name != null ? new YamlPropertySourceLoader().load(name, encodedResource.getResource()).get(0) :
                    new YamlPropertySourceLoader().load(encodedResource.getResource().getFilename(), encodedResource.getResource()).get(0);
        }
    }
}
```

​	使用的时候`@PropertySource(value = "classpath:redis.yml", factory = YmlPropertyFactory.class)`即可注入并使用其中配置的内容。



## 整合SwaggerUI

​	Swagger就是一个类似于Postman的框架，可以帮忙生成API的文档，配置也比较简单，这个项目里，为了测试自定义的properties与yml如何加载，这些都可以不必在意，一个配置类，一些注解，没有什么特殊的。



## 使用Jedis整合Redis

​	没有使用ShardedJedis 进行整合，二者区别不是很大，只是ShardedJedis去掉了一些原生操作，这里整合没有什么特别的地方，使用JedisTemplate还未成功，而且好像也比较麻烦。

### 自定义注解保存到Redis，以及动态指定Key

​	保存到    `@SaveRedis`的注解，这个是作用在方法级别的注解，标注此方法的结果会保存在Redis中，Key值默认为KEY_，如果参数标有`@Key`的注解，那么这个参数的值，会拼接在默认注解后面，作为整体的key值，这里只是一个普遍的使用方式，并未做任何特殊性扩展。具体可参照`com.xxx.notes.base.aspect.RedisAspect`。



## 权限认证

​	使用用户名生成token，然后二者进行双向认证，使用Redis缓存，这个是整合别人的，感觉一般，这里出现了一个问题，感觉这个问题的价值，比这个强多了。

具体步骤如下：

* 声明一个注解，方法级别的，一旦被此注解标注的方法，都会检测Redis中的token与username是否有效

* 定义一个过滤器，在过滤器里面进行上步所讲的检测

* 实现`WebMvcConfigurer`，添加权限拦截

  * 正是这里，出现了问题，具体看下面的代码

    ```java
    @Configuration
    public class WebAppConfiguration implements WebMvcConfigurer {
    
        // 让spring进行管理，会将依赖的类进行注入
        @Bean
        public AuthorizationInterceptor authorization() {
            return new AuthorizationInterceptor();
        }
    
        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            // 这样会报错，因为这个类里面进行了其他的注入，如果自己new，并不能放入到sprig的容器
            // 且相关的类也不会进行注入
            // registry.addInterceptor(new AuthorizationInterceptor()).addPathPatterns("/**");
    
            registry.addInterceptor(authorization()).addPathPatterns("/**");
        }
    }
    ```

思路还是蛮好的。



## 加入MyBatis-Generator

* 主要的配置文件在src/main/resources/mybatis下面
* 运行命令为 mvn mybatis-generator:generate

