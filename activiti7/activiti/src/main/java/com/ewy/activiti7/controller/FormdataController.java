package com.ewy.activiti7.controller;

import com.ewy.activiti7.mapper.FormdataMapper;
import com.ewy.activiti7.util.AjaxResponse;
import com.ewy.activiti7.util.GlobalConfig;
import org.activiti.engine.repository.Deployment;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
@RestController
@RequestMapping("/formdata")
public class FormdataController {
    @Autowired
    FormdataMapper formdataMapper;

    // 表单历史信息
    @GetMapping("findFormVariables")
    public AjaxResponse findFormVariables(@RequestParam("instanceID") String instanceID) {
        try {
            List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
            list = formdataMapper.findFormVariables(instanceID);
//            for (HashMap map : list){
//                if (map.get("Control_VALUE_").toString().indexOf("&") != -1){
//                    // Control_VALUE_ -> 费用项:&金额:&备注:
////                    String tmpValue = map.get("Control_VALUE_").toString();
////                    String tmpLabel = map.get("Control_LABEL_").toString() + "详情";
//                    map.get("Control_VALUE_").toString().replace("&",",");
//                }
//            }
            return AjaxResponse.ajaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                    GlobalConfig.ResponseCode.SUCCESS.getDesc(), list);
        } catch (Exception e) {
            return AjaxResponse.ajaxData(GlobalConfig.ResponseCode.ERROR.getCode(),
                    "获取表单历史信息失败", e.toString());
        }
    }
}
