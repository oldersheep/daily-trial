package com.xxx.notes.base.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.xxx.notes.base.annotations.AuthToken;
import com.xxx.notes.base.constant.Constant;
import com.xxx.notes.base.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;

/**
 * @ClassName AuthorizationInterceptor
 * @Description 权限认证拦截器
 * @Author l17561
 * @Date 2018/8/9 9:30
 * @Version V1.0
 */
public class AuthorizationInterceptor implements HandlerInterceptor {

    private Logger logger = LoggerFactory.getLogger(AuthorizationInterceptor.class);

    @Autowired
    private RedisService redisService;

    // 存放鉴权信息的Header名称，默认是Authorization
    private String httpHeaderName = "Authorization";

    // 鉴权失败后返回的错误信息，默认为401 unauthorized
    private String unauthorizedErrorMessage = "401 unauthorized";

    // 鉴权失败后返回的HTTP错误码，默认为401
    private int unauthorizedErrorCode = HttpServletResponse.SC_UNAUTHORIZED;

    // 存放登录用户模型Key的Request Key
    public static final String REQUEST_CURRENT_KEY = "REQUEST_CURRENT_KEY";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        if (method.getAnnotation(AuthToken.class) != null || handlerMethod.getBeanType().getAnnotation(AuthToken.class) != null) {

            String token = request.getHeader(httpHeaderName);
            logger.info("获取到token值为{}", token);

            String username = "";
            if (token != null && token.length() != 0) {

                username = redisService.get(token);
                logger.info("username is {}", username);
            }

            if (username != null && !username.trim().equals("")) {
                Long tokeBirthTime = Long.valueOf(redisService.get(token + username));
                Long diff = System.currentTimeMillis() - tokeBirthTime;
                if (diff > Constant.TOKEN_RESET_TIME) {
                    redisService.expire(username, Constant.TOKEN_EXPIRE_TIME);
                    redisService.expire(token, Constant.TOKEN_EXPIRE_TIME);
                    logger.info("Reset Expire Time...");
                    redisService.set(username + token, System.currentTimeMillis() + "");
                }
                request.setAttribute(REQUEST_CURRENT_KEY, username);
                return true;
            } else {
                JSONObject jsonObject = new JSONObject();
                PrintWriter out = null;
                try {
                    response.setStatus(unauthorizedErrorCode);
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

                    jsonObject.put("code", response.getStatus());
                    jsonObject.put("message", HttpStatus.UNAUTHORIZED);
                    out = response.getWriter();
                    out.println(jsonObject);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("不应该有异常的-_-", e);
                } finally {
                    if (out != null) {
                        out.flush();
                        out.close();
                    }
                }
                return false;
            }
        }

        request.setAttribute(REQUEST_CURRENT_KEY, null);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
