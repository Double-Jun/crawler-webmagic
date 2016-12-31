package com.doubleganse.crawler.example;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * @auth mingjun chen
 * @date 2016/12/25
 */
public class FirstDemo {

	public static void main(String[] args) {
		Spider.create(new GitHubRepoPageProcessor())
				.addUrl("http://www.xxbiquge.com/52_52861/")
				//				.addPipeline(new TextFilePipeline("D:\\webmagic\\", "寒门状元.txt"))
				//				.addPipeline(new ConsolePipeline())
				.thread(1)
				.run();
	}
}

class GitHubRepoPageProcessor implements PageProcessor {

	private Site site = Site.me().setRetryTimes(3).setSleepTime(200);

	private boolean flag = true;

	public void process(Page page) {
		if (flag) {
//			System.out.println("page:\t" + page.getRequest().getUrl());
			page.addTargetRequests(page.getHtml().css("div #list").links().all());
			flag = false;
		}
		page.putField("bookname", page.getHtml().xpath("//div[@class='bookname']/h1/tidyText()").toString());
		//		page.putField("content", page.getHtml().xpath("//div[@id='content']/tidyText()").toString());
		if (page.getResultItems().get("bookname") == null) {
			page.setSkip(true);
		}
//		System.out.println(page.getResultItems().get("bookname"));
		System.out.println("thread:" + Thread.currentThread().getName());
	}

	public Site getSite() {
		return site;
	}
}