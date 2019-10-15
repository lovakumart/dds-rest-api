package com.org.walmart.dds.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class OrderDetail {

	/** Pojo/Model of the object/attributes which will be maintained **/
	
	private String droneId;
	private String orderId;
	private String orderDate;
	private String orderTime;
	private String gridCoordinate;
	private String deliveryDistance;
	private String actDeliveryStartDate;
	private String actDeliveryStartTime;
	private String actDeliveryEndDate;
	private String actDeliveryEndTime;
	private String finalRoundTripDate;
	private String finalRoundTripTime;
	private String deliveryTimeTaken;
	private String status;
	private String comments;
	private boolean isDelivered;

	public String getDroneId() {
		return droneId;
	}

	public void setDroneId(String droneId) {
		this.droneId = droneId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getGridCoordinate() {
		return gridCoordinate;
	}

	public void setGridCoordinate(String gridCoordinate) {
		this.gridCoordinate = gridCoordinate;
	}

	public String getDeliveryDistance() {
		return deliveryDistance;
	}

	public void setDeliveryDistance(String deliveryDistance) {
		this.deliveryDistance = deliveryDistance;
	}

	public String getActDeliveryStartDate() {
		return actDeliveryStartDate;
	}

	public void setActDeliveryStartDate(String actDeliveryStartDate) {
		this.actDeliveryStartDate = actDeliveryStartDate;
	}

	public String getActDeliveryStartTime() {
		return actDeliveryStartTime;
	}

	public void setActDeliveryStartTime(String actDeliveryStartTime) {
		this.actDeliveryStartTime = actDeliveryStartTime;
	}

	public String getActDeliveryEndDate() {
		return actDeliveryEndDate;
	}

	public void setActDeliveryEndDate(String actDeliveryEndDate) {
		this.actDeliveryEndDate = actDeliveryEndDate;
	}

	public String getActDeliveryEndTime() {
		return actDeliveryEndTime;
	}

	public void setActDeliveryEndTime(String actDeliveryEndTime) {
		this.actDeliveryEndTime = actDeliveryEndTime;
	}

	public String getFinalRoundTripDate() {
		return finalRoundTripDate;
	}

	public void setFinalRoundTripDate(String finalRoundTripDate) {
		this.finalRoundTripDate = finalRoundTripDate;
	}

	public String getFinalRoundTripTime() {
		return finalRoundTripTime;
	}

	public void setFinalRoundTripTime(String finalRoundTripTime) {
		this.finalRoundTripTime = finalRoundTripTime;
	}

	public String getDeliveryTimeTaken() {
		return deliveryTimeTaken;
	}

	public void setDeliveryTimeTaken(String deliveryTimeTaken) {
		this.deliveryTimeTaken = deliveryTimeTaken;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public boolean isDelivered() {
		return isDelivered;
	}

	public void setDelivered(boolean isDelivered) {
		this.isDelivered = isDelivered;
	}
	
	

}
