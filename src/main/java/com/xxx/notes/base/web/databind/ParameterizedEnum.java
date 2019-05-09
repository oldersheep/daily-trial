/*
 * Copyright (c) 2019. H3C. All rights reserved.
 */

package com.xxx.notes.base.web.databind;

/**
 * {@code ParameterizedEnum} controller层传入参数如果是枚举类型，那么最好实现该接口，注意该接口只能由枚举类来实现
 *
 * @author f18467
 * @version 1.0.0
 * <p>
 * @since 2019/3/25
 */
public interface ParameterizedEnum {

    boolean canResolve(String t);

    String getRequestParameter();

}
