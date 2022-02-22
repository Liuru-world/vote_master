package com.ewy.activiti7.controller;

import com.ewy.activiti7.SecurityUtil;
import com.ewy.activiti7.entity.UserInfoBean;
import com.ewy.activiti7.util.AjaxResponse;
import com.ewy.activiti7.util.GlobalConfig;
import org.activiti.api.model.shared.model.VariableInstance;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/processInstance")
public class ProcessInstanceController {
    @Autowired
    RepositoryService repositoryService;
    @Autowired
    SecurityUtil securityUtil;
    @Autowired
    ProcessRuntime processRuntime;

    // 查询流程实例
    @GetMapping("/getInstances")
    public AjaxResponse getInstances(@AuthenticationPrincipal UserInfoBean userInfoBean) {
        try {
            if (GlobalConfig.Test) {
                securityUtil.logInAs("bajie");
            }
//            else {
//                securityUtil.logInAs(userInfoBean.getUsername());
//            }
            Page<ProcessInstance> processInstancePage = processRuntime.processInstances(Pageable.of(0,100));
            List<ProcessInstance> list = processInstancePage.getContent();
            list.sort((x,y)->y.getStartDate().toString().compareTo(x.getStartDate().toString()));
            List<HashMap<String, Object>> listMap = new ArrayList<HashMap<String, Object>>();
            for (ProcessInstance processInstance : list){
                HashMap<String, Object> hashMap = new HashMap<String, Object>();
                hashMap.put("id",processInstance.getId());
                hashMap.put("name",processInstance.getName());
                hashMap.put("status",processInstance.getStatus());
                hashMap.put("processDefinitionId",processInstance.getProcessDefinitionId());
                hashMap.put("processDefinitionKey",processInstance.getProcessDefinitionKey());
                hashMap.put("startDate",processInstance.getStartDate());
                hashMap.put("processDefinitionVersion",processInstance.getProcessDefinitionVersion());
                // 因为processInstance里没有历史高亮需要的depolymentID和resourceName，所以需要再次查询(可用联合查询改进)
                ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                        .processDefinitionId(processInstance.getProcessDefinitionId())
                                .singleResult();
                hashMap.put("deploymentId",processDefinition.getDeploymentId());
                hashMap.put("resourceName",processDefinition.getResourceName());
                listMap.add(hashMap);
            }
            return AjaxResponse.ajaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                    GlobalConfig.ResponseCode.SUCCESS.getDesc(), listMap);
        } catch (Exception e) {
            return AjaxResponse.ajaxData(GlobalConfig.ResponseCode.ERROR.getCode(),
                    "获取流程实例失败", e.toString());
        }
    }

    // 启动流程实例
    @GetMapping("/startProcess")
    public AjaxResponse startProcess(@RequestParam String processDefinitionKey, @RequestParam String instanceName) {
        try {
            if (GlobalConfig.Test) {
                securityUtil.logInAs("bajie");
            }
//            else {
//                securityUtil.logInAs(SecurityContextHolder.getContext().getAuthentication().getName());
//            }
            ProcessInstance processInstance = processRuntime.start(ProcessPayloadBuilder
                            .start()
                            .withProcessDefinitionKey(processDefinitionKey)
                            .withName(instanceName)
                            .withBusinessKey("自定义BusinessKey")
//                    .withVariable("参数名","参数值")
                            .build()
            );
            return AjaxResponse.ajaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                    GlobalConfig.ResponseCode.SUCCESS.getDesc(), processInstance.getName());
        } catch (Exception e) {
            return AjaxResponse.ajaxData(GlobalConfig.ResponseCode.ERROR.getCode(),
                    "启动流程实例失败", e.toString());
        }
    }


    // 挂起流程实例
    @GetMapping("/suspendInstance")
    public AjaxResponse suspendInstance(@RequestParam String instanceID) {
        try {
            if (GlobalConfig.Test){
                securityUtil.logInAs("bajie");
            }
            ProcessInstance processInstance = processRuntime.suspend(
                    ProcessPayloadBuilder
                            .suspend()
                            .withProcessInstanceId(instanceID)
                            .build()
            );
            return AjaxResponse.ajaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                    GlobalConfig.ResponseCode.SUCCESS.getDesc(), processInstance.getName());
        } catch (Exception e) {
            return AjaxResponse.ajaxData(GlobalConfig.ResponseCode.ERROR.getCode(),
                    "挂起流程实例失败", e.toString());
        }
    }

    // 激活/重启流程实例
    @GetMapping("/resumeInstance")
    public AjaxResponse resumeInstance(@RequestParam String instanceID) {
        try {
            if (GlobalConfig.Test){
                securityUtil.logInAs("bajie");
            }
            ProcessInstance processInstance = processRuntime.resume(
                    ProcessPayloadBuilder
                            .resume()
                            .withProcessInstanceId(instanceID)
                            .build()
            );
            return AjaxResponse.ajaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                    GlobalConfig.ResponseCode.SUCCESS.getDesc(), processInstance.getName());
        } catch (Exception e) {
            return AjaxResponse.ajaxData(GlobalConfig.ResponseCode.ERROR.getCode(),
                    "激活流程实例失败", e.toString());
        }
    }

    // 删除流程实例
    @GetMapping("/deleteInstance")
    public AjaxResponse deleteInstance(@RequestParam String instanceID) {
        try {
            if (GlobalConfig.Test){
                securityUtil.logInAs("bajie");
            }
            ProcessInstance processInstance = processRuntime.delete(
                    ProcessPayloadBuilder
                            .delete()
                            .withProcessInstanceId(instanceID)
                            .build()
            );
            return AjaxResponse.ajaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                    GlobalConfig.ResponseCode.SUCCESS.getDesc(), processInstance.getName());
        } catch (Exception e) {
            return AjaxResponse.ajaxData(GlobalConfig.ResponseCode.ERROR.getCode(),
                    "删除流程实例失败", e.toString());
        }
    }

    // 查询流程参数
    @GetMapping("/getVariables")
    public AjaxResponse getVariables(@RequestParam String instanceID) {
        try {
            if (GlobalConfig.Test){
                securityUtil.logInAs("bajie");
            }
           List<VariableInstance> variableInstances = processRuntime.variables(
                   ProcessPayloadBuilder
                           .variables()
                           .withProcessInstanceId(instanceID)
                           .build()
           );
            return AjaxResponse.ajaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                    GlobalConfig.ResponseCode.SUCCESS.getDesc(), variableInstances);
        } catch (Exception e) {
            return AjaxResponse.ajaxData(GlobalConfig.ResponseCode.ERROR.getCode(),
                    "获取流程实例参数失败", e.toString());
        }
    }

}
