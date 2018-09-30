package com.example.user.healthymate;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HealthyMate extends Application
{
	
	public static FirebaseAuth mFirebaseAuth;
	
	public static DatabaseReference mUsersReference;
	
	public static FirebaseUser mFirebaseUser;
	
	private static Retrofit retrofit;
	private static final String BASE_URL = "https://api.jsonbin.io";
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		
		mFirebaseAuth = FirebaseAuth.getInstance();
		
		mUsersReference = FirebaseDatabase.getInstance().getReference("users");
	}
	
	public static Retrofit getRetrofitInstance()
	{
		if (retrofit == null)
		{
			
			Gson gson = new GsonBuilder()
					.setLenient()
					.create();
			
			retrofit = new retrofit2.Retrofit.Builder()
					.baseUrl(BASE_URL)
					.addConverterFactory(GsonConverterFactory.create(gson))
					.build();
		}
		return retrofit;
	}
}
