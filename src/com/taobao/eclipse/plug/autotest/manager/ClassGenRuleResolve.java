package com.taobao.eclipse.plug.autotest.manager;

import java.util.Map;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

import com.taobao.eclipse.plug.autotest.exception.GenException;
import com.taobao.eclipse.plug.autotest.manager.base.BaseGenRule;
import com.taobao.eclipse.plug.autotest.manager.manager.ManagerGenRule;
/**
 * @author junyu.wy
 * 
 */
public class ClassGenRuleResolve {
	private BaseGenRule strategy;

	public ClassGenRuleResolve(BaseGenRule strategy){
		this.strategy=strategy;
	}

	public ClassGenRuleResolve(int type){
		//将来有新的特殊化类型的用type来区分
			strategy=new ManagerGenRule();
	}

	public Map<String,Object> getGenContent(IType clazz,String tesPackage) throws GenException, JavaModelException{
		strategy.setPackageStr(tesPackage);
		return strategy.gen(clazz);
	}

	public void changeStrategy(BaseGenRule strategy) {
		this.strategy = strategy;
	}

}
