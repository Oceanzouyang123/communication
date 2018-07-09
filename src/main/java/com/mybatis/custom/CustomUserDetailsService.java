package com.mybatis.custom;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;  
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;  
import org.springframework.security.core.userdetails.UserDetails;  
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.mybatis.controller.RoleController;
import com.mybatis.entity.User;
import com.mybatis.mapper.UserDao;  
/** 
 * 用于加载用户信息 实现UserDetailsService接口，或者实现AuthenticationUserDetailsService接口 
 * @author ChengLi 
 * 
 */  
//实现AuthenticationUserDetailsService，实现loadUserDetails方法  
public class CustomUserDetailsService implements AuthenticationUserDetailsService<CasAssertionAuthenticationToken> {  
    
	@Autowired
	UserDao userDao;
	
	RoleController roleController = new RoleController();
     
    public UserDetails loadUserDetails(CasAssertionAuthenticationToken token) throws UsernameNotFoundException {  
        //System.out.println("当前的用户名是："+token.getName()); 
        User user = new User();
        //查询登录后获取的userId是否为空，不为空则插入本地数据库
        List<User> userList = userDao.getUserByUserId(token.getName());
        if(userList.size()==0){
        	user.setUserId(token.getName());
            user.setStatus("0");
            userDao.insertUser(user);
        }        
        /*这里我为了方便，就直接返回一个用户信息，实际当中这里修改为查询数据库或者调用服务什么的来获取用户信息*/  
        UserInfo userInfo = new UserInfo();  
        userInfo.setUsername(token.getName());  
        userInfo.setName(token.getName());  
        Set<AuthorityInfo> authorities = new HashSet<AuthorityInfo>();  
        AuthorityInfo authorityInfo = new AuthorityInfo("TEST");  
        authorities.add(authorityInfo);  
        userInfo.setAuthorities(authorities);
        return userInfo;  
    }  
  
}  
