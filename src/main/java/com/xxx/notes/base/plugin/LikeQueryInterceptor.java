package com.xxx.notes.base.plugin;

import com.xxx.notes.base.annotation.Like;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.util.*;

/**
 * @ClassName LikeQueryInterceptor
 * @Description 模糊查询时的插件,方法不允许重载,如果重载,必须加@Param注解
 * @Author l17561
 * @Date 2018/11/30 11:18
 * @Version V1.0
 */
@Intercepts({
        @Signature(type = StatementHandler.class,
                method = "prepare",
                args = {Connection.class,
                        Integer.class})
})
public class LikeQueryInterceptor implements Interceptor {

    private String countSuffix = "_COUNT";

    /**
     * 直接覆盖你所拦截的对象
     *
     * @param invocation 通过Invocation对象，可以反射调度原来对象的方法
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        Boolean continues = false;

        StatementHandler statementHandler=(StatementHandler)invocation.getTarget();
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);

        //获取sql
        String sql= String.valueOf(metaObject.getValue("delegate.boundSql.sql")).toUpperCase();

        if (sql.contains(" LIKE ") && ! sql.contains(" BINARY ")) {
            sql = sql.replaceAll(" LIKE ", " LIKE BINARY ");
            continues = true;
        }
        // 将SQL重新放入
        metaObject.setValue("delegate.boundSql.sql", sql);

        // 如果SQL中没有like存在，则不用去替换值了
        if (!continues) {
            return invocation.proceed();
        }

        // 获取到 执行的方法名——格式为：包.类.方法
        String id = (String) metaObject.getValue("delegate.mappedStatement.id");
        if (id.trim().endsWith(countSuffix)){
            id = id.substring(0, id.lastIndexOf(countSuffix));
        }

        // 大概是@Parma注解吧
        /**
         * 经测试，当有Param注解时， 它是MapperMethod.ParamMap，仍未发现特殊情况
         * 没有Param注解时，它是参数的类型，比如java.lang.String、int、com.xxx.notes.entity.UserEntity
         * 当有多个参数时，你没有@Param注解时，看mybatis给你报错不报错？？
         * 所以，这个方法针对String的或者是自定义类的那种
         */
        Object obj = metaObject.getValue("delegate.parameterHandler.parameterObject");

        int i = id.lastIndexOf('.');
        // 当前执行所执行的方法名
        String methodName = id.substring(i + 1);
        // 方法所属的类
        String clazzName = id.substring(0, i);

        // 通过反射，获取到执行的类的class，能走到这一步的话，这里应该不会有异常
        Class<?> clazz = Class.forName(clazzName);

        // 当前类中所有的方法
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {

            if (Objects.equals(methodName, method.getName())) {
                /***
                 * 获取到所有的参数列表
                 * 这里分两种情况来做：
                 * 一、当是String且有@Like的注解时
                 *      这里需强制必须加@Param注解
                 * 二、非基础类型和上面的情况时
                 *      这里就没有什么限制了
                 */
                Parameter[] parameters = method.getParameters();
                Type[] types = method.getGenericParameterTypes();
                for (int j = 0; j < parameters.length; j++){
                    // 获取参数的类型
                    String typeName = types[j].getTypeName();
                    // String + @Like + @Param
                    if (Objects.equals("java.lang.String", typeName)
                            && parameters[j].getAnnotation(Like.class) != null) {

                        Param param = parameters[j].getAnnotation(Param.class);
                        // 无@Param注解
                        if (obj instanceof String && param == null) {
                            obj = replaceSpecialCharacter(String.valueOf(obj));

                        } else if (obj instanceof MapperMethod.ParamMap && param != null) { // 这里应该是不会有特殊情况
                            // 针对用户传入的值进行处理，并放回到对应的位置
                            MapperMethod.ParamMap paramMap = (MapperMethod.ParamMap) obj;
                            String value = param.value();
                            value = replaceSpecialCharacter(String.valueOf(paramMap.get(value)));
                            paramMap.put(param.value(), value);
                            obj = paramMap;
                        } else {
                            // DO NOTHING 目前还没有想到其他情况
                        }
                    } else if (!isBasicTypeAndString(typeName)){ // 非基础类型和String到这里来
                        Object entity = null;
                        Param param = parameters[j].getAnnotation(Param.class);
                        if (obj instanceof MapperMethod.ParamMap && param != null) {
                            MapperMethod.ParamMap paramMap = (MapperMethod.ParamMap) obj;
                            String value = param.value();
                            if (!paramMap.containsKey(value)) {
                                continue;
                            }
                            entity = paramMap.get(value); // 拿到对应的实体
                        } else {
                            entity =obj;
                        }

                        if (!Objects.equals(typeName, obj.getClass().getName())){
                            continue;
                        }

                        /**
                         * 获取到自定义类 参数后，针对所有的属性进行循环，判断是否有@Like注解
                         * 如果没有，就什么也不做，这样其实不大可能，如果有，那就是不过滤呗
                         * 如果有，则将特殊字符过滤并重新赋值给对象
                         */
                        Field[] fields = Class.forName(typeName).getDeclaredFields();
                        for (Field field : fields) {
                            Like like = field.getAnnotation(Like.class);
                            if (like != null) {
                                field.setAccessible(true);
                                Object fieldValue = field.get(entity);
                                if (fieldValue instanceof String) {
                                    field.set(entity, replaceSpecialCharacter((String) fieldValue));
                                }
                            }
                            // DO NOTHING
                        }
                    }
                }
                metaObject.setValue("delegate.parameterHandler.parameterObject", obj);
            }
        }

        // 这里是SQL里面的参数即${} 里面的内容，似乎没用到
        List<ParameterMapping> parameterMappings = (List) metaObject.getValue("delegate.boundSql.parameterMappings");
        // end end end


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

    /**
     * 判断type是否是基础类型，是true、否false
     * @param type
     * @return
     */
    private Boolean isBasicTypeAndString(String type) {
        List types = Arrays.asList(
                "byte", "java.lang.Byte",
                "boolean","java.lang.Boolean",
                "char","java.lang.Character",
                "short","java.lang.Short",
                "int","java.lang.Integer",
                "long","java.lang.Long",
                "float","java.lang.Float",
                "double","java.lang.Double","java.lang.String"
        );
        return types.contains(type);
    }
}
