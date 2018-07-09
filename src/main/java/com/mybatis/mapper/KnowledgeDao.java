package com.mybatis.mapper;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.mybatis.entity.Knowcenter;

/**
 * 
 * @author zouyang
 * Data:2018-5-22
 * 知识库管理
 *
 */

@Transactional
public interface KnowledgeDao {

	//根据id查询知识库信息
	List<Knowcenter> getKnowledgeById(int id);
	
	//查询所有知识库信息
	List<Knowcenter> getKnowledgeList(Knowcenter knowcenter);
	
	//统计总数
	int getCountSqlwhere(Knowcenter knowcenter);
	
	//新增知识库
	int insertKnowledge(Knowcenter knowcenter);
	
	//修改知识库信息
	int updateKnowledge(Knowcenter knowcenter);
		
	//删除知识库
	int delKnowcenter(int id);
	
	//获取最新的10条记录
	List<Knowcenter> getKnowledge();
}
