package com.org.walmart.dds.endpoint;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.org.walmart.dds.config.DdsAppConfig;
import com.org.walmart.dds.model.OrderDetail;

public class DdsEndPointTest {

	private DdsEndPoint ddsEndPoint;
	private OrderDetail orderDetail;

	@Before
	public void setUp() throws Exception {
		ddsEndPoint = ddsEndPoint.getInstance();
		orderDetail = new OrderDetail();
	}

	@Test
	public void testGetInstance() {
		assertEquals(ddsEndPoint, ddsEndPoint.getInstance());
	}

	@Test
	public void testGetOrderDetail() {
		assertNull(ddsEndPoint.getOrderDetail("WM001"));
	}

	@Test
	public void testGetOrderDetails() {
		assertNotNull(ddsEndPoint.getOrderDetails());
	}

	@Test
	public void testAddDeleteOrderDetail() {
		orderDetail.setOrderId("WM003");
		assertEquals(DdsAppConfig.ADD_ORDER_SUCCESS_MSG, ddsEndPoint.addOrderDetail(orderDetail));

		orderDetail = new OrderDetail();
		orderDetail.setOrderId("WM002");
		assertEquals(DdsAppConfig.ADD_ORDER_SUCCESS_MSG, ddsEndPoint.addOrderDetail(orderDetail));

		orderDetail = new OrderDetail();
		orderDetail.setOrderId("WM002");
		assertEquals(DdsAppConfig.ORDER_ALREADY_EXIST, ddsEndPoint.addOrderDetail(orderDetail));

		orderDetail = new OrderDetail();
		orderDetail.setOrderId("WM002");
		assertEquals(DdsAppConfig.DEL_ORDER_SUCCESS_MSG, ddsEndPoint.deleteOrderDetail(orderDetail));

		orderDetail = new OrderDetail();
		orderDetail.setOrderId("WM010");
		assertEquals(DdsAppConfig.ORDER_NOT_EXIST, ddsEndPoint.deleteOrderDetail(orderDetail));

	}

}
