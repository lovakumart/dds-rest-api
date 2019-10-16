package com.org.walmart.dds.util;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.org.walmart.dds.config.DdsAppConfig;
import com.org.walmart.dds.exception.DdsAppException;
import com.org.walmart.dds.exception.DdsErrorType;
import com.org.walmart.dds.model.OrderDetail;
import com.sun.xml.messaging.saaj.packaging.mime.internet.ParseException;

public class DdsUtil {

	static final Logger _logger = LoggerFactory.getLogger(DdsUtil.class);
	
	/**
	 * This method is to create a FilePath by concat fileDir and fileName
	 * 
	 * @param filePath
	 * @param fileName
	 * @return String (FilePath)
	 */
	public static String createFilePath(String fileDir, String fileName) {
		if(fileDir != null && fileName != null) {
			return fileDir+"\\"+fileName;
		}
		return null;
	}

	/**
	 * This method is to valdate the file exists and also the type to be .txt or
	 * .json
	 * 
	 * @param fileName
	 * @return true/false
	 */
	public static boolean isInputFileValid(String fileName) {
		fileName = fileName.trim();
		if (fileName != null && (fileName.contains(".txt") || fileName.contains(".json"))) {
			return new File(fileName).exists();
		}
		return false;
	}

	/**
	 * This method is to validate the String given in each line of file. This will
	 * check for the values in each line i.e.; WM001 N11W5 05:11:50
	 * 
	 * @param inputStr
	 * @return true/false
	 */
	public static boolean isValidInput(String inputStr) {
		String inputData[] = DdsUtil.getStringArray(inputStr);

		if (inputData.length < 3 || inputData.length > 3) {
			return false;
		}
		return true;
	}

	/**
	 * This method is to split any given string at " " and return array of strings
	 * 
	 * @param inputStr
	 * @return String[]
	 */
	public static String[] getStringArray(String inputStr) {
		return inputStr.split(" ");
	}

	/**
	 * This method is to calculate the Destination Distance. As per the requirement
	 * each block takes one minute. Based on that, the assumption is each number
	 * (either north or south etc) is calculated as block per min. e.g.; N11W5
	 * meaning North 11 blocks and W 5 blocks.
	 * 
	 * Based on above assumption, Total Distance should be sum of each Way
	 * Calculating the distance based on formula (North blocks * 2) + (West block *
	 * 2) and SquareRoot of the result.
	 * 
	 * @param newOrder
	 */
	public static String calcDestDistance(OrderDetail newOrder) {

		String coordsStr = newOrder.getGridCoordinate().replaceAll("(N|W|S|E)", " ").trim();
		String coordVals[] = coordsStr.split(" ");

		Double firstCoord = Double.parseDouble(coordVals[0]);
		Double secondCoord = Double.parseDouble(coordVals[1]);

		Double destDistance = Math.sqrt((firstCoord * firstCoord) + (secondCoord * secondCoord));

		_logger.info("Distance from Warehouse to " + newOrder.getGridCoordinate() + " is " + destDistance);
		newOrder.setDeliveryDistance(String.valueOf(destDistance));

		return newOrder.getDeliveryDistance();
	}

	/**
	 * This method is to calculate the delivery start time for each order.
	 * Assumption: Based on the requirement, delivery start time is 6:00AM and end
	 * time is 10:00PM
	 * If the Delivery Time is beyond 10:00:00PM, the order will not be processed and will be added to Undelivered List
	 * 
	 * @param inputOrderList
	 * @throws DdsAppException
	 */
	public static String calcDeliveryStartTime(OrderDetail order, String scheduledStartTime) throws DdsAppException {

		Double distance = Double.valueOf(order.getDeliveryDistance());
		order.setActDeliveryStartDate(order.getOrderDate());
		order.setActDeliveryStartTime(scheduledStartTime);

		order.setActDeliveryEndDate(order.getOrderDate());
		/** Calculate the Delivery Time using the Scheduled Start Time and Distance **/
		order.setActDeliveryEndTime(calcDeliveryTime(scheduledStartTime, distance));

		order.setFinalRoundTripDate(order.getOrderDate());
		order.setFinalRoundTripTime(calcDeliveryTime(scheduledStartTime, distance * 2));
		order.setDelivered(true);
		order.setComments(DdsAppConfig.ADD_ORDER_SUCCESS_MSG);

		order.setDeliveryTimeTaken(
				String.valueOf(calcDeliveryTimeTaken(order.getOrderTime(), order.getFinalRoundTripTime())));

		_logger.info("Delivery Start Time For Order : "+order.getOrderId()+" is " + scheduledStartTime);
		_logger.info("Delivery End Time For Order : "+order.getOrderId()+" is " + order.getActDeliveryEndTime());
		_logger.info("Drone Warehouse Returned After Order : "+order.getOrderId()+" is " + order.getFinalRoundTripTime());
		
		/**
		 * Seting the Scheduled Start Time of next Delivery using the previous order
		 * Delivery End Time
		 **/
		scheduledStartTime = order.getFinalRoundTripTime();

		return scheduledStartTime;
	}

	/**
	 * Calculate the NPS value based on the delivery details of the complete list of
	 * Orders
	 * 
	 * Assumption: Total Time Taken 
	 * < 60 mins ==> Promo 10 
	 * >= 60 mins and < 120 mins  ==> Promo 9 
	 * >= 120 mins and < 180 mins ==> Promo 8 
	 * >= 180 mins and < 240 mins ==> Promo 7 
	 * >= 240 mins and < 300 mins ==> Detract 6 
	 * >= 300 mins and < 360 mins ==> Detract 5 
	 * >= 360 mins and < 420 mins ==> Detract 4 
	 * >= 420 mins and < 480 mins ==> Detract 3 
	 * >= 480 mins and < 540 mins ==> Detract 2 
	 * >= 540 mins and < 600 mins ==> Detract 1 
	 * >= 600 mins ==> Detract 0 (Not considered)
	 * 
	 * @param orderDetailList
	 * @return NPS Value (Integer)
	 */
	public static Integer calcNPSValue(List<OrderDetail> orderDetailList) {

		int netPromoterAgg = 0;
		int netDetractorAgg = 0;
		int promotersCnt = 0;
		int detractorsCnt = 0;

		float promoterNPSPercent = 0;
		float detractorNPSPercent = 0;
		int finalNPSValue = 0;

		for (OrderDetail order : orderDetailList) {
			if(order.isDelivered()) {
				Integer totalTimeTaken = Integer.valueOf(order.getDeliveryTimeTaken());
	
				if (totalTimeTaken < 60) {
					netPromoterAgg = netPromoterAgg + 10;
					promotersCnt++;
				} else if (totalTimeTaken >= 60 && totalTimeTaken < 120) {
					netPromoterAgg = netPromoterAgg + 9;
					promotersCnt++;
				} else if (totalTimeTaken >= 120 && totalTimeTaken < 180) {
					netPromoterAgg = netPromoterAgg + 8;
					promotersCnt++;
				} else if (totalTimeTaken >= 180 && totalTimeTaken < 240) {
					netPromoterAgg = netPromoterAgg + 7;
					promotersCnt++;
				} else if (totalTimeTaken >= 240 && totalTimeTaken < 300) {
					netDetractorAgg = netDetractorAgg + 6;
					detractorsCnt++;
				} else if (totalTimeTaken >= 300 && totalTimeTaken < 360) {
					netDetractorAgg = netDetractorAgg + 5;
					detractorsCnt++;
				} else if (totalTimeTaken >= 360 && totalTimeTaken < 420) {
					netDetractorAgg = netDetractorAgg + 4;
					detractorsCnt++;
				} else if (totalTimeTaken >= 420 && totalTimeTaken < 480) {
					netDetractorAgg = netDetractorAgg + 3;
					detractorsCnt++;
				} else if (totalTimeTaken >= 480 && totalTimeTaken < 540) {
					netDetractorAgg = netDetractorAgg + 2;
					detractorsCnt++;
				} else if (totalTimeTaken >= 540 && totalTimeTaken < 600) {
					netDetractorAgg = netDetractorAgg + 1;
					detractorsCnt++;
				}
			}
		}

		if (netPromoterAgg != 0)
			promoterNPSPercent = (float) netPromoterAgg / promotersCnt;
		if (netDetractorAgg != 0)
			detractorNPSPercent = (float) netDetractorAgg / detractorsCnt;

		finalNPSValue = (int) ((float) (promoterNPSPercent - detractorNPSPercent) * 10);

		return finalNPSValue;
	}

	/**
	 * This method is to calcualte the Delivery Time Taken between two given time
	 * 
	 * @param orderTime
	 * @param deliveredTime
	 * @return Long (Time Taken)
	 * @throws DdsAppException
	 */
	public static Long calcDeliveryTimeTaken(String orderTime, String deliveredTime) throws DdsAppException {
		Long deliveryTimeTaken = 0L;
		try {
			SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
			deliveryTimeTaken = format.parse(deliveredTime).getTime() - format.parse(orderTime).getTime();
		} catch (Exception ex) {
			if(ex instanceof NumberFormatException || ex instanceof ParseException) {
				throw new DdsAppException(DdsErrorType.DDS_006, ex.getLocalizedMessage());
			}
		}

		return ((deliveryTimeTaken / 1000) / 60);
	}

	/**
	 * This method is to calculate the Delivery Start Time for each Order based on
	 * the distance from Warehouse
	 * 
	 * @param ordertime
	 * @param distance
	 * @return Delivery Time (String)
	 */
	public static String calcDeliveryTime(String ordertime, Double distance) {
		/** Converting the Distance into String and Splitting the Value and Decimal **/
		String distanceVal = new DecimalFormat("#.00").format(distance);
		String[] splitDistanceVal = distanceVal.split("\\.");
		int actualVal = Integer.parseInt((splitDistanceVal[0]));
		float decimalVal = Float.parseFloat((splitDistanceVal[1]));
		int roundedVal = (int) Math.ceil((decimalVal * 60) / 100);

		/** Extracting the Hour/Minute/Seconds from the OrderTime **/
		String[] orderTimeArr = ordertime.split("\\:");
		int orderTimeHr = Integer.parseInt((orderTimeArr[0]));
		int orderTimeMin = Integer.parseInt((orderTimeArr[1]));
		int orderTimeSec = Integer.parseInt((orderTimeArr[2]));

		/**
		 * Logic to add the rounded value to the seconds and calculate the delivery
		 * Hour/Minute/Seconds
		 **/
		/** Calculate the Delivery Time based on the Seconds condition) **/
		if ((roundedVal + orderTimeSec) >= 60) {
			int tempVal = (int) ((decimalVal + orderTimeSec) / 60);
			orderTimeMin = orderTimeMin + tempVal;
			orderTimeSec = orderTimeSec + (((roundedVal + orderTimeSec) / 60 - tempVal) * 60);
		} else {
			orderTimeSec = orderTimeSec + roundedVal;
		}

		/** Calculate the Delivery Time based on the Minutes/Hours condition) **/
		if ((actualVal + orderTimeMin) >= 60) {
			int tempVal = (int) ((actualVal + orderTimeMin) / 60);
			orderTimeHr = orderTimeHr + tempVal;
			orderTimeMin = orderTimeMin + (((actualVal + orderTimeMin) / 60 - tempVal) * 60);
		} else {
			orderTimeMin = orderTimeMin + actualVal;
		}

		/** Return the Time in fomat HH:mm:ss **/
		return String.format("%02d", orderTimeHr) + ":" + String.format("%02d", orderTimeMin) + ":"
				+ String.format("%02d", orderTimeSec);
	}

}
