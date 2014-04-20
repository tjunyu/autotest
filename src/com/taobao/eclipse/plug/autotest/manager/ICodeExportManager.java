package com.taobao.eclipse.plug.autotest.manager;

import java.util.Map;

import com.taobao.eclipse.plug.autotest.exception.GenException;
/**
 * @author junyu.wy
 * 
 */
public interface ICodeExportManager {

	/**
	 * 根据取得的变量生成文件内容
	 * @return
	 * @throws GenException
	 */
	public String genFileContent(Map<String,?> map) throws GenException ;

}
