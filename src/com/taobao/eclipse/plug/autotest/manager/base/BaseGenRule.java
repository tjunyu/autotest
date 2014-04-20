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
 * velocityģ�����ɲ��������ļ�
 *
 * @author junyu.wy
 */
public class BaseGenRule extends ClassGenRule {

	protected String testClassName = null;    // ����������
	protected String clazz      = null; 		// ԭ���� itype
	protected String p_clazz    = null; 		// ������
	protected String packageStr = null; 		// ����
	protected List<BaseMethodBean> methodList   = new ArrayList<BaseMethodBean>();     	//method�б�
	protected Map<String,String> testMethodMap = new HashMap<String,String>();
	protected Set<String> importTypes = new HashSet<String>();
	protected Map<String,IField> fieldMap = new HashMap<String,IField>(); 

	@Override
	public Map<String,Object> gen(IType daoClass) throws GenException, JavaModelException {

		// ������������
		parseClassForBaseInfo(daoClass);

		// ���������� ��������
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
	 * ��ȡ�����ı��������������
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
				p_clazz = interfaces[i];       //�ӿ�����Ϊ������
				p_clazz = p_clazz.substring(0,1).toLowerCase() //����ĸСд
				         +p_clazz.substring(1);
			}
		}
		testClassName = simpleClassName+"Test";

		//��getter�����˲���
		IField[] fields = daoClass.getFields();
		for(int i=0;i<fields.length;i++){
			fieldMap.put(fields[i].getElementName().toUpperCase(), fields[i]);
		}
	}

	/**
	 * ��ȡ���ɲ��Է�������ı��������˲���Ҫ���Եķ�������Ҫ���Եķ����Ľ�����methodParseDispatch���
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

			//ֻ���Ա���pulic�ķ���
			if(Flags.isPublic(methods[i].getFlags()) || daoClass.isInterface()){

				//������setter,getter
				tmpName = methods[i].getElementName();
				if((!tmpName.startsWith("get")
						|| !fieldMap.containsKey(tmpName.substring(3).toUpperCase()))
						&& !tmpName.startsWith("set")){
					bean = methodParseDispatch(methods[i]);
					if(bean!=null){

						//����overload��Ҫ������������û��Ҫ��put
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
	 * �ַ���������,parseClassMethodΪģ�壬������Զ�disptch�в�ͬ��ʵ��
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
