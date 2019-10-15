package com.org.walmart.dds.factory;

import java.time.LocalDate;
import com.org.walmart.dds.model.OrderDetail;
import com.org.walmart.dds.util.DdsUtil;

public class DdsDataTransformFactory {

	/**
	 * This method is to extract the data from String and generate OrderDetail object.
	 * 
	 * @param inputStr
	 * @return OrderDetail
	 */
	public static OrderDetail createOrderDetailFromString(String inputStr) {
		String inputData[] = DdsUtil.getStringArray(inputStr);
		OrderDetail orderDetail = new OrderDetail();
		
		orderDetail.setOrderId(inputData[0]);
		orderDetail.setGridCoordinate(inputData[1]);
		orderDetail.setOrderDate(LocalDate.now().toString());
		orderDetail.setOrderTime(inputData[2]);
		orderDetail.setDelivered(false);
		
		return orderDetail;
	}
}
