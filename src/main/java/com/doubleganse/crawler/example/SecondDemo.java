package com.doubleganse.crawler.example;

import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.ConsolePageModelPipeline;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

/**
 * @auth mingjun chen
 * @date 2016/12/25
 */
public class SecondDemo {

	public static void main(String[] args) {
		OOSpider.create(Site.me().setSleepTime(1000)
				, new ConsolePageModelPipeline()
				, BookRepo.class)
				.addUrl("http://www.xxbiquge.com/66_66018/3409836.html")
				.thread(5)
				.run();
	}
}

@HelpUrl("http://www.xxbiquge.com/66_66018/")
@TargetUrl("")
class BookRepo {

	@ExtractBy("//div[@class='bookname']/h1/tidyText()")
	private String bookName;

	@ExtractBy("//div[@id='content']/tidyText()")
	private String content;
}