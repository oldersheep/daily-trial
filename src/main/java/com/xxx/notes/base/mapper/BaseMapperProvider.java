package com.xxx.notes.base.mapper;

import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

import java.util.Iterator;
import java.util.Set;

/**
 * @ClassName BaseMapperProvider
 * @Description 通用mapper中扩展一个
 * @Author l17561
 * @Date 2018/9/29 13:56
 * @Version V1.0
 */
public class BaseMapperProvider extends MapperTemplate {

    public BaseMapperProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    public String deleteByIDS(MappedStatement ms) {
        Class<?> entityClass = this.getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.deleteFromTable(entityClass, this.tableName(entityClass)));
        // sql.append(SqlHelper.wherePKColumns(entityClass, true));
        sql.append("<where>");
        Set<EntityColumn> columnSet = EntityHelper.getPKColumns(entityClass);
        Iterator var4 = columnSet.iterator();
        while(var4.hasNext()) {
            EntityColumn column = (EntityColumn)var4.next();
            sql.append(" AND " + column.getColumn() + " in ");
            sql.append("<foreach item=\"" + column.getProperty() + "\" collection=\"array\" open=\"(\" separator=\",\" close=\")\">")
            .append("#{" + column.getProperty() + "}</foreach>");
        }
        sql.append("</where>");
        return sql.toString();
    }
}
