package com.doubleganse.crawler;

import com.doubleganse.crawler.processor.XinBiQuGePageProcessor;

/**
 * 将这里作为入口
 */
public class App {

	public static void main(String[] args) {
		// 这个地方可以使用多线程方式同时抓取多本小说
//		Spider.create(new XinBiQuGeBatchPageProcessor())
//				.addUrl("http://www.xxbiquge.com/xclass/4/1.html")
//				.thread(1)
//				.start();


		new XinBiQuGePageProcessor("http://www.xxbiquge.com/75_75927/", "d:/webmagic/", "大明闲人.txt").start();
	}
}
