package com.taobao.eclipse.plug.autotest.manager.base;

/**
 * 定义基本测试方法用到属性值
 * @author junyu.wy
 */
public class BaseMethodBean {

	String testFuncName = null;     //测试方法名
	String name=null;               //方法名
	String before_doMethod=null;    //执行方法前的操作
	String parameter=null;          //方法参数串

	public String getTestFuncName() {
		return testFuncName;
	}
	public void setTestFuncName(String testFuncName) {
		this.testFuncName = testFuncName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
		testFuncName="test"+name.toUpperCase().charAt(0)+name.substring(1);
	}
	public String getBefore_doMethod() {
		return before_doMethod;
	}
	public void setBefore_doMethod(String beforeDoMethod) {
		before_doMethod = beforeDoMethod;
	}
	public String getParameter() {
		return parameter;
	}
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
}
