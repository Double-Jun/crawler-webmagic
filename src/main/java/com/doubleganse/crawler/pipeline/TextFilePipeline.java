package com.doubleganse.crawler.pipeline;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.utils.FilePersistentBase;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @auth mingjun chen
 * @date 2016/12/25
 */
public class TextFilePipeline extends FilePersistentBase implements Pipeline {

	private static final Logger LOGGER = LoggerFactory.getLogger(TextFilePipeline.class);
	private static final String TEXT_FILE_SUFFIX = ".txt";
	private static final String POINT_SEPARATOR = ".";
	private static final String DEFAULT_FILE_NAME = "temp.txt";
	private PrintWriter printWriter;  // 感觉这样效率有点低，最后是先缓存到内存或redis中，最后再一次性写入磁盘

	public TextFilePipeline(String path) {
		new TextFilePipeline(path, null);
	}

	public TextFilePipeline(String path, String fileName) {
		setPath(path);
		try {
			checkAndMakeParentDirecotry(path);
			printWriter = new PrintWriter(new FileWriter(getFile(path + dealFileName(fileName))));
			LOGGER.debug("write " + path + fileName + "starting ... ");
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	public String dealFileName(String fileName) {
		if (StringUtils.isBlank(fileName)) {
			return DEFAULT_FILE_NAME;
		}
		if (fileName.endsWith(TEXT_FILE_SUFFIX)) {
			return fileName;
		}
		int index = fileName.lastIndexOf(POINT_SEPARATOR);
		if (index > -1) {
			return fileName.substring(0, index) + TEXT_FILE_SUFFIX;
		}
		return fileName + TEXT_FILE_SUFFIX;
	}

	// todo 这里的printWriter没有close，可能造成内存泄露。
	public void process(ResultItems resultItems, Task task) {
		LOGGER.info("writing\t" + resultItems.get("chapterName").toString().replaceAll("\\n", "") + " for page " + resultItems.getRequest().getUrl());
		printWriter.write(resultItems.get("chapterName").toString());
		printWriter.write(resultItems.get("content").toString());
	}
}
