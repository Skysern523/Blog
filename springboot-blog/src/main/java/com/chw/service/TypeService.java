package com.chw.service;

import com.chw.pojo.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Create by CHW on 2020/9/22 15:41
 */
public interface TypeService {
    Type saveType(Type type);

    Type getType(Long id);

    Type  getTypeByname(String name);

    Page<Type> listType(Pageable pageable);

    List<Type> listTypeTop(Integer size);

    List<Type> listType();

    Type updateType(Long id,Type type);

    void deleteType(Long id);
}
