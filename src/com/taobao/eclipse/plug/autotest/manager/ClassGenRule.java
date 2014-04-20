package com.taobao.eclipse.plug.autotest.manager;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

import com.taobao.eclipse.plug.autotest.exception.GenException;
import com.taobao.eclipse.plug.autotest.manager.base.BaseMethodBean;
import com.taobao.eclipse.plug.autotest.util.DataUtils;
/**
 * @author junyu.wy
 * 
 */
public abstract class ClassGenRule {

	protected static final String[] funcParameter = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r"};

	public abstract Map<String, Object> gen(IType clazz) throws GenException, JavaModelException;
	public abstract void initImports(String type);

	/**
	 * 生成简单类型或者简单类型的包装类型的定义语句
	 * @param p
	 * @return
	 */
	protected String createParamter(String p,int index){
		String str = "";
		String simpleName = p.substring(p.lastIndexOf(".")+1, p.length());
		if( p.equals("int") || p.equals(Integer.class.getName())){
			str = "int "+funcParameter[index]+" = 1;\r\n";
		}else if( p.equals("short") || p.equals(Short.class.getName())){
			str = "short "+funcParameter[index]+" = 1;\r\n";
		}else if( p.equals("byte") || p.equals(Byte.class.getName())){
			str = "byte "+funcParameter[index]+" = System.cur;\r\n";
		}else if( p.equals("long") || p.equals(Long.class.getName())){
			str = "long "+funcParameter[index]+" = System.currentTimeMillis();\r\n";
		}else if( p.equals("double") || p.equals(Double.class.getName())){
			str = "double "+funcParameter[index]+" = System.currentTimeMillis() + 1.0;\r\n";
		}else if( p.equals("float") || p.equals(Float.class.getName())){
			str = "float "+funcParameter[index]+" = Float.valueOf(1.1);\r\n";
		}else if( p.equals(String.class.getName())){
			str = "String "+funcParameter[index]+" = \"test\";\r\n";
		}else if( p.equals("boolean") || p.equals(Boolean.class.getName())){
			str = "boolean "+funcParameter[index]+" = false;\r\n";
		}else if( p.equals(java.util.Date.class.getName())){
			str = "java.util.Date "+funcParameter[index]+" = new java.util.Date();\r\n";
		}else if( p.contains("[")){
			String pClass = p.replace("[","");
			if( p.equals("int") || p.equals(Integer.class.getName())){
				str = "int "+funcParameter[index]+" = 1;\r\n";
			}else if( p.equals("short") || p.equals(Short.class.getName())){
				str = "short "+funcParameter[index]+" = 1;\r\n";
			}else if( p.equals("byte") || p.equals(Byte.class.getName())){
				str = "byte "+funcParameter[index]+" = System.cur;\r\n";
			}else if( p.equals("long") || p.equals(Long.class.getName())){
				str = "long "+funcParameter[index]+" = System.currentTimeMillis();\r\n";
			}else if( p.equals("double") || p.equals(Double.class.getName())){
				str = "double "+funcParameter[index]+" = System.currentTimeMillis() + 1.0;\r\n";
			}else if( p.equals("float") || p.equals(Float.class.getName())){
				str = "float "+funcParameter[index]+" = Float.valueOf(1.1);\r\n";
			}else if( p.equals(String.class.getName())){
				str = "String "+funcParameter[index]+" = \"test\";\r\n";
			}else{
				str = pClass+"[] "+funcParameter[index]+" = null;\r\n";
			}
		}
//		else if( p.endsWith("DO")||p.endsWith("Query")){//数据对象或者查询对象的生成
//			str = simpleName + " "+funcParameter[index]+" = ("+simpleName+")BaseTest.createObj("+simpleName+".class);\r\n";
//			initImports(p);
//		}
		else if( p.endsWith("List")){
			str = simpleName + " "+funcParameter[index]+" = null;\r\n";
			initImports(p);
		}else{
			str = simpleName + " "+funcParameter[index]+" = ("+simpleName+")BaseTest.createObj("+simpleName+".class);\r\n";
			initImports(p);
		}
		return str;
	}

	/**
	 * 默认的方法处理解析器
	 * @param m
	 * @return 返回指定类型的MethodBean
	 * @return
	 * @throws JavaModelException 
	 */
	@SuppressWarnings("unchecked")
	protected <T> T defaultMethodParser(IMethod m,Class<?> beanClass) throws JavaModelException{
		T t = null;
		try {
			t = (T) beanClass.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
			return t;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return t;
		}
		BaseMethodBean bean = new BaseMethodBean();
		bean.setName(m.getElementName());

		//根据方法参数生成测试方法的内部变量
		String[] paras = m.getParameterTypes();
		StringBuilder before_method = new StringBuilder();
		int i=0;
		for( i=0;i<paras.length;i++){
			String classType = DataUtils.getResolvedType(paras[i], m.getDeclaringType());
			before_method.append(createParamter(classType, i)+"			");
		}
		bean.setBefore_doMethod(before_method.toString());

		//生成方法调用参数的字符串
		String invokedMethodParameterStr = "";
		for(int j=0;j<i;j++){
			invokedMethodParameterStr += ","+funcParameter[j];
		}
		if(invokedMethodParameterStr.length()>0){
			bean.setParameter(invokedMethodParameterStr.substring(1));
		}

		try {
			BeanUtils.copyProperties(t, bean);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		return t;
	}
	
	public static void main(String[] agrs){
		System.out.println(Long.class.getName());
	}
}
