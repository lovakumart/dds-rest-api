package com.org.walmart.dds.util;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import com.org.walmart.dds.config.DdsAppConfig;
import com.org.walmart.dds.exception.DdsAppException;
import com.org.walmart.dds.model.OrderDetail;

public class DdsUtilTest {

	private DdsUtil ddsUtil;
	private OrderDetail orderDetail;

	@Before
	public void setUp() throws Exception {
		ddsUtil = new DdsUtil();
		orderDetail = new OrderDetail();
	}

	@Test
	public void testWithValidInput() {
		assertTrue(ddsUtil.isValidInput("WM001 N11W5 05:11:5"));
	}

	@Test
	public void testWithMoreThanThreeValues() {
		assertFalse(ddsUtil.isValidInput("WM001 N11W5 05:11:5 XYZ"));
	}

	@Test
	public void testWithLessThanThreeValues() {
		assertFalse(ddsUtil.isValidInput("WM001 N11W5"));
	}

	@Test
	public void testWithNoValues() {
		assertFalse(ddsUtil.isValidInput(""));
	}

	@Test
	public void testWithValidFile() throws Exception {
		assertTrue(ddsUtil.isInputFileValid(DdsAppConfig.INPUT_VALID_TXT_FILE_PATH));
	}

	@Test
	public void testWithInValidFile() throws Exception {
		assertFalse(ddsUtil.isInputFileValid(DdsAppConfig.INPUT_INVALID_TXT_FILE_PATH));
	}

	@Test
	public void testWithEmptyFile() throws Exception {
		assertFalse(ddsUtil.isInputFileValid(DdsAppConfig.INPUT_EMPTY_FILE_PATH));
	}

	@Test
	public void testWithWrongFileType() throws Exception {
		assertFalse(ddsUtil.isInputFileValid(DdsAppConfig.INPUT_WRONG_FILE_TYPE));
	}

	@Test
	public void testCalcDestDistance() throws Exception {
		orderDetail.setGridCoordinate("N11W5");
		assertEquals("12.083045973594572", ddsUtil.calcDestDistance(orderDetail));

		orderDetail.setGridCoordinate("S3E2");
		assertEquals("3.605551275463989", ddsUtil.calcDestDistance(orderDetail));
	}

	@Test
	public void testCalcDeliveryStartTime() throws DdsAppException {

		orderDetail.setDeliveryDistance("3.605551275463989");
		orderDetail.setOrderDate(LocalDate.now().toString());
		orderDetail.setOrderTime("05:11:50");

		String nextOrderStartTime = ddsUtil.calcDeliveryStartTime(orderDetail, DdsAppConfig.DAILY_DELIVERY_START_TIME);
		assertEquals("06:00:00", orderDetail.getActDeliveryStartTime());

		orderDetail = new OrderDetail();
		orderDetail.setDeliveryDistance("12.083045973594572");
		orderDetail.setOrderDate(LocalDate.now().toString());
		orderDetail.setOrderTime("05:11:55");

		ddsUtil.calcDeliveryStartTime(orderDetail, nextOrderStartTime);
		assertEquals("06:07:13", orderDetail.getActDeliveryStartTime());
	}

	// Test for delivery time outside the end of business i.e.; after 10:00PM
	/*
	 * @Test public void testInvalidDeliveryStartTime() { orderDetail = new
	 * OrderDetail(); orderDetail.setDeliveryDistance("12.083045973594572");
	 * orderDetail.setOrderDate(LocalDate.now().toString());
	 * orderDetail.setOrderTime("21:59:55");
	 * 
	 * try { ddsUtil.calcDeliveryStartTime(orderDetail, "22:59:55");
	 * fail("Expected an DdsAppException to be thrown"); } catch (DdsAppException
	 * ex) { assertThat(ex.getDdsErrorTypeEnum().getErrorId(),
	 * is("NON_DELIVERY_WINDOW")); }
	 * 
	 * }
	 */

	@Test
	public void testCalcNPSValue() throws Exception {
		List<OrderDetail> orderDetailList = new ArrayList<OrderDetail>();

		orderDetail = new OrderDetail();
		orderDetail.setDeliveryTimeTaken("55");
		orderDetail.setDelivered(true);
		orderDetailList.add(orderDetail);

		orderDetail = new OrderDetail();
		orderDetail.setDeliveryTimeTaken("75");
		orderDetail.setDelivered(true);
		orderDetailList.add(orderDetail);

		orderDetail = new OrderDetail();
		orderDetail.setDeliveryTimeTaken("104");
		orderDetail.setDelivered(true);
		orderDetailList.add(orderDetail);

		assertEquals(Integer.valueOf(93), ddsUtil.calcNPSValue(orderDetailList));

		orderDetail = new OrderDetail();
		orderDetail.setDeliveryTimeTaken("250");
		orderDetail.setDelivered(true);
		orderDetailList.add(orderDetail);

		assertEquals(Integer.valueOf(33), ddsUtil.calcNPSValue(orderDetailList));
	}

	@Test
	public void testCreateFilePath() throws Exception {
		assertEquals("D:\\Walmart-DDS\\data\\inputData.txt",ddsUtil.createFilePath("D:\\Walmart-DDS\\data", "inputData.txt"));
	}
	
	@Test
	public void testCreateNullFilePath() throws Exception {
		assertEquals(null,ddsUtil.createFilePath(null, "inputData.txt"));
	}


}
