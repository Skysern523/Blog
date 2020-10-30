package com.chw.service;

import com.chw.dao.BlogRepository;
import com.chw.handler.NotFoundException;
import com.chw.pojo.Blog;
import com.chw.pojo.Type;
import com.chw.util.MarkdownUtils;
import com.chw.util.MyBeanUtils;
import com.chw.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;

/**
 * Create by CHW on 2020/9/22 19:25
 */
@Service
public class BlogServiceImpl implements BlogService{
    @Autowired
    BlogRepository blogRepository;
    @Override
    public Blog getBlog(Long id) {

        return blogRepository.getOne(id);
    }

    /**
     * 动态查询：如果查询条件为空，不做查询条件，如果不为空，代表条件传值来查询
     * JPA 分装好了高级查询，组合查询,定义好接口直接使用
     * 传递repository中findAll( new Specification)
     */
    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override //CriteriaQuery:查询条件容器，放置一些查询条件，CriteriaBuilder具体某一个条件表达式，例如相等，还可以模糊查询，like
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {//处理动态组合的方法，root代表查询对象,把对象映射成root，从这个里面可以获取一下表的字段
                List<Predicate> predicates = new ArrayList<>();   //放置一些组合条件
                if (!"".equals(blog.getTitle()) && blog.getTitle() != null) {
                    predicates.add(cb.like(root.<String>get("title"),"%"+blog.getTitle()+"%"));//查询对象的第一个属性（title）
                }
                if(blog.getTypeId()!= null){
                    predicates.add(cb.equal(root.<Type>get("type").get("id"),blog.getTypeId()));//传递blog对象中type中的id,根据id来查询
                }
                if(blog.isRecommend()){
                    predicates.add(cb.equal(root.<Boolean>get("recommened"),blog.isRecommend()));
                }
                cq.where(predicates.toArray(new Predicate[predicates.size()]));//进行查询，相当于sql中的where,传递的参数是一个查询条件数组,把list转成数组
                return null;
            }
        },pageable);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }
    //关联查询
    @Override
    public Page<Blog> listBlog(Long tagId, Pageable pageable) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Join join = root.join("tags");//关联查询，通过当前的blog关联对象
                return cb.equal(join.get("id"),tagId);//返回page对象，里面有type对象的属性
            }
        },pageable);
    }

    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        if(blog.getId() ==null){
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setViews(0);
        }else{//不是新增
            blog.setUpdateTime(new Date());
        }

        return blogRepository.save(blog);
    }

    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "updateTime");
        Pageable pageable= PageRequest.of(0, size, sort);
        return blogRepository.findTop(pageable);
    }

    @Override
    public Map<String, List<Blog>> archiveBlog() {
        List<String> years = blogRepository.findGroupYear();
        Map<String,List<Blog>> map = new HashMap<>();
        for (String year : years){
            map.put(year,blogRepository.findByYear(year));//该年份所对应的列表

        }
        return map;
    }

    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog b = blogRepository.getOne(id);
        if(b == null){
            throw  new NotFoundException("该博客不存在");
        }
        BeanUtils.copyProperties(blog,b, MyBeanUtils.getNullPropertyNames(blog));
        b.setUpdateTime(new Date());
        return blogRepository.save(b);
    }

    @Override
    public Long countBlog() {
        return blogRepository.count();
    }

    //全局搜索
    @Override
    public Page<Blog> listBlog(String query, Pageable pageable) {
        return blogRepository.findByQuery(query,pageable);
    }

    @Transactional
    @Override
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }

    @Modifying
    @Override
    public Blog getAndConvert(Long id) {
        Blog blog =blogRepository.getOne(id);
        if(blog == null){
            throw new NotFoundException("博客不存在");
        }
        Blog b = new Blog();//便于数据库的数据不会发生改变
        BeanUtils.copyProperties(blog,b);
        String content = b.getContent();//处理b从b里拿值
        b.setContent(MarkdownUtils.markdownToHtmlExtensions(content));

        //更新views
        blogRepository.updateViews(id);
        return b;
    }
}
