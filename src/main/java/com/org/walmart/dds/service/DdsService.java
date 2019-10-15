package com.org.walmart.dds.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.org.walmart.dds.config.DdsAppConfig;
import com.org.walmart.dds.exception.DdsAppException;
import com.org.walmart.dds.exception.DdsErrorType;
import com.org.walmart.dds.factory.DdsDataTransformFactory;
import com.org.walmart.dds.model.OrderDetail;
import com.org.walmart.dds.util.DdsUtil;

public class DdsService {

	static final Logger _logger = LoggerFactory.getLogger(DdsService.class);

	/**
	 * This method is to extract and create list of String (input) reading from
	 * File.
	 * 
	 * @param fileName
	 * @return List<String>
	 * @throws DdsAppException
	 */
	public static List<String> createInputDataFromFile(String fileName) throws DdsAppException {
		_logger.info("Reading the input data from the File : " + fileName);
		BufferedReader lineReader = null;
		List<String> inputDataList = null;
		try {
			/** Read file line by line and add the value to the List of String **/
			lineReader = new BufferedReader(new FileReader(fileName));
			inputDataList = new ArrayList<String>();
			String inputStr;

			while ((inputStr = lineReader.readLine()) != null) {
				inputDataList.add(inputStr);
			}

			lineReader.close();
		} catch (Exception ex) {
			if(ex instanceof IOException || ex instanceof FileNotFoundException) {
				throw new DdsAppException(DdsErrorType.DDS_007, "Caught Exception : " + DdsErrorType.DDS_007.getErrorTxt());
			}
		}

		return inputDataList;
	}

	/**
	 * This method is to create list of OrderDetails using the List of input data
	 * read from file.
	 * Generating Random DroneID with Alpha Numeric. 
	 * Assumption: Each file or Set of Orders will be assigned to one Drone
	 * 
	 * @param inputDataList
	 * @return List<OrderDetail>
	 * @throws DdsAppException
	 */
	public static List<OrderDetail> createOrderDetails(List<String> inputDataList) throws DdsAppException {
		
		final String droneId = "DRN#"+RandomStringUtils.random(6, false, true);
		_logger.info("Creating order details and assigning to Drone : "+droneId);
		
		List<OrderDetail> orderDetailList = new ArrayList<OrderDetail>();
		try {
			for (String inputStr : inputDataList) {
				/** Check if the input data is valid **/
				if (DdsUtil.isValidInput(inputStr)) {
					/** Create the OrderDetail object from individual input data string **/
					OrderDetail newOrder = DdsDataTransformFactory.createOrderDetailFromString(inputStr);
					newOrder.setDroneId(droneId);

					/** Calculate the Distance from Warehouse to Delivery Address **/
					DdsUtil.calcDestDistance(newOrder);

					/** TODO: Required for Rest API invocation **/
					/** Add the order to the Map **/
					// DdsEndPoint.getInstance().addOrderDetail(newOrder);

					/** Add the order to the List **/
					orderDetailList.add(newOrder);
				} else {
					throw new DdsAppException(DdsErrorType.DDS_002,
							"Caught Exception : " + DdsErrorType.DDS_002.getErrorTxt());
				}
			}

		} catch (Exception ex) {
			_logger.debug(ExceptionUtils.getStackTrace(ex));
			throw new DdsAppException(DdsErrorType.DDS_002, "Caught Exception : " + DdsErrorType.DDS_002.getErrorTxt());

		}

		return orderDetailList;
	}

	/**
	 * This method is the sort the list of orders based on the delivery distance.
	 * The logic below is using the Collections sort, in which the Compare method is
	 * overridden to compare the orderdetail delivery distance and arrange them in
	 * ascending order.
	 * 
	 * @param inputOrderList
	 * @return List<OrderDetail> (Sorted in Ascending Order)
	 * @throws DdsAppException
	 */
	public static List<OrderDetail> sortOrderByDeliveryDistance(List<OrderDetail> inputOrderList) throws DdsAppException {

		_logger.info("Sorting the Order based on the Distance from Warehouse");
		Collections.sort(inputOrderList, new Comparator<OrderDetail>() {
			@Override
			public int compare(OrderDetail order1, OrderDetail order2) {
				return Double.compare(Double.valueOf(order1.getDeliveryDistance()),
						Double.valueOf(order2.getDeliveryDistance()));
			}
		});

		return inputOrderList;
	}

	
	/**
	 * This method is to calculate the Start Time for each order in the sorted list
	 * 
	 * @param inputOrderList
	 * @return List<OrderDetail> (Sorted in Ascending Order)
	 * @throws DdsAppException
	 */
	public static List<OrderDetail> createDeliverySchedule(List<OrderDetail> inputOrderList) throws DdsAppException {

		/**
		 * Calculate the Delivery Start Time for each sorted Order based on the previous
		 * Delivery
		 **/
		String scheduledStartTime = DdsAppConfig.DAILY_DELIVERY_START_TIME;
		for (OrderDetail orderDetail : inputOrderList) {

			scheduledStartTime = DdsUtil.calcDeliveryStartTime(orderDetail, scheduledStartTime);
		}
		return inputOrderList;
	}

	/**
	 * This method is to loop through the list of OrderDetails and write to given
	 * Output File
	 * 
	 * @param fileName
	 * @param orderDetailList
	 * @param npsValue
	 * 
	 * @throws DdsAppException
	 */
	public static void writeOutputToFile(String deliveredFile, String undeliveredFile, List<OrderDetail> orderDetailList, Integer npsValue)
			throws DdsAppException {

		BufferedWriter deliveredLineWriter = null;
		BufferedWriter undeliveredLineWriter = null;
		try {
			deliveredLineWriter = new BufferedWriter(new FileWriter(deliveredFile));
			undeliveredLineWriter = new BufferedWriter(new FileWriter(undeliveredFile));
		} catch (IOException ex) {
			_logger.debug(ExceptionUtils.getStackTrace(ex));
			throw new DdsAppException(DdsErrorType.DDS_001, DdsErrorType.DDS_001.getErrorTxt());
		}

		_logger.info("Writing the output of the schedule to File : " + deliveredFile);
		/** Reading each line and write to File **/
		try {
			for (OrderDetail order : orderDetailList) {
				if(order.isDelivered()) {
					deliveredLineWriter.write(order.getOrderId() + " " + order.getActDeliveryStartTime());
					deliveredLineWriter.newLine();
				} else {
					/** Undelivered Data should be same as input file i.e.; WM001 N12W5 05:00:00 **/
					undeliveredLineWriter.write(order.getOrderId() + " " + order.getGridCoordinate() + " " + order.getOrderTime());
					undeliveredLineWriter.newLine();
				}
			}

			/** Add NPS value in the end of the file (as per the required output) **/
			deliveredLineWriter.write("NPS " + npsValue);

			deliveredLineWriter.close();
			undeliveredLineWriter.close();
		} catch (IOException ex) {
			_logger.debug(ExceptionUtils.getStackTrace(ex));
			throw new DdsAppException(DdsErrorType.DDS_003, DdsErrorType.DDS_003.getErrorTxt());
		}
	}
}
