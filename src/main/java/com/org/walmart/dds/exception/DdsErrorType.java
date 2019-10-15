package com.org.walmart.dds.exception;

public enum DdsErrorType implements ErrorType {

	/**
	 * Custom error handling/error messages for multiple scenarios
	 */
	DDS_001("FILE_READ_ERROR", "Invalid File Error#"),
	DDS_002("INVALID_INPUT", "Invalid Input Data Error#"),
	DDS_003("FILE_WRITE_ERROR", "File Save/Write Error#"),
	DDS_004("INVALID_DATA", "Invalid Input Data From File#"),
	DDS_005("NON_DELIVERY_WINDOW", "Delivery Time Is Out Of Normal Business Hours#"),
	DDS_006("FORMAT_ERROR", "Formatting Error While Parsing"),
	DDS_007("SYSTEM_ERROR", "Unknown System Error");
	

	DdsErrorType(String errorId, String errorTxt) {
		this._errorId = errorId;
		this._errorTxt = errorTxt;
	}

	private String _errorId;
	private String _errorTxt;

	public String getName() {
		return this.name();
	}

	public String getErrorId() {
		return _errorId;
	}

	public String getErrorTxt() {
		return _errorTxt;
	}

	public String toString(String[] errTxtArr) {
		String finalErrorTxt = "";
		for (String str : errTxtArr) {
			finalErrorTxt = " # " + finalErrorTxt + " # " + str;
		}
		return finalErrorTxt;
	}

}
