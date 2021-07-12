package com.localhost.quxiaoshuotask.dao;

import com.localhost.quxiaoshuotask.domain.ChapterInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

//ChapterInfo的Dao层
@Repository
public interface ChapterInfoDao {

	@Update({"create table ${tableName} (" +
			"`id` int(11) NOT NULL COMMENT 'id'," +
			"`title` varchar(256) DEFAULT NULL COMMENT '小说章节名'," +
			"`novelkey` int(11) DEFAULT NULL COMMENT '所属小说id/key'," +
			"`filepath` varchar(1024) DEFAULT NULL COMMENT '章节路径'," +
			"`words` int(11) DEFAULT NULL COMMENT '章节字数'," +
			"`content` text COMMENT '章节内容'," +
			"PRIMARY KEY (`id`)" +
			" ) ENGINE=InnoDB DEFAULT CHARSET=utf8"})
	public void createChapterTable(@Param("tableName") String tableName);

	@Select("select count(*) from information_schema.TABLES where table_name = #{tableName}")
	public Integer findTableNameByName(@Param("tableName") String tableName);

	void createChapterInfo(ChapterInfo chapterInfo);
}
