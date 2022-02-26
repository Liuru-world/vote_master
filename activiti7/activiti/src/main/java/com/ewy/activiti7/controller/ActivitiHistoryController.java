package com.ewy.activiti7.controller;
import com.ewy.activiti7.SecurityUtil;
import com.ewy.activiti7.entity.UserInfoBean;
import com.ewy.activiti7.mapper.InstanceTaskMapper;
import com.ewy.activiti7.util.AjaxResponse;
import com.ewy.activiti7.util.GlobalConfig;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/activitiHistory")
public class ActivitiHistoryController {
    @Autowired
    SecurityUtil securityUtil;
    @Autowired
    HistoryService historyService;
    @Autowired
    RepositoryService repositoryService;
    @Autowired
    InstanceTaskMapper instanceTaskMapper;
    // 用户历史任务
    @GetMapping("/getTasksByUserName")
    public AjaxResponse getTasksByUserName(@AuthenticationPrincipal UserInfoBean userInfoBean) {
        try {
            List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery()
                    .orderByHistoricTaskInstanceEndTime().asc()
                    .taskAssignee(userInfoBean.getUsername())
                    .list();
            List<HashMap<String, Object>> listMap = new ArrayList<HashMap<String, Object>>();
            int seq = 0;
            for (HistoricTaskInstance historicTaskInstance : historicTaskInstances){
                HashMap<String, Object> hashMap = new HashMap<String, Object>();
                seq++;
                hashMap.put("myinstanceid",instanceTaskMapper.fiInstanceTask(historicTaskInstance.getProcessInstanceId()));
                hashMap.put("taskDefinitionKey",historicTaskInstance.getTaskDefinitionKey());
                hashMap.put("name",historicTaskInstance.getName());
//                hashMap.put("createTime",historicTaskInstance.getCreateTime());
                hashMap.put("startTime",historicTaskInstance.getStartTime());
                hashMap.put("endTime",historicTaskInstance.getEndTime());
                hashMap.put("assignee",historicTaskInstance.getAssignee());
                listMap.add(hashMap);
            }
            return AjaxResponse.ajaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                    GlobalConfig.ResponseCode.SUCCESS.getDesc(), listMap);
        } catch (Exception e) {
            return AjaxResponse.ajaxData(GlobalConfig.ResponseCode.ERROR.getCode(),
                    "获取用户历史任务失败", e.toString());
        }
    }

    // 根据流程实例ID查询历史任务
    @GetMapping("/getTasksByPiID")
    public AjaxResponse getTasksByPiID(@RequestParam("instanceId") String instanceId) {
        try {
            List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery()
                    .orderByHistoricTaskInstanceEndTime().desc()
                    .processInstanceId(instanceId)
                    .list();
            return AjaxResponse.ajaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                    GlobalConfig.ResponseCode.SUCCESS.getDesc(), historicTaskInstances);
        } catch (Exception e) {
            return AjaxResponse.ajaxData(GlobalConfig.ResponseCode.ERROR.getCode(),
                    "获取流程实例历史任务失败", e.toString());
        }
    }

    // 高亮显示流程历史
//    a34c9b77-92c2-11ec-b548-42bf0cec4264
    @GetMapping("/getHighLine")
    public AjaxResponse getHighLine(@RequestParam("instanceId") String instanceId,@AuthenticationPrincipal UserInfoBean userInfoBean) {
        try {
            HistoricProcessInstance historicProcessInstance = historyService
                    .createHistoricProcessInstanceQuery().processInstanceId(instanceId).singleResult();
            // 读取bpmn
            BpmnModel bpmnModel = repositoryService.getBpmnModel(historicProcessInstance.getProcessDefinitionId());
            Process process = bpmnModel.getProcesses().get(0);
            // 获取所以流程FlowElement的信息
            Collection<FlowElement> flowElements = process.getFlowElements();
            Map<String,String> map = new HashMap<String,String>();
            for (FlowElement flowElement : flowElements){
                // 判断是否连线
                if (flowElement instanceof SequenceFlow){
                    SequenceFlow sequenceFlow = (SequenceFlow) flowElement;
                    String ref = sequenceFlow.getSourceRef();
                    String targetRef = sequenceFlow.getTargetRef();
                    map.put(ref + targetRef,sequenceFlow.getId());
                }
            }
            // 获取流程实例 历史节点（全部）
            List<HistoricActivityInstance> historicActivityInstances = historyService
                    .createHistoricActivityInstanceQuery().processInstanceId(instanceId)
                    .list();
            // 各个历史节点 两两组合成key
            Set<String> keyList = new HashSet<String>();
            for (HistoricActivityInstance i : historicActivityInstances){
                for (HistoricActivityInstance j : historicActivityInstances){
                    if (i != j){
                        keyList.add(i.getActivityId() + j.getActivityId());
                    }
                }
            }
            // 高亮连线id
            Set<String> highLine = new HashSet<String>();
            keyList.forEach(s -> highLine.add(map.get(s)));

            // 获取已经完成的节点
            List<HistoricActivityInstance> listFinished = historyService
                    .createHistoricActivityInstanceQuery().processInstanceId(instanceId)
                    .finished()
                    .list();
            // 已经完成的节点高亮
            Set<String> highPoint = new HashSet<String>();
            listFinished.forEach(s -> highPoint.add(s.getActivityId()));

            // 获取待办节点
            List<HistoricActivityInstance> listUnfinished = historyService
                    .createHistoricActivityInstanceQuery().processInstanceId(instanceId)
                    .unfinished()
                    .list();
            // 待办节点高亮
            Set<String> waitingToDo = new HashSet<String>();
            listUnfinished.forEach(s -> waitingToDo.add(s.getActivityId()));

            // 当前用户完成的任务
            String assigneeName = null;
            assigneeName = userInfoBean.getUsername();
            List<HistoricTaskInstance> taskInstances = historyService
                    .createHistoricTaskInstanceQuery()
                    .taskAssignee(assigneeName)
                    .processInstanceId(instanceId)
                    .finished()
                    .list();
            // 高亮当前用户完成的节点
            Set<String> iDo = new HashSet<String>();
            taskInstances.forEach(s -> iDo.add(s.getTaskDefinitionKey()));

            Map<String,Object> resMap = new HashMap<String,Object>();
            resMap.put("highPoint",highPoint);
            resMap.put("highLine",highLine);
            resMap.put("waitingToDo",waitingToDo);
            resMap.put("iDo",iDo);

            return AjaxResponse.ajaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                    GlobalConfig.ResponseCode.SUCCESS.getDesc(), resMap);
        } catch (Exception e) {
            return AjaxResponse.ajaxData(GlobalConfig.ResponseCode.ERROR.getCode(),
                    "高亮历史历史失败", e.toString());
        }
    }

}
