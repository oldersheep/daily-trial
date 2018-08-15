package com.xxx.notes.base.mapper;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @ClassName BaseMapper
 * @Description TODO
 * @Author l17561
 * @Date 2018/7/20 17:36
 * @Version V1.0
 */
public interface BaseMapper<T> extends Mapper<T>, MySqlMapper<T> {

}
