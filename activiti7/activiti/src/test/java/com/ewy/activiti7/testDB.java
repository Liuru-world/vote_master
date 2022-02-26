package com.ewy.activiti7;

import com.ewy.activiti7.mapper.ActivitiMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class testDB {
    @Autowired
    ActivitiMapper activitiMapper;

    @Test
    public void test1(){
        System.out.println(activitiMapper.countInstanceName("报销流程启动1"));
    }

}
