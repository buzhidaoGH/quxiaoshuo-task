package com.localhost.quxiaoshuotask.task;

import com.localhost.quxiaoshuotask.domain.ChapterInfo;
import com.localhost.quxiaoshuotask.domain.NovelInfo;
import com.localhost.quxiaoshuotask.service.ChapterInfoService;
import com.localhost.quxiaoshuotask.service.NovelInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

@Component
public class NovelInfoPipeline implements Pipeline {
	@Autowired
	private NovelInfoService novelInfoService;
	@Autowired
	private ChapterInfoService chapterInfoService;

	@Override
	public void process(ResultItems resultItems, Task task) {
		// 获取封装好的小说详情对象
		NovelInfo novelInfo = resultItems.get("novelInfo");
		// 获取封装好的章节详情
		ChapterInfo chapterInfo = resultItems.get("chapterInfo");
		// 判断数据是否不为空
		if (novelInfo != null) {// 如果不为空就保存到数据库中
			this.novelInfoService.saveNovelInfo(novelInfo);
		}
		// 判断是否不为空章节
		if (chapterInfo != null) {
			this.chapterInfoService.saveChapterInfo(chapterInfo);
		}
	}
}
