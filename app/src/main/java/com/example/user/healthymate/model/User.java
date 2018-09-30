package com.example.user.healthymate.model;

public class User
{
	
	public String
			id,
			name,
			email,
			dob;
	
	User()
	{
	
	}
	
	public User(String name, String email, String dob)
	{
		this.name = name;
		this.email = email;
		this.dob = dob;
	}
}
