package com.weilai.service;

import com.weilai.common.Blog;

/**
 * @ClassName BlogServiceImpl
 * @Description: TODO
 */
public class BlogServiceImpl implements BlogService {
    @Override
    public Blog getBlogById(Integer id) {
        Blog blog = Blog.builder().id(123).userId(777).title("Blog").build();
        System.out.println("客户端查询了" + id + "博客");
        return blog;
    }
}
