package com.org.walmart.dds.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import com.org.walmart.dds.config.DdsAppConfig;
import com.org.walmart.dds.exception.DdsAppException;
import com.org.walmart.dds.model.OrderDetail;

public class DdsServiceTest {

	private DdsService ddsService;
	private OrderDetail orderDetail;
	private List<String> inputList;
	private List<OrderDetail> orderList;

	@Before
	public void setUp() throws Exception {
		ddsService = new DdsService();
		orderDetail = new OrderDetail();
		orderList = new ArrayList<OrderDetail>();
	}

	@Test
	public void testDdsServiceAllMethods() throws DdsAppException {

		inputList = ddsService.createInputDataFromFile(DdsAppConfig.TEST_INPUT_FILE_PATH);
		assertEquals(2, inputList.size());
		assertEquals("WM001 N11W5 05:11:50", inputList.get(0));

		orderList = ddsService.createOrderDetails(inputList);
		assertEquals(2, orderList.size());
		assertEquals("WM001", orderList.get(0).getOrderId());

		orderList = ddsService.sortOrderByDeliveryDistance(orderList);
		assertEquals("WM002", orderList.get(0).getOrderId());

		orderList = ddsService.createDeliverySchedule(orderList);
		assertEquals(2, orderList.size());
		orderDetail = orderList.get(0);
		assertEquals("WM002 06:00:00", (orderDetail.getOrderId() + " " + orderDetail.getActDeliveryStartTime()));
		orderDetail = orderList.get(1);
		assertEquals("WM001 06:03:37", (orderDetail.getOrderId() + " " + orderDetail.getActDeliveryStartTime()));

	}

	@SuppressWarnings("deprecation")
	@Test
	public void testWriteOutputToFile() throws Exception {
		orderDetail.setOrderId("WM001");
		orderList.add(orderDetail);

		ddsService.writeOutputToFile(DdsAppConfig.TEST_DELIVERED_TXT_FILE_PATH,
				DdsAppConfig.TEST_UNDELIVERED_TXT_FILE_PATH, orderList, Integer.valueOf(90));
		final String fileContentStr = FileUtils.readFileToString(new File(DdsAppConfig.TEST_DELIVERED_TXT_FILE_PATH));
		assertTrue(fileContentStr.contains("NPS 90"));
	}

	// Test for delivery time outside the end of business i.e.; after 10:00PM
	@Test
	public void testInvalidDataInFile() {

		try {
			inputList = ddsService.createInputDataFromFile(DdsAppConfig.INPUT_INVALID_TXT_FILE_PATH);
			fail("Expected an DdsAppException to be thrown");
		} catch (DdsAppException ex) {
			assertThat(ex.getDdsErrorTypeEnum().getErrorId(), is("SYSTEM_ERROR"));
		}

	}
}
