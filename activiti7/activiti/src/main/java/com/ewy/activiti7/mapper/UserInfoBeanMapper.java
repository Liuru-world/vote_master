package com.ewy.activiti7.mapper;

import com.ewy.activiti7.entity.UserInfoBean;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Component
public interface UserInfoBeanMapper {

    @Select("select * from user where username = #{username}")
    UserInfoBean selectByUsername(@Param("username")String username);
}
