package com.xxx.notes.base.mapper;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import tk.mybatis.mapper.provider.SpecialProvider;

import java.util.List;

/**
 * @ClassName InsertListMapperExt
 * @Description
 * @Author aitaiyo
 * @Date 2019/4/29 21:25
 * @Version 1.0
 */
@tk.mybatis.mapper.annotation.RegisterMapper
public interface InsertListMapperExt<T> {

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @InsertProvider(type = InsertListProviderExt.class, method = "insertListExt")
    int insertListExt(List<T> recordList);
}
