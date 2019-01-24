package com.xxx.notes.base.util;

import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;

/**
 * @ClassName SpELParseUtils
 * @Description 解析SpEL表示式
 * @Author l17561
 * @Date 2019/1/24 16:13
 * @Version V1.0
 */
public class SpELParseUtils {

    public static String parse(String SpEL, Method method, Object[] args) {
        //获取被拦截方法参数名列表(使用Spring支持类库)
        LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
        String[] paraNameArr = u.getParameterNames(method);
        //使用SpEL进行key的解析
        ExpressionParser parser = new SpelExpressionParser();
        //SpEL上下文
        StandardEvaluationContext context = new StandardEvaluationContext();
        //把方法参数放入SpEL上下文中
        for (int i = 0; i < paraNameArr.length; i++) {
            context.setVariable(paraNameArr[i], args[i]);
        }
        return parser.parseExpression(SpEL).getValue(context, String.class);
    }

    /**
     * 支持 #p0 参数索引的表达式解析
     *
     * @param rootObject 根对象,method 所在的对象
     * @param SpEL       表达式
     * @param method     ，目标方法
     * @param args       方法入参
     * @return 解析后的字符串
     */
    public static String parse(Object rootObject, String SpEL, Method method, Object[] args) {
        //获取被拦截方法参数名列表(使用Spring支持类库)
        LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
        String[] paraNameArr = u.getParameterNames(method);
        //使用SpEL进行key的解析
        ExpressionParser parser = new SpelExpressionParser();
        //SpEL上下文
        StandardEvaluationContext context = new MethodBasedEvaluationContext(rootObject, method, args, u);
        //把方法参数放入SpEL上下文中
        for (int i = 0; i < paraNameArr.length; i++) {
            context.setVariable(paraNameArr[i], args[i]);
        }
        return parser.parseExpression(SpEL).getValue(context, String.class);
    }
}
