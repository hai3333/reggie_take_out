package com.hfs.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.hfs.reggie.common.BaseContext;
import com.hfs.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebFilter
@Slf4j
public class LoginCheckFilter implements Filter {
    //路径匹配器 支持通配符
    public  static final AntPathMatcher PATH_MATCHER=new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request=(HttpServletRequest) servletRequest;
        HttpServletResponse response=(HttpServletResponse)  servletResponse;
        //获取本次请求的uri
        String requestURI = request.getRequestURI();
        log.info("本次请求地址"+requestURI);
        //定于不需要处理的请求路径
        String[] urls=new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**"
        } ;
        //判断是否需要处理
        boolean check = check(urls, requestURI);
        //不需要处理直接放行
        if(check){
            filterChain.doFilter(request,response);
            return;
        }
        //判断是否登录 若登录 直接放行
        if(request.getSession().getAttribute("employee")!=null){
            log.info("登录");


            Long empId=(Long)request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);


            filterChain.doFilter(request,response);
            return;
        }
        log.info("用户未登录");
        //通过客户端像浏览器响应数据
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));


     return;

    }

        /*
        * 进行路径匹配
        * */
    public boolean check(String[] urls, String requestURI) {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match == true) {
                return true;
            }
        }
        return false;
    }
}
