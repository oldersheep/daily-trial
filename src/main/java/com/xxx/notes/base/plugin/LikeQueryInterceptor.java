package com.xxx.notes.base.plugin;

import com.xxx.notes.base.annotation.Like;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/**
 * @ClassName LikeQueryInterceptor
 * @Description 模糊查询时的插件
 * @Author l17561
 * @Date 2018/11/30 11:18
 * @Version V1.0
 */
@Intercepts({
        @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class}),
//        @Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class})
})
public class LikeQueryInterceptor implements Interceptor {


    /**
     * 直接覆盖你所拦截的对象
     *
     * @param invocation 通过Invocation对象，可以反射调度原来对象的方法
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        // 根据这个来判断query还是prepare，其实是不用的吧
        String name = invocation.getMethod().getName();
        System.out.println(name);

        boolean continues = false;

        StatementHandler statementHandler=(StatementHandler)invocation.getTarget();
        MetaObject metaStatementHandler = SystemMetaObject.forObject(statementHandler);

        //获取sql
        String sql= String.valueOf(metaStatementHandler.getValue("delegate.boundSql.sql")).toUpperCase();

        if (sql.contains(" LIKE ") && ! sql.contains(" BINARY ")) {
            sql = sql.replaceAll(" LIKE ", " LIKE BINARY ");
            continues = true;
        }
        metaStatementHandler.setValue("delegate.boundSql.sql",sql);

        if (!continues) {
            // 执行目标方法
            return invocation.proceed();
        }

        // TODO try try try
        // 执行的方法名，包.类.方法
        String id = (String) metaStatementHandler.getValue("delegate.mappedStatement.id");
        // 大概是@Parma注解吧
        Object obj = metaStatementHandler.getValue("delegate.boundSql.parameterObject");

        int i = id.lastIndexOf('.');
        String methodName = id.substring(i + 1);
        String clazzName = id.substring(0, i);
        Class<?> clazz = Class.forName(clazzName);
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (Objects.equals(methodName, method.getName())) {
                Parameter[] parameters = method.getParameters();
                Type[] types = method.getGenericParameterTypes();
                for (int j = 0; j < parameters.length; j++){
                    // 获取参数的类型
                    String typeName = types[j].getTypeName();
                    if (Objects.equals("java.lang.String", typeName)
                            && parameters[j].getAnnotation(Like.class) != null) {

                        Param param = parameters[j].getAnnotation(Param.class);
                        // 无@Param注解 TODO 有问题
                        if (obj instanceof String) {
                            obj = replaceSpecialCharacter(String.valueOf(obj));

                        } else if (obj instanceof MapperMethod.ParamMap) {
                            MapperMethod.ParamMap paramMap = (MapperMethod.ParamMap) obj;

                            String value = replaceSpecialCharacter(String.valueOf(paramMap.get(param.value())));
                            paramMap.put(param.value(), value);
                            obj = paramMap;
                        }
                    } else {
                        Object entity = null;
                        Param param = parameters[j].getAnnotation(Param.class);
                        if ( param != null) {
                            MapperMethod.ParamMap paramMap = (MapperMethod.ParamMap) obj;
                            String value = param.value();
                            entity = paramMap.get(value);
                        } else {
                            entity =obj;
                        }
                        Field[] fields = Class.forName(typeName).getDeclaredFields();
                        for (Field field : fields) {
                            Like like = field.getAnnotation(Like.class);
                            if (like != null) {
                                field.setAccessible(true);
                                Object fieldValue = field.get(entity);
                                if (fieldValue instanceof String) {
                                    field.set(entity, replaceSpecialCharacter((String) fieldValue));
                                }
                                // DO NOTHING
                            }

                        }
                    }
                }
                metaStatementHandler.setValue("delegate.boundSql.parameterObject", obj);
            }
        }

        // 这里是SQL里面的参数即${} 里面的内容
        List<ParameterMapping> parameterMappings = (List) metaStatementHandler.getValue("delegate.boundSql.parameterMappings");


        // TODO end end end


        // 执行目标方法
        return invocation.proceed();
    }

    /**
     * 给被拦截对象生成一个代理对象
     *
     * @param target 被拦截的对象
     * @return
     */
    @Override
    public Object plugin(Object target) {

        Object proxy = Plugin.wrap(target,this);

        return proxy;
    }

    /**
     * plugin元素中配置所需参数，该方法在插件初始化的时候会被调用一次
     *
     * @param properties
     */
    @Override
    public void setProperties(Properties properties) {
        System.out.println("setProperties---");
    }

    /**
     * 替换特殊字符
     * @param word
     * @return
     */
    private String replaceSpecialCharacter(String word) {

        return word.replaceAll("_", "\\\\_")
                .replaceAll("%", "\\\\%");
    }
}
