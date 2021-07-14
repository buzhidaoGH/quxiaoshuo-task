package com.localhost.quxiaoshuotask.task;

import com.localhost.quxiaoshuotask.domain.ChapterInfo;
import com.localhost.quxiaoshuotask.domain.NovelInfo;
import com.localhost.quxiaoshuotask.service.ChapterInfoService;
import com.localhost.quxiaoshuotask.service.NovelInfoService;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;

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
		// 判断数据是否不为空
		if (novelInfo != null) {// 如果不为空就保存到数据库中
			this.novelInfoService.saveNovelInfo(novelInfo);
		}


		// 获取封装好的章节详情
		ChapterInfo chapterInfo = new ChapterInfo();
		System.out.println("进来了");
		Integer novelKey = Integer.parseInt(resultItems.get("novelKey"));
		List<Selectable> chapters = resultItems.get("chapters");
		int size = chapters.size();
		if (chapters == null) {
			size = 0;
		}
		int ibg;
		if (size <= 18) {
			size /= 2;
			ibg = size;
		} else {
			size -= 9;
			ibg = 9;
		}
		//System.out.println(chapters);
		for (int i = ibg; i < chapters.size(); i++) {
			String chapterLink = chapters.get(i).links().toString();
			String chapterTitle = Jsoup.parse(chapters.get(i).toString()).select("a").text();
			chapterInfo.setTitle(chapterTitle);
			chapterInfo.setNovelkey(novelKey);
			chapterInfo.setFilepath(chapterLink);
			String[] split1 = chapterLink.split("/");
			Integer weight= Integer.parseInt(split1[split1.length - 1].split("\\.")[0]);
			// Integer weight = Integer.parseInt(split[split.length - 1].split(".")[0]);
			chapterInfo.setWeight(weight);
			System.out.println(chapterInfo);
		}
		System.out.println(novelKey + " : " + novelKey / 1000);

	}
}
