package com.org.walmart.dds.exception;

public class DdsAppException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	private final DdsErrorType _ddsErrorEnum;
	
	/**
	 * 
	 * @param ddsErrTypeEnum
	 * @param message
	 * @param oops
	 */
	public DdsAppException(final DdsErrorType ddsErrTypeEnum, final String message, final Throwable oops) {
		super(message, oops);
		_ddsErrorEnum = ddsErrTypeEnum;
	}
	
	/**
	 * 
	 * @param ddsErrTypeEnum
	 * @param message
	 */
	public DdsAppException(final DdsErrorType ddsErrTypeEnum, final String message) {
		super(message);
		_ddsErrorEnum = ddsErrTypeEnum;
	}
	
	/**
	 * 
	 * @param ddsErrTypeEnum
	 * @param oops
	 */
	public DdsAppException(final DdsErrorType ddsErrTypeEnum, final Throwable oops) {
		super(oops);
		_ddsErrorEnum = ddsErrTypeEnum;
	}
	
	/**
	 * 
	 * @return DdsErrorType
	 */
	public DdsErrorType getDdsErrorTypeEnum() {
		return _ddsErrorEnum;
	}
}
