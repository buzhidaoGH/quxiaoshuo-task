package com.localhost.quxiaoshuotask.service.impl;

import com.localhost.quxiaoshuotask.dao.NovelInfoDao;
import com.localhost.quxiaoshuotask.domain.NovelInfo;
import com.localhost.quxiaoshuotask.service.NovelInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NovelInfoImpl implements NovelInfoService {

	@Autowired
	private NovelInfoDao novelInfoDao;

	@Override
	public void saveNovelInfo(NovelInfo novelInfo) {
		Boolean exit = novelInfoDao.selectNovelInfoIsExist(novelInfo.getNovelkey());
		if (!exit) {//如果不存在则添加
			System.out.println(novelInfo);
			novelInfoDao.createNovelInfo(novelInfo);
		}
	}
}
