package com.localhost.quxiaoshuotask.service.impl;

import com.localhost.quxiaoshuotask.dao.ChapterInfoDao;
import com.localhost.quxiaoshuotask.domain.ChapterInfo;
import com.localhost.quxiaoshuotask.service.ChapterInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChapterInfoServiceImpl implements ChapterInfoService {
	@Autowired
	private ChapterInfoDao chapterInfoDao;

	@Override
	public void saveChapterInfo(ChapterInfo chapterInfo) {
		chapterInfoDao.createChapterInfo(chapterInfo);
	}
}
