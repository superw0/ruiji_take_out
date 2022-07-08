package com.wang.ruiji_take_out.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wang.ruiji_take_out.common.Result;
import com.wang.ruiji_take_out.pojo.Employee;
import com.wang.ruiji_take_out.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public Result<Employee> login(HttpServletRequest request, @RequestBody  Employee employee){
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Employee::getUsername,employee.getUsername());
        Employee dbEmployee = employeeService.getOne(wrapper);
        if (dbEmployee==null){
            return Result.error("登录失败！");
        }
        String password = employee.getPassword();
        String md5 = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!md5.equals(dbEmployee.getPassword())){
            return Result.error("密码错误！");
        }
        if (dbEmployee.getStatus()==0){
            return Result.error("账号已锁定！");
        }
        request.getSession().setAttribute("employee",dbEmployee.getId());
        return Result.success(dbEmployee);
    }

    @PostMapping("/logout")
    public Result<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return Result.success("退出成功");
    }

    @PostMapping()
    public Result<String> save(HttpServletRequest request,@RequestBody Employee employee){
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        Long emp = (Long) request.getSession().getAttribute("employee");
        /*employee.setCreateUser(emp);
        employee.setUpdateUser(emp);
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());*/
        employeeService.save(employee);
        return Result.success("保存成功！");
    }

    @GetMapping("/page")
    public Result<Page> page(Integer page,Integer pageSize,String name){
        Page pageinfo=new Page(page,pageSize);
        LambdaQueryWrapper<Employee> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(name),Employee::getName,name);
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        employeeService.page(pageinfo,queryWrapper);

        pageinfo.setTotal(pageinfo.getRecords().size());
        return Result.success(pageinfo);
    }

    @PutMapping
    public Result<String> update(HttpServletRequest request,@RequestBody Employee employee){
        Long updateEmp = (Long) request.getSession().getAttribute("employee");
        /*employee.setUpdateUser(updateEmp);
        employee.setUpdateTime(LocalDateTime.now());*/
        employeeService.updateById(employee);
        return Result.success("更新成功！");
    }

    @GetMapping("/{id}")
    public Result<Employee> getById(@PathVariable Long id){
        Employee employee = employeeService.getById(id);
        if (employee!=null){
            return Result.success(employee);
        }
        return Result.error("未查询到该用户！");
    }
}
