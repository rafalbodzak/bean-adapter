package com.rbo.beanadapter;

import java.util.List;

/**
 * User: rbo, Date: 06.07.14, Time: 23:31
 */
public interface BeanAdapter {

    void adapt(Object sourceBean, Object destBean);

    <SOURCE, DEST> List<DEST> adaptToNewList(List<SOURCE> sourceList, Class<DEST> clazz);
}
