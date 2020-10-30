package com.chw.controller;

import com.chw.pojo.Type;
import com.chw.service.BlogService;
import com.chw.service.TypeService;
import com.chw.vo.BlogQuery;
import org.springframework.stereotype.Controller;
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
 * Create by CHW on 2020/9/23 20:25
 */
@Controller
public class TypeShowController {
    @Autowired
    private TypeService typeService;

    @Autowired
    private BlogService blogService;

    @GetMapping("/types/{id}")
    public String types(@PageableDefault(size = 3, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                        @PathVariable Long id, Model model) {
        List<Type> types = typeService.listTypeTop(10000);
        if (id == -1) {//表示从导航点击过来的
            id = types.get(0).getId();//默认选中第一个类型
        }
        BlogQuery blogQuery = new BlogQuery();
        blogQuery.setTypeId(id);
        model.addAttribute("types", types);//获取分类
        model.addAttribute("page", blogService.listBlog(pageable, blogQuery));//获取博客列表
        model.addAttribute("activeTypeId", id);//获取ui中活动的id
        return "types";
    }
}
