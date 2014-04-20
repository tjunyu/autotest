package com.taobao.eclipse.plug.autotest.exception;
/**
 * @author junyu.wy
 * 
 */
public class GenException extends Exception {

	public GenException(Exception e) {
		super(e);
	}

	public GenException(String string) {
		super(string);
	}

	private static final long serialVersionUID = -5941572854313236365L;

}
