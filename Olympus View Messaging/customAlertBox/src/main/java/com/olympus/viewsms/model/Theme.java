package com.olympus.viewsms.model;

import android.content.Context;

import com.olympus.viewsms.util.Scale;

public class Theme {
	
	private int id,is_paid,is_coming_soon;
	private String name,product_id;
	private float price;

	private String ctitle,ctext,reply_color;  //color
	public int L,R,T,B,h1,h2,L2,R2,button_bottom,button_width,content_height,content_width,content_top,content_left,theme_height,reply_box_width;  //padding dimen in pixel
	private int title_layout;
    private int is_button_divider;
	private int theme_type;

	Context cxt;

	public Theme(Context cxt){this.cxt=cxt;}

	public Theme(Context cxt,int id, String name, float price, int is_paid, String product_id,int is_coming_soon,
			String ctitle, String ctext,String reply_color, int l,int r, int t, int b,int h1, int h2,int title_layout,int theme_type,int button_bottom,int button_width,int is_button_divider,int content_width,int content_height,int content_left,int content_top,int theme_height,int reply_box_width) {
		super();
		this.cxt=cxt;
		this.id = id;
		this.name = name;
		this.price = price;
		this.is_paid = is_paid;
		this.product_id=product_id;
		this.is_coming_soon=is_coming_soon;
		this.ctitle = ctitle;
		this.ctext = ctext;
		this.reply_color=reply_color;
		L = l;
		R = r;
		T = t;
		B = b;
		this.h1 = h1;
		this.h2 = h2;
		this.title_layout=title_layout;
		this.theme_type=theme_type;
		this.button_width=button_width;
		this.button_bottom=button_bottom;
		this.is_button_divider=is_button_divider;
		this.content_height=content_height;
		this.content_top=content_top;
		this.content_width=content_width;
		this.content_left=content_left;
		this.theme_height=theme_height;
		this.reply_box_width=reply_box_width;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}

	public int getIs_paid() {
		return is_paid;
	}

	public void setIs_paid(int is_paid) {
		this.is_paid = is_paid;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public int getIs_coming_soon() {
		return is_coming_soon;
	}

	public void setIs_coming_soon(int is_coming_soon) {
		this.is_coming_soon = is_coming_soon;
	}

	public String getCtitle() {
		return ctitle;
	}

	public void setCtitle(String ctitle) {
		this.ctitle = ctitle;
	}

	public String getCtext() {
		return ctext;
	}

	public void setCtext(String ctext) {
		this.ctext = ctext;
	}

	public int getL() {
		return Scale.cvDPtoPX(cxt, L);
	}

	public void setL(int l) {
		L = l;
	}

	public int getR() {
		return Scale.cvDPtoPX(cxt, R);
	}

	public void setR(int r) {
		R = r;
	}

	public int getT() {
		return Scale.cvDPtoPX(cxt, T);
	}

	public void setT(int t) {
		T = t;
	}

	public int getB() {
		return Scale.cvDPtoPX(cxt, B);
	}

	public void setB(int b) {
		B = b;
	}

	public int getH1() {
		return Scale.cvDPtoPX(cxt, h1);
	}

	public void setH1(int h1) {
		this.h1 = h1;
	}

	public int getH2() {
		return Scale.cvDPtoPX(cxt, h2);
	}

	public void setH2(int h2) {
		this.h2 = h2;
	}

	public int getL2() {
		return Scale.cvDPtoPX(cxt, L2);
	}

	public void setL2(int l2) {
		L2 = l2;
	}

	public int getR2() {
		return Scale.cvDPtoPX(cxt, R2);
	}

	public void setR2(int r2) {
		R2 = r2;
	}

	public int getTitle_layout() {
		return title_layout;
	}

	public void setTitle_layout(int title_layout) {
		this.title_layout = title_layout;
	}

	public int getTheme_type() {
		return theme_type;
	}

	public void setTheme_type(int theme_type) {
		this.theme_type = theme_type;
	}

	public int getButton_bottom() {
		return Scale.cvDPtoPX(cxt, button_bottom);
	}

	public void setButton_bottom(int button_bottom) {
		this.button_bottom = button_bottom;
	}

	public int getButton_width() {
		return Scale.cvDPtoPX(cxt, button_width);
	}

	public void setButton_width(int button_width) {
		this.button_width = button_width;
	}
	public int getIs_button_divider() {
		return is_button_divider;
	}

	public void setIs_button_divider(int is_button_divider) {
		this.is_button_divider = is_button_divider;
	}
	public int getContent_height() {
		return content_height;
	}

	public void setContent_height(int content_height) {
		this.content_height = content_height;
	}

	public int getContent_width() {
		return content_width;
	}

	public void setContent_width(int content_width) {
		this.content_width = content_width;
	}

	public int getContent_top() {
		return content_top;
	}

	public void setContent_top(int content_top) {
		this.content_top = content_top;
	}

	public int getContent_left() {
		return content_left;
	}

	public void setContent_left(int content_left) {
		this.content_left = content_left;
	}
	public int getTheme_height() {
		return theme_height;
	}

	public void setTheme_height(int theme_height) {
		this.theme_height = theme_height;
	}
	public String getReply_color() {
		return reply_color;
	}

	public void setReply_color(String reply_color) {
		this.reply_color = reply_color;
	}

	public int getReply_box_width() {
		return reply_box_width;
	}

	public void setReply_box_width(int reply_box_width) {
		this.reply_box_width = reply_box_width;
	}

}
