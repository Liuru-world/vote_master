package com.ewy.activiti7.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
public interface BussinessFormMapper {
    // 修改为已处理状态
    @Update("update bussiness_form set status = 1 where my_instanceid = #{myinstanceid,jdbcType=VARCHAR} and task_key != #{taskKey,jdbcType=VARCHAR}")
    int updateBussinessForm(@Param("myinstanceid")String myinstanceid,@Param("taskKey")String taskKey);

    // 查询数据
    @Select("select count(bform_id) from bussiness_form where my_instanceid = #{myinstanceid,jdbcType=VARCHAR} and task_key = #{taskKey,jdbcType=VARCHAR}")
    int findBussinessFormCount(@Param("myinstanceid")String myinstanceid,@Param("taskKey")String taskKey);


    // 查询数据
    @Select("select count(bform_id) from bussiness_form where form_name = #{formName,jdbcType=VARCHAR}")
    int findBussinessFormCount2(@Param("formName")String formName);


    // 查询数据
    @Select("select task_key,form_name,if(status=1,'已处理','未处理') as status from bussiness_form where my_instanceid = #{myinstanceid,jdbcType=VARCHAR} group by task_key,form_name,status")
    List<HashMap<String,Object>> findBussinessForm(@Param("myinstanceid")String myinstanceid);

    // 生成业务表单
    @Insert("<script> insert into bussiness_form (column_name,column_value,form_name,my_instanceid,task_key)"
            + "values"
            + "<foreach collection=\"listMap\" item=\"formData\" index=\"index\" separator=\",\">"
            + "(#{formData.column_name,jdbcType=VARCHAR},#{formData.column_value,jdbcType=VARCHAR}," +
            "#{formData.form_name,jdbcType=VARCHAR},#{formData.my_instanceid,jdbcType=VARCHAR}," +
            "#{formData.task_key,jdbcType=VARCHAR})"
            + "</foreach>"
            + "</script>")
    int insertBussinessForm(@Param("listMap") List<HashMap<String,Object>> listMap);


}
