package com.xxx.notes.base.plugin;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
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
        @Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class})
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
        StatementHandler statementHandler=(StatementHandler)invocation.getTarget();
        MetaObject metaStatementHandler = SystemMetaObject.forObject(statementHandler);

        //获取sql
        String sql= String.valueOf(metaStatementHandler.getValue("delegate.boundSql.sql")).toUpperCase();

        List<ParameterMapping> parameterMappings = (List) metaStatementHandler.getValue("delegate.boundSql.parameterMappings");
        for (ParameterMapping parameterMapping : parameterMappings) {
            System.out.println(parameterMapping.getProperty());
        }
        String name = invocation.getMethod().getName();
        System.out.println(name);

        if (sql.contains(" LIKE ") && ! sql.contains(" BINARY ")) {
            sql = sql.replaceAll(" LIKE ", " LIKE BINARY ");
        }

        metaStatementHandler.setValue("delegate.boundSql.sql",sql);
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

    private MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null && ms.getKeyProperties().length > 0) {
            builder.keyProperty(ms.getKeyProperties()[0]);
        }
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.resultSetType(ms.getResultSetType());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());
        return builder.build();
    }

    public static class BoundSqlSqlSource implements SqlSource {
        private BoundSql boundSql;
        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }
        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }
    }
}
