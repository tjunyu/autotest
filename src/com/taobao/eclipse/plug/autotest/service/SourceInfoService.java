package com.taobao.eclipse.plug.autotest.service;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.IType;

import com.taobao.eclipse.plug.autotest.config.Configure;

/**
 * @author junyu.wy
 * 
 */
public class SourceInfoService {

	private Configure conf = null;

	public SourceInfoService(Configure conf) {
		this.conf = conf;
	}

	public List<IType> findAllSouceClasses() {
		List<IType> result = new ArrayList<IType>();
		String[] files = conf.getPackageDir().list();
		int len = files.length;
		String clazz = null;
		for (int i = 0; i < len; i++) {
			clazz = files[i];
			if (clazz.endsWith(".java")) {
				try {
					result.add(conf.getType(conf.getDaoPackge() + "." + clazz.substring(0, clazz.indexOf("."))));
				} catch (Exception e) {
					System.err.println(clazz + " occurs class not found exception");
				}
			}
		}
		return result;
	}

	public IType findSingleSouceClasses() {
		IType result = null;
		String clazz = conf.getJavaFile();
		if (clazz.endsWith(".java")) {
			try {
				result = conf.getType(clazz.substring(0, clazz.lastIndexOf(".")));
			} catch (Exception e) {
				System.err.println(clazz + " occurs class not found exception");
			}
		}
		return result;
	}
}
