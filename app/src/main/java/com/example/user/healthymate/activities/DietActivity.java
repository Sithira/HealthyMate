package com.example.user.healthymate.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.user.healthymate.DataServices.APIService;
import com.example.user.healthymate.HealthyMate;
import com.example.user.healthymate.R;
import com.example.user.healthymate.adapters.RecyclerViewAdapter;
import com.example.user.healthymate.model.Anime;
import com.example.user.healthymate.pojos.Doctor;
import com.example.user.healthymate.pojos.Food;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DietActivity extends AppCompatActivity
{
	
	private final String URL = "http://api.jsonbin.io/b/5bb064139353c37b7437d616";
	private JsonArrayRequest request;
	private RequestQueue requestQueue;
	private List<Anime> list;
	private RecyclerView recyclerView;
	
	private List<Food> foodList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_diet);
		
		list = new ArrayList<>();
		recyclerView = findViewById(R.id.rv_list);
		//jsonrequest();
		
		fetchData();
		
	}
	
	/**
	 * Get all Food data from API
	 *
	 */
	private void fetchData()
	{
		// Retrofit
		APIService service = HealthyMate.getRetrofitInstance().create(APIService.class);
		
		// food data
		Call<List<Food>> foods = service.getFoods();
		
		// doctor data
//		Call<List<Doctor>> doctors = service.getDoctors();
//
//		doctors.enqueue(new Callback<List<Doctor>>()
//		{
//			@Override
//			public void onResponse(Call<List<Doctor>> call, Response<List<Doctor>> response)
//			{
//				// get data from API Call
//				List<Doctor> doctorList = response.body();
//
//				Doctor doctor = doctorList.get(0);
//
//				doctor.getImg();
//			}
//
//			@Override
//			public void onFailure(Call<List<Doctor>> call, Throwable t)
//			{
//
//			}
//		});
		
		foods.enqueue(new Callback<List<Food>>()
		{
			@Override
			public void onResponse(Call<List<Food>> call, retrofit2.Response<List<Food>> response)
			{
				List<Food> foodList = response.body();
			}
			
			@Override
			public void onFailure(Call<List<Food>> call, Throwable t)
			{
				
				Log.e("RETRO", t.getMessage());
				
				System.out.println(call.request().url());
				
				Toast.makeText(DietActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
			}
		});
	
	}
	
	public void setuprecyclerView(List<Food> list)
	{
		
		RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, list);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.setAdapter(adapter);
	}
}
