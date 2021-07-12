package com.localhost.quxiaoshuotask.task;

import com.localhost.quxiaoshuotask.domain.ChapterInfo;
import com.localhost.quxiaoshuotask.domain.NovelInfo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class NovelInfoProcessor implements PageProcessor, CommandLineRunner {

	@Override
	public void process(Page page) {
		// 判断是否为小说详情页面
		List<Selectable> list = page.getHtml().css("div#list dd").nodes();
		List<Selectable> exit = page.getHtml().css("div.box_con").nodes();
		String flag = page.getHtml().css("title").toString();
		if (flag.contains("错误")) {
			System.out.println("跳过");
			// page.setSkip(true);
			spider.stop();
		}
		if (exit.size() != 0) {//不等于0,是小说详情页面
			try {
				Html pageHtml = page.getHtml();
				//标题
				String title = pageHtml.css("meta[property=og:novel:book_name]").css("meta", "content").toString();
				//小说id
				String id = page.getUrl().toString().split("_")[1];
				//image
				String image = pageHtml.css("meta[property=og:image]").css("meta", "content").toString();
				//作者
				String author = pageHtml.css("meta[property=og:novel:author]").css("meta", "content").toString();
				//小说类型
				String category = pageHtml.css("meta[property=og:novel:category]").css("meta", "content").toString();
				//URL
				String url = pageHtml.css("meta[property=og:url]").css("meta", "content").toString();
				//更新时间
				// DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pageHtml.css("meta[property=og:url]").css("meta", "content").toString());
				Date updateDate = null;
				try {
					updateDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(pageHtml.css("meta[property=og:novel:update_time]").css("meta", "content").toString());
				} catch (ParseException e) {
					page.setSkip(true);
					e.printStackTrace();
				}
				//状态
				String statusStr = pageHtml.css("meta[property=og:novel:status]").css("meta", "content").toString();
				int status = 1;
				if ("连载".equals(statusStr)) {
					status = 0;
				}
				//简介
				String description = pageHtml.css("meta[property=og:description]").css("meta", "content").toString();
				//一共多少章
				List<String> chapters = pageHtml.css("div#list dl dd").all();
				int size = chapters.size();
				if (chapters==null){
					size=0;
					page.setSkip(true);
				}
				int ibg;
				if (size <= 18) {
					size /= 2;
					ibg = size;
				} else {
					size -= 9;
					ibg = 9;
				}

				//获取数据,封装到对象中
				//System.out.println(title + " : " + image + " : " + id + " : " + size + " : " + status + " : " + updateDate + " : " + url + " : " + author);
				NovelInfo novelInfo = new NovelInfo();
				novelInfo.setTitle(title);
				novelInfo.setNovelkey(Long.valueOf(id));
				novelInfo.setAuthor(author);
				novelInfo.setChapters(Long.valueOf(size));
				novelInfo.setUrl(url);
				novelInfo.setDescription(description);
				novelInfo.setUpdate(updateDate);
				novelInfo.setStatus(status);
				novelInfo.setImage(image);
				novelInfo.setCategory(category);
				// System.out.println(novelInfo);

				//获取详情页面的章节超链接
				// for (int i = ibg; i < list.size(); i++) {
				// 	//获取url地址
				// 	String chapterUrl = list.get(i).links().toString();
				// 	page.addTargetRequest(chapterUrl);
				// }

				//下一部小说路径
				String nextUrl = "http://www.biquge.tv/0_" + (Long.valueOf(id) + 1);
				page.addTargetRequest(nextUrl);

				//把小说详情页面结果保存起来
				page.putField("novelInfo", novelInfo);
			} catch (Exception e) {
				page.setSkip(true);
				e.printStackTrace();
			}
		}
		// else {//章节页
		// 	saveChapterInfo(page);
		// }
	}

	//解析章节页面,保存
	private void saveChapterInfo(Page page){
		Html pageHtml = page.getHtml();
		Document parse =  pageHtml.getDocument();
		//小说章节的url
		String chapterUrl = page.getUrl().toString();
		//属于小说的id
		Integer novelId = Integer.valueOf(page.getUrl().toString().split("_")[1].split("/")[0]);
		//小说章节的标题
		String chapterTitle = parse.select(".bookname h1").text();
		//小说的内容content
		String content = parse.select("#content").text();

		ChapterInfo chapterInfo = new ChapterInfo();
		chapterInfo.setTitle(chapterTitle);
		chapterInfo.setNovelkey(novelId);
		chapterInfo.setFilepath(chapterUrl);
		chapterInfo.setContent(content);
		chapterInfo.setWords(content.length());
		System.out.println(chapterInfo);
	}

	/**
	 * site是对象
	 * Site是站点的配置
	 * 可以返回默认配置
	 */
	private Site site = Site.me()
			.setCharset("gbk")//编码格式
			.setRetryTimes(2)//重试次数
			.addHeader("Referer", "http://www.biquge.tv/")//设置跳转前页面
			.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.106 Safari/537.36")
			.setTimeOut(30*1000)//超时时间3s
			.setRetryTimes(2000)//重试间隔
			.setSleepTime(300);//两次间隔

	@Override
	public Site getSite() {
		return site;
	}

	@Autowired
	private NovelInfoPipeline novelInfoPipeline;

	private static Spider spider = Spider.create(new NovelInfoProcessor())
			//设置起始url
			.thread(14);
	//.addUrl("http://www.biquge.tv/0_63352/","http://www.biquge.tv/0_63351/");

	@Override
	public void run(String... args) throws Exception {
		System.out.println("开始执行一次");
		String url = "http://www.biquge.tv/0_66152";//初始
		// http://www.biquge.tv/0_66105/
		// for (int i = 66152; i <= 66154; i++) {
		// 	String realUrl = url + String.valueOf(i);
		// 	spider.addUrl(realUrl);
		// }
		spider.addUrl(url);
		spider.addPipeline(this.novelInfoPipeline);
		spider.start();
		//启动爬虫(同步方法,当前线程中执行,完成此次进行下一次);start同时进行(新建线程)
		// .run();
		// .start();
	}

	//initialDelay任务启动后,等待多久后执行
	//fixedDelay,间隔100秒执行一次
	//@Scheduled(initialDelay = 1000,fixedDelay = 100*1000)
	public void process() {
		System.out.println("执行了");
	}
}
