package com.org.walmart.dds;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.org.walmart.dds.config.DdsAppConfig;
import com.org.walmart.dds.exception.DdsAppException;
import com.org.walmart.dds.model.OrderDetail;
import com.org.walmart.dds.service.DdsService;
import com.org.walmart.dds.util.DdsUtil;

@SpringBootApplication
public class DdsRestApiApplication {

	static final Logger _logger = LoggerFactory.getLogger(DdsRestApiApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(DdsRestApiApplication.class, args);
		DdsRestApiApplication.executeProcess(args);
	}

	public static void executeProcess(String[] args) {
		_logger.info("Number of arguments passed : " + args.length);
		for (String argument : args) {
			_logger.info("Argument : " + argument);
		}

		String inputFileName = null;
		String outputFileName = null;
		List<OrderDetail> orderDetailList = null;
		List<String> inputDataList = null;

		/**
		 * Assumption: If the File Path is not sent as argument, it will pick the
		 * default path from Config "D:/Walmart-DDS/data/inputData.txt"
		 * 
		 * Sample Input: WM001 N11W5 05:11:50 WM002 S3E2 05:11:55
		 */
		if (args.length != 0) {
			inputFileName = args[0];
		} else {
			_logger.info("The input file path is not provided. Getting default from CONFIG file");
			inputFileName = DdsAppConfig.INPUT_TXT_FILE_PATH;
		}

		try {
			if (DdsUtil.isInputFileValid(inputFileName)) {

				/** Creating List of Input Data reading from the input File **/
				inputDataList = DdsService.createInputDataFromFile(inputFileName);

				/** Creating Order Details List from the Input Data **/
				orderDetailList = DdsService.createOrderDetails(inputDataList);

				/** Sorting the orders based on the Delivery Distance **/
				orderDetailList = DdsService.sortOrderByDeliveryDistance(orderDetailList);

				/** Creating Delivery Schedule Based On below assumptions */
				orderDetailList = DdsService.createDeliverySchedule(orderDetailList);

				/** Calculate the Net Promoter Score Percentile */
				Integer npsValue = DdsUtil.calcNPSValue(orderDetailList);

				/**
				 * Assumption: If the File Path is not sent as argument, it will pick the
				 * default path from Config "G:/Walmart-Drone/data/outputData.txt"
				 */
				if (args.length > 1) {
					outputFileName = args[1];
				} else {
					_logger.info("The output file path is not provided. Getting default from CONFIG file.");
					outputFileName = DdsAppConfig.DELIVERED_TXT_FILE_PATH;
				}

				/**
				 * Write the Output (Order and Departure Time, along with NPS Value to a file)
				 * 
				 * Sample Output: 
				 * WM002 06:00:00 
				 * WM001 06:07:13 
				 * NPS 87
				 */
				DdsService.writeOutputToFile(outputFileName, DdsAppConfig.UNDELIVERED_TXT_FILE_PATH, orderDetailList, npsValue);
				_logger.info("Request completed");
			}
		} catch (DdsAppException ex) {
			_logger.error(ex.getDdsErrorTypeEnum().getName() + " # " + ex.getDdsErrorTypeEnum().getErrorId() + " # "
					+ ex.getDdsErrorTypeEnum().getErrorTxt());
		}

	}

}
