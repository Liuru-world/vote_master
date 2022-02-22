package com.ewy.activiti7.security;

import com.ewy.activiti7.entity.UserInfoBean;
import com.ewy.activiti7.mapper.UserInfoBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    UserInfoBeanMapper userInfoBeanMapperl;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfoBean userInfoBean = userInfoBeanMapperl.selectByUsername(username);
        if (userInfoBean == null){
            throw new UsernameNotFoundException("数据库中无此用户！");
        }
        return userInfoBean;
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
