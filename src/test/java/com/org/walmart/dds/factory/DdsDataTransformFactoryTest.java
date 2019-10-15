package com.org.walmart.dds.factory;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.org.walmart.dds.model.OrderDetail;

public class DdsDataTransformFactoryTest {

	private DdsDataTransformFactory ddsDataFactory;
	
	@Before
	public void setUp() throws Exception {
		ddsDataFactory = new DdsDataTransformFactory();
	}

	@Test
	public void testCreateOrderDetailFromString() {
		OrderDetail orderDetail = ddsDataFactory.createOrderDetailFromString("WM001 N11W5 05:11:50");
		assertEquals("WM001", orderDetail.getOrderId());
		assertEquals("N11W5", orderDetail.getGridCoordinate());
		assertEquals("05:11:50", orderDetail.getOrderTime());
	}

}
