package com.org.walmart.dds.endpoint;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.org.walmart.dds.config.DdsAppConfig;
import com.org.walmart.dds.model.OrderDetail;

public class DdsEndPoint {

	private Map<String, OrderDetail> orderDetailsMap;
	private Map<String, OrderDetail> deliveredDetailsMap;
	
	static final Logger _logger = LoggerFactory.getLogger(DdsEndPoint.class);

	// Singleton
	private static DdsEndPoint ddsEndPoint = null;

	/** Constructor **/
	private DdsEndPoint() {
		orderDetailsMap = new HashMap<String, OrderDetail>();
		deliveredDetailsMap = new HashMap<String, OrderDetail>();
	}

	/** 
	 * Singleton Pattern invocation to handle the state of the OrderDetails.
	 * This will actually help to have one common Map Object of OrderDetails.
	 * This is helpful when we convert this java application into Rest Service.
	 **/
	public static DdsEndPoint getInstance() {
		if (ddsEndPoint == null) {
			_logger.info("New DDS Endpoint Instance Created.");
			ddsEndPoint = new DdsEndPoint();
			return ddsEndPoint;
		} else {
			return ddsEndPoint;
		}
	}

	/**
	 * This method is to get the Order Details for given OrderId from the common Map of OrderDetails
	 * 
	 * @param orderId
	 * @return OrderDetail
	 */
	public OrderDetail getOrderDetail(String orderId) {
		_logger.info("Getting details for the Order ID : " + orderId);
		return orderDetailsMap.get(orderId);
	}

	/**
	 * This method is to get the Map of all Order Details
	 * 
	 * @return Map of OrderDetails
	 */
	public Map<String, OrderDetail> getOrderDetails() {
		_logger.info("Getting details of All Orders");
		return orderDetailsMap;
	}

	/**
	 * This method is to add given OrderDetail to the Map, with OrderId as Key
	 * 
	 * @param order
	 * @return String(Success/Fail/Already Exists)
	 */
	public String addOrderDetail(OrderDetail order) {
		if (orderDetailsMap.get(order.getOrderId()) == null) {
			_logger.info("Adding new Order Details : " + order.getOrderId());
			orderDetailsMap.put(order.getOrderId(), order);
		} else {
			_logger.info("Order Already Exist :  " + order.getOrderId());
			return DdsAppConfig.ORDER_ALREADY_EXIST;
		}
		return DdsAppConfig.ADD_ORDER_SUCCESS_MSG;
	}

	/**
	 * This method is to delete given OrderDetail from the Map, with OrderId as Key
	 * 
	 * @param order
	 * @return String(Success/Fail/Already Exists)
	 */
	public String deleteOrderDetail(OrderDetail order) {
		if (orderDetailsMap.get(order.getOrderId()) != null) {
			_logger.info("Delete Existing Order Details : " + order.getOrderId());
			orderDetailsMap.remove(order.getOrderId());
			deliveredDetailsMap.put(order.getOrderId(), order);
		} else {
			_logger.info("Order Doesnot Exist!");
			return DdsAppConfig.ORDER_NOT_EXIST;
		}
		return DdsAppConfig.DEL_ORDER_SUCCESS_MSG;

	}
}
