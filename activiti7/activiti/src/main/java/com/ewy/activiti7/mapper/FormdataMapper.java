package com.ewy.activiti7.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
public interface FormdataMapper {
    // 表单历史参数
    @Select(" select FORM_KEY_,Control_LABEL_,Control_VALUE_" +
            "    from formdata where PROC_INST_ID_ = #{PROC_INST_ID,jdbcType=VARCHAR}" +
            "    group by FORM_KEY_,Control_LABEL_,Control_VALUE_,form_id order by form_id asc")
    List<HashMap<String,Object>> findFormVariables(@Param("PROC_INST_ID")String PROC_INST_ID);


}
