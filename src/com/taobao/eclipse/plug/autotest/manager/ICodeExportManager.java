package com.taobao.eclipse.plug.autotest.manager;

import java.util.Map;

import com.taobao.eclipse.plug.autotest.exception.GenException;
/**
 * @author junyu.wy
 * 
 */
public interface ICodeExportManager {

	/**
	 * ����ȡ�õı��������ļ�����
	 * @return
	 * @throws GenException
	 */
	public String genFileContent(Map<String,?> map) throws GenException ;

}
