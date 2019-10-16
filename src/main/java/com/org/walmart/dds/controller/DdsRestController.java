package com.org.walmart.dds.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.org.walmart.dds.endpoint.DdsEndPoint;
import com.org.walmart.dds.exception.DdsAppException;
import com.org.walmart.dds.model.OrderDetail;
import com.org.walmart.dds.service.DdsService;

@Controller
public class DdsRestController {

	static final Logger _logger = LoggerFactory.getLogger(DdsRestController.class);

	@RequestMapping(method = RequestMethod.GET, value = "/order/getorders")
	@ResponseBody
	/**
	 * This method is to get all the Active Orders which are not delivered
	 * 
	 * @return Map<String, OrderDetail> (String -> OrderId)
	 */
	public Map<String, OrderDetail> getAllOrders() {
		_logger.info("Request to get current order details");
		return DdsEndPoint.getInstance().getOrderDetails();
	}

	/**
	 * This method is to get the Order for given OrderId.
	 * 
	 * @param orderId
	 * @return OrderDetail object
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/order/getorder")
	@ResponseBody
	public OrderDetail getOrder(@RequestBody String orderId) {

		_logger.info("Request to get order details for given OrderId");
		return DdsEndPoint.getInstance().getOrderDetail(orderId);
	}

	/**
	 * This method is to add given OrderDetails. If already exists then, it will
	 * give corresponding response.
	 * 
	 * @param newOrder (OrderDetail)
	 * @return Static Text Success/Failed/AlreadyExist
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/order/addorder")
	@ResponseBody
	public String addOrder(@RequestBody OrderDetail newOrder) {

		_logger.info("Request to Add new Order");
		return DdsEndPoint.getInstance().addOrderDetail(newOrder);
	}

	/**
	 * This method is to delete a specific order or delivered order from the Active
	 * List.
	 * 
	 * @param deleteOrder
	 * @return Success/DoesnotExist/Fail
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/order/deleteorder")
	@ResponseBody
	public String deleteOrder(@RequestBody OrderDetail deleteOrder) {

		_logger.info("Request to Delete Existing Order");
		return DdsEndPoint.getInstance().deleteOrderDetail(deleteOrder);

	}

	/**
	 * This method is to update given OrderDetails. If already exists then, it will
	 * give corresponding response.
	 * 
	 * @param order (OrderDetail)
	 * @return Static Text Success/Failed/AlreadyExist
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/order/updateorder")
	@ResponseBody
	public String updateOrder(@RequestBody OrderDetail order) {

		_logger.info("Request to Update existing Order");
		return DdsEndPoint.getInstance().addOrderDetail(order);
	}
	
	/**
	 * This method is to read the uploaded/requested file and give the response
	 * 
	 * @param file
	 * @param redirectAttributes
	 * @return List<OrderDetail>
	 * @throws DdsAppException
	 * @throws IOException
	 */
	/*@RequestMapping(method = RequestMethod.POST, value = "/order/processfile")
	public List<OrderDetail> fileUpload(@RequestParam MultipartFile file, RedirectAttributes redirectAttributes)
			throws DdsAppException, IOException {

		_logger.info("Received file : " + file);
		if (file.isEmpty()) {
			redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
			return null;
		}

		List<String> inputList = new ArrayList<String>();
		List<OrderDetail> orderList = new ArrayList<OrderDetail>();
		String strLine;
		InputStream inputStream = file.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		while ((strLine = reader.readLine()) != null) {
			inputList.add(strLine);
		}

		DdsService.createOrderDetails(inputList);
		orderList = DdsService.sortOrderByDeliveryDistance(orderList);
		orderList = DdsService.createDeliverySchedule(orderList);

		redirectAttributes.addFlashAttribute("message",
				"You successfully uploaded '" + file.getOriginalFilename() + "'");

		return orderList;
	}*/
	
	/**
	 * This method is to read the uploaded/requested file and give the response
	 * 
	 * @param file
	 * @param redirectAttributes
	 * @return List<OrderDetail>
	 * @throws DdsAppException
	 * @throws IOException
	 */
	/*@RequestMapping(method = RequestMethod.POST, value = "/order/readfile")
	public List<OrderDetail> readFromFile(@RequestParam MultipartFile file)	throws DdsAppException, IOException {

		_logger.info("Received file : " + file);
		
		List<String> inputList = new ArrayList<String>();
		List<OrderDetail> orderList = new ArrayList<OrderDetail>();
		String strLine;
		InputStream inputStream = file.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		while ((strLine = reader.readLine()) != null) {
			inputList.add(strLine);
		}

		DdsService.createOrderDetails(inputList);
		
		return orderList;
	}*/

}
