package com.example.user.healthymate.pojos;

import com.google.gson.annotations.SerializedName;

public class Food{

	@SerializedName("img")
	private String img;

	@SerializedName("name")
	private String name;

	@SerializedName("description")
	private String description;

	public void setImg(String img){
		this.img = img;
	}

	public String getImg(){
		return img;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	@Override
 	public String toString(){
		return 
			"Food{" + 
			"img = '" + img + '\'' + 
			",name = '" + name + '\'' + 
			",description = '" + description + '\'' + 
			"}";
		}
}