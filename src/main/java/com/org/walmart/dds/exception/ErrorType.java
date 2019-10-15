package com.org.walmart.dds.exception;

public interface ErrorType {

	public String getName();

	public String getErrorId();

	public String getErrorTxt();

	public String toString(String[] errTxtArr);
}
