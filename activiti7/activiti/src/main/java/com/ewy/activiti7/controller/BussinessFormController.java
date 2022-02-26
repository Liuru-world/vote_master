package com.ewy.activiti7.controller;

import com.ewy.activiti7.mapper.BussinessFormMapper;
import com.ewy.activiti7.util.AjaxResponse;
import com.ewy.activiti7.util.GlobalConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/bussiness")
public class BussinessFormController {

    @Autowired
    BussinessFormMapper bussinessFormMapper;

    // 生成业务表单
    @PostMapping("/insertBussinessForm")
    public AjaxResponse insertBussinessForm(@RequestParam("formName") String formName,
                                            @RequestParam("formData") String formData,
                                            @RequestParam("myinstanceid") String myinstanceid,
                                            @RequestParam("taskKey") String taskKey) {
        try {
            // 前段传来的字符串拆分
            List<HashMap<String, Object>> listMap = new ArrayList<HashMap<String, Object>>();
            String[] formDataList = formData.split("!_!");
            for (String controlItem : formDataList) {
                String[] formDataItem = controlItem.split("-_!");
                HashMap<String, Object> hashMap = new HashMap<String, Object>();
                hashMap.put("column_value", formDataItem[1]);
                hashMap.put("column_name", formDataItem[3]);
                // 存入对应的taskKey和myinstanceid
                hashMap.put("form_name", formName);
                hashMap.put("task_key", taskKey);
                hashMap.put("my_instanceid", myinstanceid);
                listMap.add(hashMap);
            }
            int i = bussinessFormMapper.findBussinessFormCount2(formName);
            if (i > 0){
                return AjaxResponse.ajaxData(GlobalConfig.ResponseCode.ERROR.getCode(),
                        "单据不能重名","请重试～～");
            }
            // 写入数据库
            int result = bussinessFormMapper.insertBussinessForm(listMap);
            return AjaxResponse.ajaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                    GlobalConfig.ResponseCode.SUCCESS.getDesc(), result);
        } catch (Exception e) {
            return AjaxResponse.ajaxData(GlobalConfig.ResponseCode.ERROR.getCode(),
                    "生成业务表单", e.toString());
        }
    }

    // 实例单据信息
    @GetMapping("/findBussinessForm")
    public AjaxResponse findBussinessForm(@RequestParam("myinstanceid") String myinstanceid) {
        try {
            List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
            list = bussinessFormMapper.findBussinessForm(myinstanceid);
            return AjaxResponse.ajaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                    GlobalConfig.ResponseCode.SUCCESS.getDesc(), list);
        } catch (Exception e) {
            return AjaxResponse.ajaxData(GlobalConfig.ResponseCode.ERROR.getCode(),
                    "获取实例单据信息失败", e.toString());
        }
    }
}
