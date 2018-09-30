package com.example.user.healthymate.pojos;

import com.google.gson.annotations.SerializedName;

public class Doctor{

	@SerializedName("img")
	private String img;

	@SerializedName("phone")
	private String phone;

	@SerializedName("name")
	private String name;

	@SerializedName("hname")
	private String hname;

	public void setImg(String img){
		this.img = img;
	}

	public String getImg(){
		return img;
	}

	public void setPhone(String phone){
		this.phone = phone;
	}

	public String getPhone(){
		return phone;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setHname(String hname){
		this.hname = hname;
	}

	public String getHname(){
		return hname;
	}

	@Override
 	public String toString(){
		return 
			"Doctor{" + 
			"img = '" + img + '\'' + 
			",phone = '" + phone + '\'' + 
			",name = '" + name + '\'' + 
			",hname = '" + hname + '\'' + 
			"}";
		}
}