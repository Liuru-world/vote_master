package com.ewy.activiti7.controller;

import com.ewy.activiti7.mapper.ActivitiMapper;
import com.ewy.activiti7.util.AjaxResponse;
import com.ewy.activiti7.util.GlobalConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    ActivitiMapper activitiMapper;

    // 获取用户列表
    @GetMapping("/getUsers")
    public AjaxResponse getUsers() {
        try {
            List<HashMap<String,Object>> userList = activitiMapper.findUser();
            return AjaxResponse.ajaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                    GlobalConfig.ResponseCode.SUCCESS.getDesc(), userList);
        } catch (Exception e) {
            return AjaxResponse.ajaxData(GlobalConfig.ResponseCode.ERROR.getCode(),
                    "获取用户列表失败", e.toString());
        }
    }
}
