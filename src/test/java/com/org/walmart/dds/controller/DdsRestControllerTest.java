package com.org.walmart.dds.controller;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.org.walmart.dds.config.DdsAppConfig;
import com.org.walmart.dds.model.OrderDetail;

public class DdsRestControllerTest {

	private DdsRestController ddsController;
	private OrderDetail orderDetail;
	
	@Before
	public void setUp() throws Exception {
		ddsController = new DdsRestController();
		orderDetail = new OrderDetail();
	}

	@Test
	public void testGetAllOrders() {
		assertNotNull(ddsController.getAllOrders());
	}

	@Test
	public void testGetOrder() {
		orderDetail.setOrderId("WM001");
		ddsController.addOrder(orderDetail);
		assertNotNull(ddsController.getOrder("WM001"));
	}

	@Test
	public void testAddDeleteOrder() {
		
		orderDetail.setOrderId("WM005");
		assertEquals(DdsAppConfig.ADD_ORDER_SUCCESS_MSG, ddsController.addOrder(orderDetail));
		
		orderDetail = new OrderDetail();
		orderDetail.setOrderId("WM006");
		assertEquals(DdsAppConfig.ADD_ORDER_SUCCESS_MSG, ddsController.addOrder(orderDetail));
		
		orderDetail = new OrderDetail();
		orderDetail.setOrderId("WM006");
		assertEquals(DdsAppConfig.ORDER_ALREADY_EXIST, ddsController.addOrder(orderDetail));
		
		orderDetail = new OrderDetail();
		orderDetail.setOrderId("WM005");
		assertEquals(DdsAppConfig.DEL_ORDER_SUCCESS_MSG, ddsController.deleteOrder(orderDetail));
		
		orderDetail = new OrderDetail();
		orderDetail.setOrderId("WM010");
		assertEquals(DdsAppConfig.ORDER_NOT_EXIST, ddsController.deleteOrder(orderDetail));
		
	}

}
