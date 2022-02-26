package com.ewy.activiti7.controller;
import com.ewy.activiti7.SecurityUtil;
import com.ewy.activiti7.entity.UserInfoBean;
import com.ewy.activiti7.mapper.ActivitiMapper;
import com.ewy.activiti7.mapper.BussinessFormMapper;
import com.ewy.activiti7.mapper.InstanceTaskMapper;
import com.ewy.activiti7.util.AjaxResponse;
import com.ewy.activiti7.util.GlobalConfig;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.bpmn.model.FormProperty;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.runtime.ExecutionQuery;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired
    BussinessFormMapper bussinessFormMapper;
    @Autowired
    SecurityUtil securityUtil;
    @Autowired
    TaskRuntime taskRuntime;
    @Autowired
    ProcessRuntime processRuntime;
    @Autowired
    RepositoryService repositoryService;
    @Autowired
    ActivitiMapper activitiMapper;
    @Autowired
    InstanceTaskMapper instanceTaskMapper;

    // 获取我的待办任务
    @GetMapping("/getTasks")
    public AjaxResponse getTasks(@AuthenticationPrincipal UserInfoBean userInfoBean) {
        try {
            if (GlobalConfig.Test) {
                securityUtil.logInAs("bajie");
            }
            // 封装了按当前登录用户获取任务列表
            Page<Task> taskPage = taskRuntime.tasks(Pageable.of(0,100));
            List<HashMap<String,Object>> listMap = new ArrayList<HashMap<String,Object>>();
            String username = userInfoBean.getUsername();
            int seq = 0;
            for (Task task : taskPage.getContent()){
                // 用于获取实例名，可用表关联改进
                ProcessInstance processInstance = processRuntime.processInstance(task.getProcessInstanceId());
                HashMap<String,Object> hashMap = new HashMap<String,Object>();
                String instanceid = task.getProcessInstanceId();
                hashMap.put("myinstanceid", instanceTaskMapper.fiInstanceTask(instanceid));
                seq++;
                hashMap.put("seq",seq);
                hashMap.put("id",task.getId());
                hashMap.put("taskName",task.getName());
                hashMap.put("status",task.getStatus());
                hashMap.put("createdDate",task.getCreatedDate());
                hashMap.put("taskKey",task.getFormKey());
                if (task.getAssignee() == null){// 执行人，null时前台显示未拾取
                    hashMap.put("assignee","待拾取任务");
                } else {
                    hashMap.put("assignee",task.getAssignee());
                }
                hashMap.put("processInstanceName",processInstance.getName());
                listMap.add(hashMap);
            }
            return AjaxResponse.ajaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                    GlobalConfig.ResponseCode.SUCCESS.getDesc(), listMap);
        } catch (Exception e) {
            return AjaxResponse.ajaxData(GlobalConfig.ResponseCode.ERROR.getCode(),
                    "获取我的待办任务失败", e.toString());
        }
    }

    // 完成任务
    @GetMapping("/completeTask")
    public AjaxResponse completeTasks(@RequestParam("taskID")String taskID) {
        try {
            if (GlobalConfig.Test) {
                securityUtil.logInAs("bajie");
            }
            Task task = taskRuntime.task(taskID);
            if (task.getAssignee() == null){
                taskRuntime.claim(TaskPayloadBuilder.claim().withTaskId(task.getId()).build());
            }
            taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(task.getId())
//                    .withVariable("num","2"))// 执行环节设置变量
                    .build());
            return AjaxResponse.ajaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                    GlobalConfig.ResponseCode.SUCCESS.getDesc(), null);
        } catch (Exception e) {
            return AjaxResponse.ajaxData(GlobalConfig.ResponseCode.ERROR.getCode(),
                    "完成任务失败", e.toString());
        }
    }

    // 渲染动态表单
    @GetMapping("formDataShow")
    public AjaxResponse formDataShow(@RequestParam("taskID")String taskID) {
        try {
            if (GlobalConfig.Test) {
                securityUtil.logInAs("bajie");
            }
            Task task = taskRuntime.task(taskID);
            //--------构建表单控件历史数据字典----------
            HashMap<String,String> controlListMap = new HashMap<String,String>();
            //读取数据库中本实例下所有表单数据
            List<HashMap<String,Object>> tmpControlList = activitiMapper.findFormData(task.getProcessInstanceId());
            for (HashMap control1 : tmpControlList){
                controlListMap.put(control1.get("Control_ID_").toString(),control1.get("Control_VALUE_").toString());
            }
            UserTask userTask = (UserTask) repositoryService
                    .getBpmnModel(task.getProcessDefinitionId())
                    // 拿不到taskKey故另taskKey=formKey
                    .getFlowElement(task.getFormKey());
            if (userTask == null){
                return AjaxResponse.ajaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                        GlobalConfig.ResponseCode.SUCCESS.getDesc(),"无表单");
            }
            List<FormProperty> formProperties = userTask.getFormProperties();
            List<HashMap<String,Object>> listMap = new ArrayList<HashMap<String,Object>>();
            for (FormProperty formProperty : formProperties){
                String[] splitFP = formProperty.getId().split("-_!");
                HashMap<String,Object> hashMap = new HashMap<String,Object>();
                hashMap.put("id",splitFP[0]);
                hashMap.put("controlType",splitFP[1]);
                hashMap.put("controlLabel",splitFP[2]);
                // 默认如果是表单控件ID
                if (splitFP[3].startsWith("FormProperty_")){
                    // 控件ID存在
                    if (controlListMap.containsKey(splitFP[3])){
                        hashMap.put("controlDefValue",controlListMap.get(splitFP[3]));
                    } else {
                        hashMap.put("controlDefValue","读取失败，检查" + splitFP[0] + "配置");
                    }
                } else {
                    hashMap.put("controlDefValue",splitFP[3]);
                }
                hashMap.put("controlIsParam",splitFP[4]);
                listMap.add(hashMap);
            }
            return AjaxResponse.ajaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                    GlobalConfig.ResponseCode.SUCCESS.getDesc(),listMap);
        } catch (Exception e) {
            return AjaxResponse.ajaxData(GlobalConfig.ResponseCode.ERROR.getCode(),
                    "任务表单渲染失败", e.toString());
        }
    }

    // 保存动态表单
    @PostMapping("formDataSave")
    public AjaxResponse formDataSave(@RequestParam("taskID")String taskID,@RequestParam("formData")String formData,@AuthenticationPrincipal UserInfoBean userInfoBean) {
        try {
            if (GlobalConfig.Test) {
                securityUtil.logInAs("bajie");
            }
            Task task = taskRuntime.task(taskID);
            HashMap<String,Object> variables = new HashMap<String,Object>();
            Boolean hasVariables = false;// 没有任何参数
            // 前段传来的字符串拆分为控件组
            List<HashMap<String,Object>> listMap = new ArrayList<HashMap<String,Object>>();
            String[] formDataList = formData.split("!_!");
            for (String controlItem : formDataList){
                String[] formDataItem = controlItem.split("-_!");
                HashMap<String,Object> hashMap = new HashMap<String,Object>();
                hashMap.put("PROC_DEF_ID_",task.getProcessDefinitionId());
                hashMap.put("PROC_INST_ID_",task.getProcessInstanceId());
                hashMap.put("FORM_KEY_",task.getFormKey());
                hashMap.put("Control_ID_",formDataItem[0]);
                hashMap.put("Control_VALUE_",formDataItem[1]);
                hashMap.put("Control_LABEL_",formDataItem[3]);
//                hashMap.put("Control_PARAM_",formDataItem[2]);
                listMap.add(hashMap);
                // 构建参数集合
                switch (formDataItem[2]){
                    case "f":
                        System.out.println("控件不作为参数");
                        break;
                    case "s":
                        variables.put(formDataItem[0],formDataItem[1]);
                        hasVariables = true;
                        break;
                    case "t":
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        variables.put(formDataItem[0],simpleDateFormat.parse(formDataItem[1]));
                        hasVariables = true;
                        break;
                    case "b":
                        variables.put(formDataItem[0], BooleanUtils.toBoolean(formDataItem[1]));
                        hasVariables = true;
                        break;
                    default:
                        System.out.println("控件ID" + formDataItem[0] + "的参数" + formDataItem[2]  + "不存在");
                }
            }


            if (hasVariables){
                // 带参数完成任务
                taskRuntime.complete(TaskPayloadBuilder
                        .complete()
                        .withTaskId(taskID)
                        .withVariables(variables)
                        .build()
                );
                if ("lisi".equals(userInfoBean.getUsername()) || "wangwu".equals(userInfoBean.getUsername())){
                    // 同时有单据就修改单据状态为已处理
                    String myinstanceid = instanceTaskMapper.fiInstanceTask(task.getProcessInstanceId());
                    String taskKey = task.getFormKey();
                    int i = bussinessFormMapper.findBussinessFormCount(myinstanceid,taskKey);
                    if (i > 0){
                        bussinessFormMapper.updateBussinessForm(myinstanceid,taskKey);
                    }
                }
            } else {
                taskRuntime.complete(TaskPayloadBuilder
                                .complete()
                                .withTaskId(taskID)
                                .build()
                );
                if ("lisi".equals(userInfoBean.getUsername()) || "wangwu".equals(userInfoBean.getUsername())){
                    // 同时有单据就修改单据状态为已处理
                    String myinstanceid = instanceTaskMapper.fiInstanceTask(task.getProcessInstanceId());
                    String taskKey = task.getFormKey();
                    int i = bussinessFormMapper.findBussinessFormCount(myinstanceid,taskKey);
                    if (i > 0){
                        bussinessFormMapper.updateBussinessForm(myinstanceid,taskKey);
                    }
                }
            }
            // 写入数据库
            int result = activitiMapper.insertFormData(listMap);
            return AjaxResponse.ajaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                    GlobalConfig.ResponseCode.SUCCESS.getDesc(),listMap);
        } catch (Exception e) {
            return AjaxResponse.ajaxData(GlobalConfig.ResponseCode.ERROR.getCode(),
                    "保存表单信息失败", e.toString());
        }
    }


}
