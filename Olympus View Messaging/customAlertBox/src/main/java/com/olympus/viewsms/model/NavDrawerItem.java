package com.olympus.viewsms.model;

public class NavDrawerItem {
	private String title;
    private int icon;
    private boolean isCheckBoxVisible = false;
    
    public NavDrawerItem(String title, int icon, boolean isCheckBoxVisible){
    	this.title=title;
    	this.icon=icon;
    	this.isCheckBoxVisible=isCheckBoxVisible;
    }
    public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getIcon() {
		return icon;
	}
	public void setIcon(int icon) {
		this.icon = icon;
	}
	public boolean isCheckBoxVisible() {
		return isCheckBoxVisible;
	}
	public void setCheckBoxVisible(boolean isCheckBoxVisible) {
		this.isCheckBoxVisible = isCheckBoxVisible;
	}

}
