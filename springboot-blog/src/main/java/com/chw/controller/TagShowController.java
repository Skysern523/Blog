package com.chw.controller;

import com.chw.pojo.Tag;
import com.chw.service.BlogService;
import com.chw.service.TagService;
import com.chw.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * Create by CHW on 2020/9/23 20:53
 */
@Controller
public class TagShowController {
    @Autowired
    private TagService tagService;

    @Autowired
    private BlogService blogService;

    @GetMapping("/tags/{id}")
    public String tags(@PageableDefault(size = 3, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                        @PathVariable Long id, Model model) {
        List<Tag> tags = tagService.listTagTop(100000);
        if (id == -1) {//表示从导航点击过来的
            id = tags.get(0).getId();//默认选中第一个类型
        }
        BlogQuery blogQuery = new BlogQuery();
        model.addAttribute("tags", tags);//获取分类
        model.addAttribute("page", blogService.listBlog(id, pageable));//获取博客列表
        model.addAttribute("activetagId", id);//获取ui中活动的id
        return "tags";
    }
}
