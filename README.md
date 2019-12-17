### 1.构建MVC环境
```java
package com.imooc.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-12-15
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    /**
     * 在所有方法执行之前执行
     * 构造mvc环境
     */
    @Before
    public void setup(){
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void user() throws Exception {
        // 构建http请求
        mockMvc.perform(MockMvcRequestBuilders.get("/user") // 请求地址
                .param("userName","jiaojiao")
                .contentType(MediaType.APPLICATION_JSON_UTF8)) // content-type: application/json
                .andExpect(MockMvcResultMatchers.status().isOk()) // 期待的请求结果状态码 200
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(3));// 期待的请求结果长度为 3
    }
}
```

表达式
---------

| Operator                  | Description                                                        |
| :------------------------ | :----------------------------------------------------------------- |
| `$`                       | The root element to query. This starts all path expressions.       |
| `@`                       | The current node being processed by a filter predicate.            |
| `*`                       | Wildcard. Available anywhere a name or numeric are required.       |
| `..`                      | Deep scan. Available anywhere a name is required.                  |
| `.<name>`                 | Dot-notated child                                                  |
| `['<name>' (, '<name>')]` | Bracket-notated child or children                                  |
| `[<number> (, <number>)]` | Array index or indexes                                             |
| `[start:end]`             | Array slice operator                                               |
| `[?(<expression>)]`       | Filter expression. Expression must evaluate to a boolean value.    |


方法
---------

函数可以在路径的末尾调用——函数的输入是路径表达式的输出。  
函数输出由函数本身决定。

| Function                  | Description                                                         | Output    |
| :------------------------ | :------------------------------------------------------------------ |-----------|
| min()                     | Provides the min value of an array of numbers                       | Double    |
| max()                     | Provides the max value of an array of numbers                       | Double    |
| avg()                     | Provides the average value of an array of numbers                   | Double    |
| stddev()                  | Provides the standard deviation value of an array of numbers        | Double    |
| length()                  | Provides the length of an array                                     | Integer   |
| sum()                     | Provides the sum value of an array of numbers                       | Double    |


Filter Operators
-----------------

筛选器是用于筛选数组的逻辑表达式。一个典型的过滤器应该是`[?(@.age > 18)]` 其中 `@`表示当前正在处理的项目。可以使用逻辑运算符`&&` 和 `||`创建更复杂的过滤器.字符串文本必须用单引号或双引号括起来 (`[?(@.color == 'blue')]` or `[?(@.color == "blue")]`).   

| Operator                 | Description                                                           |
| :----------------------- | :-------------------------------------------------------------------- |
| ==                       | left is equal to right (note that 1 is not equal to '1')              |
| !=                       | left is not equal to right                                            |
| <                        | left is less than right                                               |
| <=                       | left is less or equal to right                                        |
| >                        | left is greater than right                                            |
| >=                       | left is greater than or equal to right                                |
| =~                       | left matches regular expression  [?(@.name =~ /foo.*?/i)]             |
| in                       | left exists in right [?(@.size in ['S', 'M'])]                        |
| nin                      | left does not exists in right                                         |
| subsetof                 | left is a subset of right [?(@.sizes subsetof ['S', 'M', 'L'])]       |
| anyof                    | left has an intersection with right [?(@.sizes anyof ['M', 'L'])]     |
| noneof                   | left has no intersection with right [?(@.sizes noneof ['M', 'L'])]    |
| size                     | size of left (array or string) should match right                     |
| empty                    | left (array or string) should be empty                                |

### 2.@JsonView控制在不同的视图下 显示不同的字段
```
1.使用接口来声明多个视图
2.在值对象的get方法上指定视图
3.在Controller的方法上指定视图
```
User
```java
package com.imooc.dto;

import com.fasterxml.jackson.annotation.JsonView;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-12-15
 */
public class User {
    public interface UserSimpleView {};
    public interface UserDetailView extends UserSimpleView {};

    private String username;
    private String password;

    @JsonView(UserSimpleView.class)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    @JsonView(UserDetailView.class)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
```
UserController
```java
package com.imooc.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.imooc.dto.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-12-15
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    
    @GetMapping
    @JsonView(User.UserSimpleView.class)
    public List<User> user(@RequestParam String userName){
        log.info("【userName】{}",userName);
        List<User> userList = new ArrayList<>();
        userList.add(new User());
        userList.add(new User());
        userList.add(new User());
        return userList;
    }

    @GetMapping("/{id:\\d+}")
    @JsonView(User.UserDetailView.class)
    public User getInfo(@PathVariable("id") String id){
        log.info("【id】=>{}", id);
        User user = new User();
        user.setUsername("lovingliu");
        user.setPassword("123");
        return user;
    }
}
```
### 3.RESTful API的校验

`@Valid`  被注释的元素是一个对象，需要检查此对象的所有字段值  
`@Null` 被注释的元素必须为 null  
`@NotNull`	被注释的元素必须不为 null  
`@AssertTrue`	被注释的元素必须为 true  
`@AssertFalse`	被注释的元素必须为 false  
`@Min(value)`	被注释的元素必须是一个数字，其值必须大于等于指定的最小值  
`@Max(value)`	被注释的元素必须是一个数字，其值必须小于等于指定的最大值  
`@DecimalMin(value)`	被注释的元素必须是一个数字，其值必须大于等于指定的最小值  
`@DecimalMax(value)`	被注释的元素必须是一个数字，其值必须小于等于指定的最大值  
`@Size(max, min)`	被注释的元素的大小必须在指定的范围内  
`@Digits (integer, fraction)`	被注释的元素必须是一个数字，其值必须在可接受的范围内  
`@Past`	被注释的元素必须是一个过去的日期  
`@Future`	被注释的元素必须是一个将来的日期  
`@Pattern(value)`	被注释的元素必须符合指定的正则表达式

**自定义校验**
1.创建自定义Annotation
```java
package com.imooc.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author：LovingLiu
 * @Description: 自定义参数校验逻辑
 * @Date：Created in 2019-12-16
 */
@Target({ElementType.FIELD,ElementType.METHOD}) // 可以添加在什么类型上
@Retention(RetentionPolicy.RUNTIME) // 起作用的时机
@Constraint(validatedBy = MyConstraintValidator.class) // 实现的逻辑（要执行的校验逻辑）
public @interface MyConstraint {
    /**
     * 自定义校验器必须含有以下三个属性
     */
    String message() default "自定义校验逻辑验证未通过";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
```
2.创建自定义Annotation所执行的校验逻辑
```java
package com.imooc.validator;

import com.imooc.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @Author：LovingLiu
 * @Description: Spring 会将 实现了ConstraintValidator接口的类自动注入到容器中。不用@Component
 * @Date：Created in 2019-12-16
 */
public class MyConstraintValidator implements ConstraintValidator<MyConstraint,Object> {
    /**
     * 设置可以获取 Spring管理的所有的类
     */
    @Autowired
    private HelloService helloService;

    /**
     * 校验器初始化
     * @param constraintAnnotation
     */
    @Override
    public void initialize(MyConstraint constraintAnnotation) {
        System.out.println("my validator init...");
    }

    /**
     *
     * @param value 要校验的值
     * @param context 校验的上下文
     * @return
     */
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        String result = helloService.sayHello(value.toString());
        System.out.println("isValid:"+result);
        return false;// 是否通过验证
    }
}
```
3.在需要校验的地方使用
```java
package com.imooc.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.imooc.validator.MyConstraint;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.util.Date;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-12-15
 */
public class User {
    public interface UserSimpleView {};
    public interface UserDetailView extends UserSimpleView {};

    private String id;
    @MyConstraint // 自定义验证
    private String username;
    @NotBlank
    private String password;
    @Past(message = "生日必须是过去的时间")
    private Date birthday;

    @JsonView(UserSimpleView.class)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    @JsonView(UserDetailView.class)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    @JsonView(UserDetailView.class)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    @JsonView(UserSimpleView.class)
    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
}
```
### 4.自定义异常处理处理
当系统访问一个不存在的URL地址时,对于浏览器和APP（其他）访问方式的处理方式是不一致的。
创建`resources/error/404.html`也是仅仅对浏览器发生的404请求的处理方式。
`com.imooc.exception.UserException`
```java
package com.imooc.exception;

import lombok.Data;

/**
 * @Author：LovingLiu
 * @Description: 自定义异常
 * @Date：Created in 2019-12-16
 */
@Data
public class UserException extends RuntimeException {

    private Integer id;
    public UserException(String msg,Integer id){
        super(msg);
        this.id = id;
    }
}
```
`com.imooc.handle.UserExceptionHandle`
```java
package com.imooc.handle;

import com.imooc.exception.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author：LovingLiu
 * @Description: 统一异常
 * @Date：Created in 2019-12-16
 */
@ControllerAdvice
public class UserExceptionHandle {
     @ExceptionHandler(value = UserException.class)
     @ResponseBody
     @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
     public Map<String,Object> handleUserException(UserException e){
         Map<String,Object> result = new HashMap<>();
         result.put("id",e.getId());
         result.put("message",e.getMessage());
         return result;
     }
}
```
### 5.Filter,Inreceptor,Aop
**Filter**  
1.拦截所有请求  
com.imooc.filter.TimeFilter
```java
package com.imooc.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

/**
 * @Author：LovingLiu
 * @Description: 自定义过滤器
 * @Date：Created in 2019-12-16
 */
@Component
@Slf4j
public class TimeFilter implements Filter {
    /**
     * 指定过滤器逻辑
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("time filter start");
        long start = System.currentTimeMillis();
        chain.doFilter(request,response);
        log.info("filter spend time {}",System.currentTimeMillis() - start);
        log.info("time filter finish");
    }

    /**
     * 初始化
     * @param filterConfig
     * @throws ServletException
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("time filter init");
    }

    /**
     * 销毁
     */
    @Override
    public void destroy() {
        log.info("time filter destory");
    }
}
```
2.配置拦截指定url  
com.imooc.filter.TimeFilter2
```java
package com.imooc.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import java.io.IOException;

/**
 * @Author：LovingLiu
 * @Description: 自定义过滤器
 * @Date：Created in 2019-12-16
 */
@Slf4j
public class TimeFilter2 implements Filter {
    /**
     * 指定过滤器逻辑
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("time filter2 start");
        long start = System.currentTimeMillis();
        chain.doFilter(request,response);
        log.info("filter2 spend time {}",System.currentTimeMillis() - start);
        log.info("time filter2 finish");
    }

    /**
     * 初始化
     * @param filterConfig
     * @throws ServletException
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("time filter2 init");
    }

    /**
     * 销毁
     */
    @Override
    public void destroy() {
        log.info("time filter2 destory");
    }
}
```
com.imooc.config.WebCongfig
```java
package com.imooc.config;

import com.imooc.filter.TimeFilter2;
import com.imooc.interceptor.TimeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author：LovingLiu
 * @Description: 不继承Filter接口 实现Filter拦截器(可适用于第三方Filter)
 * @Date：Created in 2019-12-16
 */
@Configuration
public class WebCongfig {
    /**
     * Filter 设置
     * @return
     */
    @Bean
    public FilterRegistrationBean timeFilter2() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        TimeFilter2 timeFilter2 = new TimeFilter2();
        registrationBean.setFilter(timeFilter2);
        List<String> urls = new ArrayList<>();
        urls.add("/*"); // 指定拦截的url列表
        registrationBean.setUrlPatterns(urls);
        return registrationBean;
    }
}
```
**Inteceptor**
　　1、拦截器执行顺序是按照Spring配置文件中定义的顺序而定的。  

　　2、会先按照顺序执行所有拦截器的preHandle方法，一直遇到return false为止，比如第二个preHandle方法是return false，则第三个以及以后所有拦截器都不会执行。若都是return true，则按顺序加载完preHandle方法。  

　　3、然后执行主方法（自己的controller接口），若中间抛出异常，则跟return false效果一致，不会继续执行postHandle，只会倒序执行afterCompletion方法。  

　　4、在主方法执行完业务逻辑（页面还未渲染数据）时，按倒序执行postHandle方法。若第三个拦截器的preHandle方法return false，则会执行第二个和第一个的postHandle方法和afterCompletion（postHandle都执行完才会执行这个，也就是页面渲染完数据后，执行after进行清理工作）方法。（postHandle和afterCompletion都是倒序执行）  
1.创建拦截器 `com.imooc.interceptor.TimeInterceptor` 
```java
package com.imooc.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @Author：LovingLiu
 * @Description: 时间拦截器 （Interceptor解决的是 无法Spring管理 因为符合的是J2EE的规范, 无法知道控制器信息）
 * @Date：Created in 2019-12-16
 */
@Component
@Slf4j
public class TimeInterceptor implements HandlerInterceptor {
    /**
     * 控制器（Controller）方法调用之前会被调用
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("preHandle");
        log.info("methodClassName=>{}",((HandlerMethod) handler).getBean().getClass().getName());
        log.info("methodName=>{}",((HandlerMethod) handler).getMethod().getName());
        request.setAttribute("startTime", new Date().getTime());
        return true;
    }

    /**
     * 控制器（Controller）方法调用成功之后会被调用
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("postHandle");
        Long start = (Long) request.getAttribute("startTime");
        log.info("time interceptor 耗时:{}", new Date().getTime() - start);
    }

    /**
     * 控制器（Controller）方法调用之后会被调用（无论成功或者失败）
     * 注意:@ControllerAdvice声明的统一异常处理是在afterCompletion 之前的。所以发生的异常如果被@ControllerAdvice声明的异常处理类处理,无法捕获。ex 为null
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("afterCompletion");
        Long start = (Long) request.getAttribute("startTime");
        log.info("afterCompletion 耗时:{}", start);
    }
}
```
2.注入到Spring中
```java
package com.imooc.config;

import com.imooc.filter.TimeFilter2;
import com.imooc.interceptor.TimeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebCongfig extends WebMvcConfigurationSupport {
    @Autowired
    private TimeInterceptor timeInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(timeInterceptor);
    }
}
```
**Aspect**
创建切点和切面 `com.imooc.aspect.TimeAspect`
```java
package com.imooc.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author：LovingLiu
 * @Description: 解决Inteceptor无法拿到请求参数（User user）user无法拿到
 * @Date：Created in 2019-12-16
 */
@Aspect
@Component
@Slf4j
public class TimeAspect {
    @Pointcut("execution(public * com.imooc.controller.UserController.* (..))")
    public void timePointcut() {}

    @Around("timePointcut()")
    public Object handleControllerMethod(ProceedingJoinPoint pjp) throws Throwable{
        log.info("time aspect start");
        Object[] args = pjp.getArgs();// 调用被拦击的控制器的方法时 传递的参数
        for (Object obj: args) {
            log.info("arg : {}", obj);
        }
        long start = new Date().getTime();
        Object result = pjp.proceed();// 调用被拦击的控制器的方法
        log.info("result: {}",result);
        log.info("Aspect 耗时: {}",(new Date().getTime() - start));
        log.info("time aspect end");
        return result;
    }
}
```
#### 它们之间的差别
过滤器（Filter）         ：可以拿到原始的http请求，但是拿不到你请求的控制器和请求控制器中的方法的信息。  

拦截器（Interceptor）    ：可以拿到你请求的控制器和方法，却拿不到请求方法的参数。  

切片  （Aspect）         :  可以拿到方法的参数，但是却拿不到http请求和响应的对象。  

```
com.imooc.filter.TimeFilter2             : time filter2 start
com.imooc.filter.TimeFilter              : time filter start
com.imooc.interceptor.TimeInterceptor    : preHandle
com.imooc.interceptor.TimeInterceptor    : methodClassName=>com.imooc.controller.UserController$$EnhancerBySpringCGLIB$$95854c15
com.imooc.interceptor.TimeInterceptor    : methodName=>getInfo
com.imooc.aspect.TimeAspect              : time aspect start
com.imooc.aspect.TimeAspect              : arg : 1
com.imooc.controller.UserController      : 【id】=>1
com.imooc.aspect.TimeAspect              : result: com.imooc.dto.User@5af5ec7
com.imooc.aspect.TimeAspect              : Aspect 耗时: 4
com.imooc.aspect.TimeAspect              : time aspect end
com.imooc.interceptor.TimeInterceptor    : postHandle
com.imooc.interceptor.TimeInterceptor    : time interceptor 耗时:72
com.imooc.interceptor.TimeInterceptor    : afterCompletion
com.imooc.interceptor.TimeInterceptor    : afterCompletion 耗时:1576511855244
com.imooc.filter.TimeFilter              : filter spend time 90
com.imooc.filter.TimeFilter              : time filter finish
com.imooc.filter.TimeFilter2             : filter2 spend time 90
com.imooc.filter.TimeFilter2             : time filter2 finish
```

### 6.RESTFul异步改造 提高系统吞吐量
1.传统同步
```java
package com.imooc.async;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.Callable;

/**
 * @Author：LovingLiu
 * @Description: 异步处理请求
 * @Date：Created in 2019-12-17
 */
@RestController
@RequestMapping("/order")
@Slf4j
public class AsyncController {
    @GetMapping
    public String order() throws Exception{
        log.info("主线程开始");
        Thread.sleep(1000);
        log.info("主线程结束");
        return "success";
    }
}
```
2.Callable 接口实现异步处理
com.imooc.async.AsyncController
```java
package com.imooc.async;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.Callable;

/**
 * @Author：LovingLiu
 * @Description: 异步处理请求
 * @Date：Created in 2019-12-17
 */
@RestController
@RequestMapping("/order")
@Slf4j
public class AsyncController {
    @GetMapping("/orderAsync")
    public Callable<String> orderAsync() throws Exception{
        log.info("主线程开始");
        Callable<String> result = new Callable<String>() {
            @Override
            public String call() throws Exception {
                log.info("副线程开始");
                Thread.sleep(1000);// 模拟下单的业务逻辑
                log.info("副线程返回");
                return "success";
            }
        };
        log.info("主线程结束");
        return result;
    }
}
```
3.DeferredResult 接口实现异步处理
`com.imooc.async.AsyncController`
```java
package com.imooc.async;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.Callable;

/**
 * @Author：LovingLiu
 * @Description: 异步处理请求
 * @Date：Created in 2019-12-17
 */
@RestController
@RequestMapping("/order")
@Slf4j
public class AsyncController {

    @Autowired
    private MockQueue mockQueue;
    @Autowired
    private DeferredResultHolder deferredResultHolder;

    @GetMapping("/orderDeferred")
    public DeferredResult <String> orderDeferred() throws Exception{
        log.info("主线程开始");
        String orderNo = RandomStringUtils.randomNumeric(8); // 生成订单号
        mockQueue.setPlaceOrder(orderNo); // 放入消息队列
        DeferredResult<String> result = new DeferredResult<>();
        deferredResultHolder.getMap().put(orderNo,result);
        log.info("主线程结束");
        return result;
    }
}
```
`com.imooc.async.MockQueue`
```java
package com.imooc.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author：LovingLiu
 * @Description: 模拟消息队列
 * @Date：Created in 2019-12-17
 */
@Component
@Slf4j
public class MockQueue {
    private String placeOrder; // 下单的消息
    private String completeOrder; // 订单完成的消息

    public String getPlaceOrder() {
        return placeOrder;
    }

    public void setPlaceOrder(String placeOrder){
        new Thread(()-> {
            log.info("接到下单请求, {}", placeOrder);
            try {
                Thread.sleep(1000);
            }catch (Exception e){
                e.printStackTrace();
            }

            this.completeOrder = placeOrder;
            log.info("下单请求处理完毕, {}", placeOrder);
        }).start();

    }

    public String getCompleteOrder() {
        return completeOrder;
    }

    public void setCompleteOrder(String completeOrder) {
        this.completeOrder = completeOrder;
    }
}
```
`com.imooc.async.QueueListener`
```java
package com.imooc.async;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @Author：LovingLiu
 * @Description: ContextRefreshedEvent spring容器初始化完成的事件
 * @Date：Created in 2019-12-17
 */
@Component
@Slf4j
public class QueueListener implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private MockQueue mockQueue;
    @Autowired
    private DeferredResultHolder deferredResultHolder;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        new Thread(() -> {
            while (true){
                if(StringUtils.isNotBlank(mockQueue.getCompleteOrder())){
                    String orderNo = mockQueue.getCompleteOrder();
                    log.info("返回订单处理结果:{}",orderNo);
                    deferredResultHolder.getMap().get(orderNo).setResult("place order success");
                    mockQueue.setCompleteOrder(null);
                }else {
                    try {
                        Thread.sleep(100);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        }).start();

    }
}
```
`com.imooc.async.DeferredResultHolder`
```java
package com.imooc.async;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author：LovingLiu
 * @Description: 订单处理结果 Map<key, info>   key: 订单号 info:订单处理信息
 * @Date：Created in 2019-12-17
 */
@Component
public class DeferredResultHolder {
    private Map<String, DeferredResult<String>> map = new HashMap<>();

    public Map<String, DeferredResult<String>> getMap() {
        return map;
    }

    public void setMap(Map<String, DeferredResult<String>> map) {
        this.map = map;
    }
}
```