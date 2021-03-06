package com.doubleganse.crawler.processor;

import com.doubleganse.crawler.pipeline.TextFilePipeline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 新笔趣阁小说网小说抓取
 *
 * @auth mingjun chen
 * @date 2016/12/25
 */
public class XinBiQuGePageProcessor implements PageProcessor, Runnable {

	private static final Logger LOGGER = LoggerFactory.getLogger(XinBiQuGePageProcessor.class);
	private static boolean firstFlag = true;

	public void process(Page page) {
		LOGGER.info("processing page\t" + page.getRequest().getUrl());
		// 这里是从小说目录页开始抓取，所以只需要将目录页所有符合条件的url加入targetRequest就行
		if (firstFlag) {
			page.putField("fileName", page.getHtml().xpath("//div[@id='info']/h1/tidyText()").toString().trim());
			LOGGER.info("download novel {}", page.getResultItems().get("fileName").toString());
			page.addTargetRequests(page.getHtml().css("div #list").links().all());
			firstFlag = false;
		}
		// 章节名称
		page.putField("chapterName", page.getHtml().xpath("//div[@class='bookname']/h1/tidyText()").toString());
		// 章节内容
		page.putField("content", page.getHtml().xpath("//div[@id='content']/tidyText()").toString());
		// 如果章节名称或章节内容为空则跳过
		if (page.getResultItems().get("fileName") == null && (page.getResultItems().get("chapterName") == null || page.getResultItems().get("content") == null)) {
			LOGGER.warn(page.getRequest().getUrl() + " download chapter or content is null! skip it");
			page.setSkip(true);
		}
	}

	public Site getSite() {
		// 设置错误重复抓取3次，每次抓取间隔1 00ms
		return Site.me()
				.setDomain("https://www.xxbiquge.com/")
				.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.116 Safari/537.36")
				.setRetryTimes(3)
				.setSleepTime(100)
				.setTimeOut(6000);
	}

	private String url;
	private String path;

	/**
	 * @param url  要抓取小说的目录页url
	 * @param path 保存的文件路径
	 */
	public XinBiQuGePageProcessor(String url, String path) {
		this.url = url;
		this.path = path;
	}

	public void start() {
		new Thread(this).start();
	}

	public void run() {
		Spider.create(this)
				.addUrl(url)
				.addPipeline(new TextFilePipeline(path))
				.thread(1)           //todo 这里因为还没解决多线程下章节抓取顺序混乱问题，暂使用单线程，后续改进
				.start();
	}
}
