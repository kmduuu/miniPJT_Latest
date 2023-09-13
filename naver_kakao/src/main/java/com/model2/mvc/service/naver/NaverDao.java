package com.model2.mvc.service.naver;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Repository;
import com.model2.mvc.service.domain.User;

@Mapper
public interface NaverDao {

	public void addUser(User user) throws Exception;
	
	public User getUser(String userId) throws Exception;

}
