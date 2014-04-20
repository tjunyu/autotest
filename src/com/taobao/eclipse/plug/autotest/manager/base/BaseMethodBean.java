package com.taobao.eclipse.plug.autotest.manager.base;

/**
 * ����������Է����õ�����ֵ
 * @author junyu.wy
 */
public class BaseMethodBean {

	String testFuncName = null;     //���Է�����
	String name=null;               //������
	String before_doMethod=null;    //ִ�з���ǰ�Ĳ���
	String parameter=null;          //����������

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
