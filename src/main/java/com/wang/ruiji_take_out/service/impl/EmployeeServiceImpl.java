package com.wang.ruiji_take_out.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wang.ruiji_take_out.mapper.EmployeeMapper;
import com.wang.ruiji_take_out.pojo.Employee;
import com.wang.ruiji_take_out.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
