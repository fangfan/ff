package com.adchina.dbutil.exception;

/**
 * 基础异常定义(内部组件异常).
 * @author F.Fang
 *
 */
public class NestedComponentException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NestedComponentException(String msg) {
		super(msg);
	}

	public NestedComponentException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
