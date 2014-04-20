package com.taobao.eclipse.plug.autotest.service;

import java.util.Map;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

import com.taobao.eclipse.plug.autotest.config.Configure;
import com.taobao.eclipse.plug.autotest.manager.ClassGenRuleResolve;
/**
 * @author junyu.wy
 * 
 */
public class TestCaseGenerateService {

	private Configure conf = null;
	public TestCaseGenerateService(Configure conf) {
		this.conf = conf;
	}

	public Map<String,Object> generateTestCase(IType clazz) throws Exception{

		return createTestCase(clazz);
	}

	/**
	 * 生成测试代码信息
	 * @param clazz 测试类
	 * @return Map  测试类模板信息
	 * @throws JavaModelException 
	 */
	private Map<String,Object> createTestCase(IType clazz) throws Exception{
		Map<String,Object> javaFileInfo = null;
		ClassGenRuleResolve resolve = new ClassGenRuleResolve(conf.getType());
			javaFileInfo = resolve.getGenContent(clazz,conf.getTestDaoPackge());
		return javaFileInfo;
	}

	public static void main(String[] args){
//		Configure conf = new Configure();
//		try {
//			conf.load();
//		} catch (ConfigException e) {
//			e.printStackTrace();
//		}
//		TestCaseGenerateService tcg = new TestCaseGenerateService(conf);
//		tcg.generateTestCases(com.dsp.biz.dal.daoibatis.IbatisEventDAO.class);
	}

}
