package com.hfs.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hfs.reggie.common.R;
import com.hfs.reggie.entity.Employee;
import com.hfs.reggie.service.EmployeeService;
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
     * 员工登录
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request,@RequestBody Employee employee){

        //1、将页面提交的密码password进行md5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //2、根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        //3、如果没有查询到则返回登录失败结果
        if(emp == null){
            return R.error("登录失败");
        }

        //4、密码比对，如果不一致则返回登录失败结果
        if(!emp.getPassword().equals(password)){
            return R.error("登录失败");
        }

        //5、查看员工状态，如果为已禁用状态，则返回员工已禁用结果
        if(emp.getStatus() == 0){
            return R.error("账号已禁用");
        }

        //6、登录成功，将员工id存入Session并返回登录成功结果
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }

    /**
     * 员工退出
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        //清理Session中保存的当前登录员工的id
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }
    //新增员工
    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
        log.info("新增员工信息"+employee.toString());
        //密码需要md5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

        //设置时间
        //employee.setCreateTime(LocalDateTime.now());
        //employee.setUpdateTime(LocalDateTime.now());
        //Long empId = (Long) request.getSession().getAttribute("employee");
        //employee.setCreateUser(empId);
        //employee.setUpdateUser(empId);

        employeeService.save(employee);


        return R.success("修改成功");
    }

    /*
    * 展示员工信息
    * */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        log.info("page={},pageSize={},name={}",page,pageSize,name);
        //构造分页器
        Page pageInfo=new Page(page,pageSize);
        //构造条件创造器
        LambdaQueryWrapper<Employee> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        //排序
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        //执行查询
        employeeService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }
    //用户状态修改
        @RequestMapping
        public R<String> update(HttpServletRequest request,@RequestBody Employee employee){
            log.info(employee.toString());

            long id = Thread.currentThread().getId();
            log.info("线程为id为{}",id);

            Long empId = (Long) request.getSession().getAttribute("employee");
            employee.setUpdateTime(LocalDateTime.now());
            employee.setUpdateUser(empId);
            employeeService.updateById(employee);
            return R.success("员工信息修改成功");
        }

        //根据id查询员工共信息
        @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable long id){
        log.info("{}",id);
       Employee employee=employeeService.getById(id);
       return R.success(employee);
    }
}

