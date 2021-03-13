package com.eluon.pim.value;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SubscriptionInfoVO {

	@JsonProperty("Subscription_no")
	private int no;
	@JsonProperty("Subscription_type")
	private int type;
	@JsonProperty("Target_no")
	private int targetNo;
	@JsonProperty("Notify_url")
	private String notifyUrl;
	@JsonProperty("Client_id")
	private String clientId;
	
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getTargetNo() {
		return targetNo;
	}
	public void setTargetNo(int targetNo) {
		this.targetNo = targetNo;
	}
	public String getNotifyUrl() {
		return notifyUrl;
	}
	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	
	@Override
	public String toString() {
		return "SubscriptionInfoVO [no=" + no + ", type=" + type + ", targetNo=" + targetNo + ", notifyUrl=" + notifyUrl
				+ ", clientId=" + clientId + "]";
	}

}
