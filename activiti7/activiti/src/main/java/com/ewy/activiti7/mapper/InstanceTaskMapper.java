package com.ewy.activiti7.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
public interface InstanceTaskMapper {
    // 启动实例同时增加记录，此表用于读取自定义唯一实例ID
//    @Insert("<script> insert into instance_task (instanceid,instancename)"
//            + "values"
//            + "<foreach collection=\"listMap\" item=\"formData\" index=\"index\" separator=\",\">"
//            + "(#{formData.id,jdbcType=VARCHAR},#{formData.name,jdbcType=VARCHAR})"
//            + "</foreach>"
//            + "</script>")
//    int inInstanceTask(@Param("listMap")List<HashMap<String,Object>> listMap);

    @Insert("insert into instance_task (instanceid,instancename,byusername) values (#{instanceid,jdbcType=VARCHAR},#{instancename,jdbcType=VARCHAR},#{byusername,jdbcType=VARCHAR})")
    int inInstanceTask(@Param("instanceid")String instanceid,@Param("instancename")String instancename,@Param("byusername") String byusername);

    // 删除实例同时删除记录，此表用于读取自定义唯一实例ID
    @Delete("delete from instance_task where instanceid = #{instanceid,jdbcType=VARCHAR}")
    int delInstanceTask(@Param("instanceid")String instanceid);

    // 根据唯一实例ID获取自定义的唯一实例ID
    @Select("select concat('INST',myinstanceid,'by',byusername)" +
            "from instance_task where instanceid = #{instanceid,jdbcType=VARCHAR}")
    String fiInstanceTask(@Param("instanceid")String instanceid);

    // 根据唯一实例ID获取自定义的唯一实例ID
    @Select("select concat('INST',myinstanceid,'by',byusername)" +
            "from instance_task where instanceid like concat(#{instanceid,jdbcType=VARCHAR},'%')")
    String fiInstanceTas2(@Param("instanceid")String instanceid);
}
