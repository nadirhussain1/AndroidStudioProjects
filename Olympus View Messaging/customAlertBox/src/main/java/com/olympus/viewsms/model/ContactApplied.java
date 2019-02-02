package com.olympus.viewsms.model;

public class ContactApplied extends Contact{
	private int id, theme_id;


    private String thumbnailUri="";
    
    public ContactApplied(){
    	super("", "");
    }
 
    public ContactApplied(int id,String name, String number,int theme_id,String thumbnailUri) {
    	super(name, number);
    	this.id = id;
        this.theme_id=theme_id;
        this.thumbnailUri=thumbnailUri;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    @Override
    public String toString() {
        return getName() + "\n" + getNumber();
    }
	public int getTheme_id() {
		return theme_id;
	}

	public void setTheme_id(int theme_id) {
		this.theme_id = theme_id;
	}
    public String getThumbnailUri() {
        return thumbnailUri;
    }



}
