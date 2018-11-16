package com.novip.model;

public class BaseResponse {
	
	private boolean success = true;
	private String note = "success";
	
	public BaseResponse() {
		
	}
public BaseResponse(boolean success,String note) {
		this.success  = success;
		this.note = note;
	}

	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
	
}
