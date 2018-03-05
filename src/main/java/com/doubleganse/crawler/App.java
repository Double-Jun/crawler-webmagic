package com.doubleganse.crawler;

import com.doubleganse.crawler.processor.XinBiQuGePageProcessor;

/**
 * 将这里作为入口
 */
public class App {

	public static void main(String[] args) {

		new XinBiQuGePageProcessor("https://www.xxbiquge.com/80_80027/", "d:\\webmagic\\").start();

	}
}
