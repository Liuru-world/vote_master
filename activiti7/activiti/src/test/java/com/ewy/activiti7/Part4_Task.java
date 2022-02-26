package com.ewy.activiti7;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class Part4_Task {

    @Autowired
    private TaskService taskService;

    //任务查询
    @Test
    public void getTasks(){
        List<Task> list = taskService.createTaskQuery().list();
        for(Task tk : list){
            System.out.println("Id："+tk.getId());
            System.out.println("Name："+tk.getName());
            System.out.println("Assignee："+tk.getAssignee());
        }
    }

    //查询我的代办任务
    @Test
    public void getTasksByAssignee(){
        List<Task> list = taskService.createTaskQuery()
                .taskAssignee("tangseng")
                .list();
        for(Task tk : list){
            System.out.println("Id："+tk.getId());
            System.out.println("Name："+tk.getName());
            System.out.println("Assignee："+tk.getAssignee());
        }
    }

    //执行任务
    @Test
    public void completeTask(){
        taskService.complete("b898a05e-8ef3-11ec-829b-4af45b9a5db0");
        taskService.complete("b898a060-8ef3-11ec-829b-4af45b9a5db0");
        System.out.println("完成任务");

    }

    //拾取任务
    @Test
    public void claimTask(){
        Task task = taskService.createTaskQuery().taskId("b846a87d-8be6-11ec-a653-4e89443e7ff0").singleResult();
        taskService.claim("b846a87d-8be6-11ec-a653-4e89443e7ff0","bajie");
    }

    //归还与交办任务
    @Test
    public void setTaskAssignee(){
        Task task = taskService.createTaskQuery().taskId("b846a87d-8be6-11ec-a653-4e89443e7ff0").singleResult();
//        taskService.setAssignee("b846a87d-8be6-11ec-a653-4e89443e7ff0","null");//归还候选任务
        taskService.setAssignee("b846a87d-8be6-11ec-a653-4e89443e7ff0","wukong");//交办
    }



}
