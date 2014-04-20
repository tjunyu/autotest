package com.taobao.eclipse.plug.autotest.manager.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

import com.taobao.eclipse.plug.autotest.exception.GenException;
import com.taobao.eclipse.plug.autotest.manager.ClassGenRule;

/**
 * velocity模板生成测试用例文件
 *
 * @author junyu.wy
 */
public class BaseGenRule extends ClassGenRule {

	protected String testClassName = null;    // 测试类类名
	protected String clazz      = null; 		// 原类名 itype
	protected String p_clazz    = null; 		// 变量名
	protected String packageStr = null; 		// 包名
	protected List<BaseMethodBean> methodList   = new ArrayList<BaseMethodBean>();     	//method列表
	protected Map<String,String> testMethodMap = new HashMap<String,String>();
	protected Set<String> importTypes = new HashSet<String>();
	protected Map<String,IField> fieldMap = new HashMap<String,IField>(); 

	@Override
	public Map<String,Object> gen(IType daoClass) throws GenException, JavaModelException {

		// 基本变量解析
		parseClassForBaseInfo(daoClass);

		// 各方法解析 生成内容
		parseClassMethod(daoClass);


		return combineResult();
	}

	protected Map<String,Object> combineResult(){
		Map<String,Object> ctx = new HashMap<String,Object>();
		ctx.put("testClassName", testClassName);
		ctx.put("clazz",clazz);
		ctx.put("p_clazz",p_clazz);
		ctx.put("packageStr",packageStr);
		ctx.put("methodList",methodList);
		ctx.put("importTypes",importTypes.toArray());
		return ctx;
	}

	/**
	 * 获取基本的变量，除方法外的
	 * @param daoClass
	 * @throws JavaModelException 
	 */
	protected void parseClassForBaseInfo(IType daoClass) throws JavaModelException {
		clazz = daoClass.getFullyQualifiedName();
		String simpleClassName = daoClass.getElementName();

		p_clazz = simpleClassName.substring(0,1)
								 .toLowerCase()
								 +simpleClassName.substring(1); //default
		String[] interfaces = daoClass.getSuperInterfaceNames();
		for(int i=0;i<interfaces.length;i++){
			if(simpleClassName.indexOf(interfaces[i])!=-1){
				p_clazz = interfaces[i];       //接口名作为变量名
				p_clazz = p_clazz.substring(0,1).toLowerCase() //首字母小写
				         +p_clazz.substring(1);
			}
		}
		testClassName = simpleClassName+"Test";

		//找getter，过滤测试
		IField[] fields = daoClass.getFields();
		for(int i=0;i<fields.length;i++){
			fieldMap.put(fields[i].getElementName().toUpperCase(), fields[i]);
		}
	}

	/**
	 * 获取生成测试方法所需的变量，过滤不必要测试的方法，需要测试的方法的解析由methodParseDispatch完成
	 * @param daoClass
	 * @return
	 * @throws JavaModelException 
	 */
	protected List<BaseMethodBean> parseClassMethod(IType daoClass) throws JavaModelException {

		List<BaseMethodBean> ls = null;
		IMethod[] methods = daoClass.getMethods();
		int len = methods.length;
		String tmpName = null;
		BaseMethodBean bean=null;

		for(int i=0;i<len;i++){

			//只测试本类pulic的方法
			if(Flags.isPublic(methods[i].getFlags()) || daoClass.isInterface()){

				//不测试setter,getter
				tmpName = methods[i].getElementName();
				if((!tmpName.startsWith("get")
						|| !fieldMap.containsKey(tmpName.substring(3).toUpperCase()))
						&& !tmpName.startsWith("set")){
					bean = methodParseDispatch(methods[i]);
					if(bean!=null){

						//对于overload的要重新命名，但没必要做put
						if(testMethodMap.containsKey(bean.getTestFuncName())){
							bean.setTestFuncName(bean.getTestFuncName()+""+i);
						}else{
							testMethodMap.put(bean.getTestFuncName(),bean.getTestFuncName());
						}
						methodList.add(bean);
					}
				}
			}
		}
		return ls;
	}

	/**
	 * 分发方法解析,parseClassMethod为模板，子类可以对disptch有不同的实现
	 * @param m
	 * @throws JavaModelException 
	 */
	protected BaseMethodBean methodParseDispatch(IMethod m) throws JavaModelException{
		return (BaseMethodBean) defaultMethodParser(m,BaseMethodBean.class);
	}

	public void setPackageStr(String packageStr) {
		this.packageStr = packageStr;
	}

	@Override
	public void initImports(String type) {
		importTypes.add(type);
	}

}
