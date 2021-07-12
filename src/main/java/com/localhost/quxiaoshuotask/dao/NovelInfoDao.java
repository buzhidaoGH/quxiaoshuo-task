package com.localhost.quxiaoshuotask.dao;

import com.localhost.quxiaoshuotask.domain.NovelInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

//NovelInfo的Dao层
@Repository
public interface NovelInfoDao {
	//插入新的NovelInfo
	@Insert("INSERT INTO `quxiaoshuo`.`novelinfo` (`title`,`novelkey`,`author`,`image`,`chapters`,`url`,`description`,`update`,`status`,`category`) VALUES (#{novelInfo.title},#{novelInfo.novelkey},#{novelInfo.author},#{novelInfo.image},#{novelInfo.chapters},#{novelInfo.url},#{novelInfo.description},#{novelInfo.update},#{novelInfo.status},#{novelInfo.category})")
	public void createNovelInfo(@Param("novelInfo") NovelInfo novelInfo);

	//查询NovelInfo是否存在
	@Select("SELECT COUNT(*) FROM novelinfo WHERE novelkey = #{novelkey}")
	public Boolean selectNovelInfoIsExist(@Param("novelkey")Long novelkey);
}
