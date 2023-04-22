package com.eatsTime.main.dao;

import org.apache.ibatis.session.SqlSession;
import com.eatsTime.mybatis.config.MyBatisConfig;

public class MainDAO {
	public SqlSession sqlSession;

	public MainDAO() {
      sqlSession = MyBatisConfig.getSqlSessionFactory().openSession(true);
	}

}