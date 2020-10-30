package com.chw.dao;

import com.chw.pojo.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Create by CHW on 2020/9/22 19:26
 */

/**
 * 继承JpaSpecificationExecutor<>方法来动态查询<>传递
 */
public interface BlogRepository extends JpaRepository<Blog,Long>, JpaSpecificationExecutor<Blog> {//接口中的方法可以实现动态查询
    @Query("select b from Blog b where b.recommened = true ")
    List<Blog> findTop(Pageable pageable);
    //jbql
    //select * from t_blog where title like '%内容%'
    @Query("select b from Blog b where b.title like ?1 or b.content like ?1")
   Page<Blog> findByQuery (String query,Pageable pageable);

    @Transactional
    @Modifying
    @Query("update Blog b set b.views = b.views+1 where b.id = ?1")
    int updateViews(Long id);

    @Query("select  function('date_format',b.updateTime,'%Y') as year from Blog  b group by function('date_format',b.updateTime,'%Y') order by year desc ")
    List<String> findGroupYear();

    //根据年份查询到的博客
    @Query("select  b from  Blog b where function('date_format',b.updateTime,'%Y') = ?1 ")//第一个占位数就是传递的年份
    List<Blog> findByYear(String year);
 }
