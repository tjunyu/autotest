package com.taobao.eclipse.plug.autotest.exception;
/**
 * @author junyu.wy
 * 
 */
public class ConfigException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = 2901070219445902022L;

	public ConfigException(String string) {
		super(string);
	}

	public ConfigException(Exception e) {
		super(e);
	}

}
