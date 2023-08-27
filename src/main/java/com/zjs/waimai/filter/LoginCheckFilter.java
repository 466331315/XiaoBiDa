package com.zjs.waimai.filter;

import com.alibaba.fastjson.JSON;
import com.zjs.waimai.common.BaseContext;
import com.zjs.waimai.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //1、获取本次请求的uri
        String requesturl = request.getRequestURI();
        log.info("拦截到请求:{}", request.getRequestURI());
        //不需要处理的请求
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login"
        };
        //2、判断本次请求是否需要处理
        boolean check = check(urls, requesturl);
        //3、如果不需要处理则直接放行
        if (check) {
            log.info("本次请求不需要处理", requesturl);
            //放行
            filterChain.doFilter(request, response);
            return;
        }
        //4、判断登陆状态，如果已经登陆则直接放行
        if (request.getSession().getAttribute("employee") != null) {
            //调用实现的BaseContext来获取ID
            Long empid = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentID(empid);
            //放行
            filterChain.doFilter(request, response);
            return;
        }
        //手机用户是否登录
        if (request.getSession().getAttribute("user") != null) {
            log.info("监测到已登陆,id为:{}",request.getSession().getAttribute("user"));
            //调用实现的BaseContext来获取ID
            Long userid = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentID(userid);
            //放行
            filterChain.doFilter(request, response);
            return;
        }
        log.info("用户未登录");
        //5、如果未登录则返回未登录结果,因为前端需要后端的数据来进行页面的跳转
        //所以在此需要使用流的方式写入数据
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
//        log.info("拦截到请求:{}",request.getRequestURI());

    }

    public boolean check(String[] urls, String requestURI) {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match) {
                return true;
            }
        }
        return false;
    }
}
