package com.example.user.healthymate.DataServices;

import com.example.user.healthymate.pojos.Doctor;
import com.example.user.healthymate.pojos.Food;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIService
{

	@GET("/b/5bb064139353c37b7437d616")
	Call<List<Food>> getFoods();
	
	@GET("/b/5bb0969c9353c37b7437ef13")
	Call<List<Doctor>> getDoctors();

}
