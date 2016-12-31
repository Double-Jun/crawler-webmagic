package com.doubleganse.crawler.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * @auth mingjun chen
 * @date 2016/12/25
 */
public class XinBiQuGeBatchPageProcessor implements PageProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(XinBiQuGeBatchPageProcessor.class);
	private boolean firstFlag = true;

	public void process(Page page) {
		if (firstFlag) {
			page.addTargetRequests(page.getHtml().xpath("//div[@id='newscontent']/div[@class='r']").links().all());
			firstFlag = false;
		}
		String bookName = page.getHtml().xpath("//div[@id='info']/h1/tidyText()").toString();
		String author = page.getHtml().xpath("//div[@id='info']/p/tidyText()").toString();
		if (bookName != null && author != null) {
			bookName = bookName.trim().replaceAll("\\n", "");
			author = author.trim().replaceAll("\\n", "");
			String[] split = author.split("：");
			if (split.length > 1) {
				author = split[1];
			}
			LOGGER.info("new thread start to download " + bookName);
			new XinBiQuGePageProcessor(page.getRequest().getUrl(), "d:/webmagic/历史军事/", (bookName + "(" + author + ")") + ".txt").start();
		}
	}

	public Site getSite() {
		return Site.me().setRetryTimes(3).setSleepTime(100);
	}
}
