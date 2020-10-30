package com.chw.service;

import com.chw.pojo.Blog;
import com.chw.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * Create by CHW on 2020/9/22 19:22
 */
public interface BlogService {
    Blog getBlog(Long id);

    Page<Blog> listBlog(Pageable pageable, BlogQuery blog);

    Page<Blog> listBlog(Pageable pageable);

    //标签查询
    Page<Blog> listBlog(Long tagId ,Pageable pageable);

    Blog saveBlog(Blog blog);

    List<Blog> listRecommendBlogTop(Integer size);

    //博客年份
    Map<String,List<Blog>> archiveBlog();

    Blog updateBlog(Long id,Blog blog);

    //拿到博客归档的博客数量
    Long countBlog();

    //全局搜索
    Page<Blog> listBlog(String query,Pageable pageable);

    void deleteBlog(Long id);

    //处理文本
    Blog getAndConvert(Long id);

    //
}
