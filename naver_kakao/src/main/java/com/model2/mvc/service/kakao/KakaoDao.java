package com.model2.mvc.service.kakao;
import org.apache.ibatis.annotations.Mapper;

import com.model2.mvc.service.domain.User;
@Mapper
public interface KakaoDao {
	
	public void addUser(User user) throws Exception ;
	
	public User getUser(String userId) throws Exception;
}
