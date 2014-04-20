package com.taobao.eclipse.plug.autotest.service;

import java.util.Map;

import com.taobao.eclipse.plug.autotest.config.Configure;
import com.taobao.eclipse.plug.autotest.exception.GenException;
import com.taobao.eclipse.plug.autotest.manager.DefaultCodeExportManager;
import com.taobao.eclipse.plug.autotest.manager.ICodeExportManager;
import com.taobao.eclipse.plug.autotest.util.FileUtil;

/**
 * @author junyu.wy
 * 
 */
public class CodeExportService {
	private Configure conf = null;
	ICodeExportManager dcem = new DefaultCodeExportManager();
	public CodeExportService(Configure conf) {
		this.conf = conf;
	}

	public void export2File(Map<String,Object> codeInfo,String fileName){
		String content;
		try {
			content = dcem.genFileContent(codeInfo);
			generateFileToOutputDir(fileName,content);
			conf.refreshFolder();
		} catch (GenException e) {
			System.err.println("error occur while exporting java : "+fileName);
			e.printStackTrace();
		}
	}



	/**
	 * 生成测试用例文件
	 * @param fileName 文件名
	 * @param content  文件内容
	 */
	private void generateFileToOutputDir(String fileName,String content){
		try{
			FileUtil.createFile(conf.getOutputDir(),fileName,content);
		}catch(Exception e){
			System.err.println("error when write to file "+fileName);
			e.printStackTrace();
		}
	}
}
