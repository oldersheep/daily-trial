package com.xxx.notes.base.mapper;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @ClassName BaseMapper
 * @Description TODO
 * @Author l17561
 * @Date 2018/7/20 17:36
 * @Version V1.0
 */
@RegisterMapper
public interface BaseMapper<T> extends Mapper<T>, MySqlMapper<T> {

    /**
     * 根据主键ID批量删除
     *
     * @param key
     * @return
     */
    @DeleteProvider(type = BaseMapperProvider.class, method = "dynamicSQL")
    int deleteByIDS(Object[] key);
}
