package com.ewy.activiti7.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
public interface ActivitiMapper {

    // 读取表单
    @Select("SELECT Control_ID_,Control_VALUE_ FROM formdata WHERE PROC_INST_ID_ = #{PROC_INST_ID}")
    List<HashMap<String,Object>> findFormData(@Param("PROC_INST_ID")String PROC_INST_ID);

    // 新增表单
    @Insert("<script> insert into formdata (PROC_DEF_ID_,PROC_INST_ID_,FORM_KEY_,Control_ID_,Control_VALUE_)"
            + "values"
            + "<foreach collection=\"maps\" item=\"formData\" index=\"index\" separator=\",\">"
            + "(#{formData.PROC_DEF_ID_,jdbcType=VARCHAR},#{formData.PROC_INST_ID_,jdbcType=VARCHAR},"
            + "#{formData.FORM_KEY_,jdbcType=VARCHAR},#{formData.Control_ID_,jdbcType=VARCHAR},"
            + "#{formData.Control_VALUE_,jdbcType=VARCHAR})"
            + "</foreach>"
            + "</script>")
    int insertFormData(@Param("maps")List<HashMap<String,Object>> maps);

    // 获取用户列表
    @Select("SELECT name,username FROM user")
    List<HashMap<String,Object>> findUser();

    // 流程定义数
    // SELECT COUNT(ID_) FROM ACT_RE_PROCDEF
    // 进行中的流程实例
    // SELECT COUNT(DISTINCT e.PROC_INST_ID_) FROM act_ru_execution
    // 查询流程定义产生的流程实例数
     /* SELECT p.NAME,COUNT(DISTINCT e.PROC_INST_ID_) AS PiNUM FROM act_ru_execution AS ex
     RIGHT JOIN ACT_RE_PROCDEF AS p ON e.PROC_DEF_ID_ = p.ID_
     WHERE p.NAME_ IS NOT NULL GROUP BY p.NAME_ */
}
