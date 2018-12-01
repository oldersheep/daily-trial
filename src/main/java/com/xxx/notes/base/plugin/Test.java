package com.xxx.notes.base.plugin;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.Properties;

/**
 * @ClassName LikeQueryInterceptor
 * @Description 模糊查询时的插件
 * @Author l17561
 * @Date 2018/11/30 11:18
 * @Version V1.0
 */
@Intercepts(
        {
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class})
        }
)
public class Test implements Interceptor {

    /**
     * 直接覆盖你所拦截的对象
     *
     * @param invocation 通过Invocation对象，可以反射调度原来对象的方法
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        Object[] queryArgs = invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement) queryArgs[0];
        Object parameter = queryArgs[1];

//        BoundSql boundSql = (BoundSql) invocation.getArgs()[5];
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        String sql  = boundSql.getSql().toUpperCase();
        if (sql.contains("LIKE") && ! sql.contains("BINARY")) {
            sql = sql.replaceAll("LIKE", "LIKE BINARY");
        }

        BoundSql newBoundSql = new BoundSql(mappedStatement.getConfiguration(), sql, boundSql.getParameterMappings(), boundSql.getParameterObject());
        // 把新的查询放到statement里
        MappedStatement newMs = copyFromMappedStatement(mappedStatement, new BoundSqlSqlSource(newBoundSql));
        for (ParameterMapping mapping : boundSql.getParameterMappings()) {
            String prop = mapping.getProperty();
            if (boundSql.hasAdditionalParameter(prop)) {
                newBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
            }
        }
        invocation.getArgs()[0] = newMs;


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
