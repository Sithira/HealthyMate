package com.example.user.healthymate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.healthymate.activities.DietActivity;

public class HomeActivity extends AppCompatActivity
{
	private LinearLayout connectDevice,
			findDoctor,
			dietPlans,
			dieticians,
			settings;
	
	private TextView mLogoutButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		connectDevice = findViewById(R.id.connectDevice);
		findDoctor = findViewById(R.id.findDoctor);
		dietPlans = findViewById(R.id.dietPlans);
		dieticians = findViewById(R.id.dieticians);
		
		mLogoutButton = (TextView) findViewById(R.id.logout);
		
		mLogoutButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				HealthyMate.mFirebaseAuth.signOut();
				
				HealthyMate.mFirebaseAuth = null;
				
				startActivity(new Intent(HomeActivity.this, LoginActivity.class));
				
				finish();
			}
		});
		
		connectDevice.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				startActivity(new Intent(HomeActivity.this, DeviceScanActivity.class));
			}
		});
		
		dietPlans.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				startActivity(new Intent(HomeActivity.this, DietActivity.class));
			}
		});
		
	}
}
