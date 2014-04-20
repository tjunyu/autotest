package com.taobao.eclipse.plug.autotest.util;

import java.util.Map;
/**
 * @author junyu.wy
 * 
 */
public class VmResourceUtil {

	private static final String packageDirName    = "com/taobao/eclipse/plug/autotest/resource";
	protected final static String BASE_VM_PATH    = packageDirName+"/JunitTestMain.vm";
	protected final static String DAO_VM_PATH     = packageDirName+"/JunitTestMain.vm";
	protected final static String BASETEST_VM_PATH = packageDirName+"/BaseTest.vm";

	/**
	 * 根据取得的class信息生成文件内容
	 * @return
	 * @throws GenException
	 */
	public static String findVmPathByClassInfo(Map<String,?> info){
		String path = "";
		String testClassName = info.get("testClassName").toString();
		if(testClassName.indexOf("DAO")!=-1){
			path = DAO_VM_PATH ;
		}else if(testClassName.indexOf("BaseTest")!=-1){
			path = BASETEST_VM_PATH ;
		}else {
			path = BASE_VM_PATH ;
		}
		return path;
	}
}
