package com.taobao.eclipse.plug.autotest.Generate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

import com.taobao.eclipse.plug.autotest.config.Configure;
import com.taobao.eclipse.plug.autotest.exception.ConfigException;
import com.taobao.eclipse.plug.autotest.service.CodeExportService;
import com.taobao.eclipse.plug.autotest.service.SourceInfoService;
import com.taobao.eclipse.plug.autotest.service.TestCaseGenerateService;

/**
 * @author junyu.wy
 * 
 */
public class Generater {
	private Configure conf = null;
	private TestCaseGenerateService tcg = null;
	private SourceInfoService sourceManager = null;
	private CodeExportService ces = null;

	private List<String> exportTestClass = new ArrayList<String>();

	public Generater() {
	}

	public List<String> exportBatchTestCase(String daoDir, String daoPackgeString, IProject daoPproject, String testDaoDir, String testDaoPackge, IProject testDdaoPproject,String baseTestClass) throws Exception {

		List<String> files = new ArrayList<String>();

			this.init(daoDir, daoPackgeString, daoPproject, testDaoDir, testDaoPackge, testDdaoPproject);
			List<IType> classes = findAllSouceClasses();
			Map<String, Object> tmpInfo = null;
			for (IType clazz : classes) {
				tmpInfo = genTestCase(tmpInfo,files,clazz,baseTestClass);
			}
			genBaseTest(tmpInfo,files);

		return files;
	}

	public List<String> exportSingleTestCase(String javaFile, IProject daoPproject, String testDaoDir, String testDaoPackge, IProject testDdaoPproject,String baseTestClass) throws CoreException {

		List<String> files = new ArrayList<String>();

		try {
			this.init(javaFile, daoPproject, testDaoDir, testDaoPackge, testDdaoPproject);
			IType clazz = findSingleSouceClasses();
			Map<String, Object> tmpInfo = null;
			tmpInfo = genTestCase(tmpInfo,files,clazz,baseTestClass);
			genBaseTest(tmpInfo,files);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return files;
	}
	
	private void genBaseTest(Map<String, Object> tmpInfo,List<String> files){
		if (tmpInfo != null) {
			tmpInfo.clear();
			tmpInfo.put("packageStr", this.conf.getTestDaoPackge());
			tmpInfo.put("testClassName", "BaseTest");
			this.export2File(tmpInfo, "BaseTest.java");
			files.add(conf.getTestDaoPackge().replaceAll("\\.","/") + ".BaseTest.java");
		}
	}
	
	private Map<String, Object> genTestCase(Map<String, Object> tmpInfo,List<String> files,IType clazz,String baseTestClass) throws Exception{
		tmpInfo = this.generateTestCase(clazz);
		if (tmpInfo != null) {
			tmpInfo.put("baseTestClass", baseTestClass);
			String javaFileName = null;
			String simpleName = clazz.getElementName();
			if (this.exportTestClass.isEmpty() || this.exportTestClass.contains(simpleName)) {
				javaFileName = simpleName + "Test.java";
				this.export2File(tmpInfo, javaFileName);
				files.add(conf.getTestDaoPackge().replaceAll("\\.","/")  + javaFileName);
			}
		}
		return tmpInfo;
	}
	
	/**
	 * 获取需要测试的类 批量
	 * 
	 * @return
	 */
	private List<IType> findAllSouceClasses() {
		List<IType> ls = null;
		ls = sourceManager.findAllSouceClasses();
		return ls;
	}

	/**
	 * 获取需要测试的类 单个
	 * 
	 * @return
	 */
	private IType findSingleSouceClasses() {
		return sourceManager.findSingleSouceClasses();
	}

	/**
	 * 生成测试用例
	 * 
	 * @param classes
	 * @throws JavaModelException
	 */
	private Map<String, Object> generateTestCase(IType clazz) throws Exception {
		return tcg.generateTestCase(clazz);
	}

	/**
	 * 生成测试用例
	 * 
	 * @param classes
	 */
	private void export2File(Map<String, Object> classInfo, String fileName) {
		ces.export2File(classInfo, fileName);
	}

	/**
	 * 初始化配置
	 * 
	 * @param args
	 * @throws CoreException
	 */
	protected void init(String javaFile, IProject project, String testDir, String testPackge, IProject testProject) throws ConfigException, CoreException {
		conf = new Configure(javaFile, project, testDir, testPackge, testProject);
		conf.load();

		sourceManager = new SourceInfoService(conf);
		tcg = new TestCaseGenerateService(conf);
		ces = new CodeExportService(conf);
	}

	/**
	 * 初始化配置
	 * 
	 * @param args
	 * @throws CoreException
	 */
	protected void init(String daoDir, String daoPackgeString, IProject daoPproject, String testDaoDir, String testDaoPackge, IProject testDdaoPproject) throws ConfigException, CoreException {
		conf = new Configure(daoDir, daoPackgeString, daoPproject, testDaoDir, testDaoPackge, testDdaoPproject);
		conf.load();

		sourceManager = new SourceInfoService(conf);
		tcg = new TestCaseGenerateService(conf);
		ces = new CodeExportService(conf);
	}
}
