package com.olympus.viewsms.model;

public class Contact {
	
	private String name;
	private String number;
	private Boolean check;
	
	public Contact(String name,String number){
		this.name=name;
		this.number=number;
		check=false;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
	public Boolean getCheck() {
		return check;
	}
	public void setCheck(Boolean check) {
		this.check = check;
	}

	

}
