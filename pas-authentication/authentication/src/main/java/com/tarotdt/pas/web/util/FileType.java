package com.tarotdt.pas.web.util;

import com.alibaba.fastjson.JSON;

/**
 * 文件类型接口
 * @author lir
 *
 */
public interface FileType {
	/**
	 * 文件内容处理成json
	 * @param fileName 文件路径加文件名称 例：D:/Temp/test.json
	 * @return  Json
	 * @throws Exception 
	 */
	public JSON handleFileToJson(String fileName) throws Exception;
	
	/**
	 * 取得头文件
	 * @param fileName 文件路径加文件名称 例：D:/Temp/test.json
	 * @return 头文件Json
	 * @throws Exception
	 */
	public JSON getHead(String fileName) throws Exception;
}
