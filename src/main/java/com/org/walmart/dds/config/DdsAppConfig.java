package com.org.walmart.dds.config;


public class DdsAppConfig {

	/** Application Related Config **/
	public static final String DAILY_DELIVERY_START_TIME = "06:00:00";
    public static final String DAILY_DELIVERY_END_TIME = "22:00:00";
    public static final String INPUT_TXT_FILE_PATH = "D:/Walmart-DDS/data/inputData.txt";
    public static final String DELIVERED_TXT_FILE_PATH = "D:/Walmart-DDS/data/deliveredData.txt";
    public static final String UNDELIVERED_TXT_FILE_PATH = "D:/Walmart-DDS/data/undeliveredData.txt";
    public static final String ADD_ORDER_SUCCESS_MSG = "Order Successful";
    public static final String DEL_ORDER_SUCCESS_MSG = "Delete Order Successful";
    public static final String ORDER_ALREADY_EXIST = "Order Already Exists";
    public static final String ORDER_NOT_EXIST = "Order Doesn't Exists";
    public static final String ORDER_NOT_DELIVERED = "Order Not Delivered";
    public static final String ORDER_SETUP_TIME = "2"; //Minutes to setup the next order to the Drone 
    //TODO: Enhance this service to handle Json Type (Rest API)
    //public static final String INPUT_JSON_FILE_PATH = "D:/Walmart-DDS/data/inputData.json";
    //public static final String OUTPUT_JSON_FILE_PATH = "D:/Walmart-DDS/data/outputData.json";
    
    //TODO: Need to move to test related CONFIG file
    /** Test Related Config **/
    public static final String INPUT_VALID_TXT_FILE_PATH = "D:/Walmart-DDS/data/test/junitInputData.txt";
	public static final String INPUT_INVALID_TXT_FILE_PATH = "D:/Walmart-DDS/data/test/junitInputData1.txt";
	public static final String INPUT_EMPTY_FILE_PATH = "";
	public static final String INPUT_WRONG_FILE_TYPE = "D:/Walmart-DDS/data/test/junitInputData.doc";
	public static final String TEST_DELIVERED_TXT_FILE_PATH = "D:/Walmart-DDS/data/test/junitTestDeliveredData.txt";
	public static final String TEST_UNDELIVERED_TXT_FILE_PATH = "D:/Walmart-DDS/data/test/junitTestUndeliveredData.txt";
	public static final String TEST_INPUT_FILE_PATH = "D:/Walmart-DDS/data/test/junitInputTest.txt";
	
}
