package com.zjs.waimai.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjs.waimai.common.R;
import com.zjs.waimai.entity.Employee;
import com.zjs.waimai.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录功能，@RequestBody用来接收前端返回的json数据
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        //1、将页面提交的密码进行md5加密
        String password = employee.getPassword();
        password= DigestUtils.md5DigestAsHex(password.getBytes());
         //2、根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp=employeeService.getOne(queryWrapper);
        //3、如果没有查询到则返回登陆失败结果
        if (emp==null){
            return R.error("登陆失败");
        }
        //4、密码比对，如果不一致则返回登陆失败结果
        if(!emp.getPassword().equals(password)){
            return R.error("两次密码不一致");
        }
        //5、查看员工的状态，若为禁用状态则返回员工已禁用的结果
        if(emp.getStatus()==0){
            return  R.error("员工已被禁用");
        }
        //6、登陆成功，将员工的id存入session并返回成功结果
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        //1、清理Session中保存的当前员工的id
        request.getSession().removeAttribute("employee");

        return R.success("退出成功");
    }

    /**
     * 添加新员工,@RequestBody用来接收前端返回的json数据
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
        log.info("新增员工信息{}",employee.toString());
        //创建新员工时未设置初始密码，因此手动为其添加初始密码，并对其加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
//       下面五行代码因为是公共字段，所以在此注释掉，在MyMetaObiecthandler中进行编写
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//        long empid= (long) request.getSession().getAttribute("employee");
//        employee.setCreateUser(empid);
//        employee.setUpdateUser(empid);

        employeeService.save(employee);
        return R.success("新增员工成功");
    }

    /**
     * 员工分页查询
     * @param page Page：Mybatisplus提供的方法
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page ,int pageSize,String name){
        //构造分页构造器
        Page pageinfo = new Page(page,pageSize);
        //构造条件构造器,相当于数据库操作中的where name =......
        LambdaQueryWrapper<Employee> queryWrapper =new LambdaQueryWrapper();
        //添加过滤条件
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        //添加排序时间
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        //执行查询
        employeeService.page(pageinfo,queryWrapper);
        return R.success(pageinfo);
    }

    /**
     * 根据id修改员工的信息，包括员工状态等。
     * @param employee
     * @return
     */
    @PutMapping()
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){
        log.info(employee.toString());
        //公共字段，同理
//        Long empId= (Long) request.getSession().getAttribute("employee");
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(empId);
        employeeService.updateById(employee);
        return R.success("员工信息修改成功");
    }
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable long id){
        Employee employee =employeeService.getById(id);
        if (employee!=null){
        return R.success(employee);}
        else {
            return R.error("没有此用户相关信息");
        }
    }
}
