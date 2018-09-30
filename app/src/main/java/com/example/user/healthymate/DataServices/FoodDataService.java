package com.example.user.healthymate.DataServices;

import com.example.user.healthymate.pojos.Food;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface FoodDataService
{

	@GET("/b/5bb064139353c37b7437d616")
	Call<List<Food>> getFoods();

}
