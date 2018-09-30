package com.example.user.healthymate;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.example.user.healthymate.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity
{
	
	private EditText name, email, age, password, c_password;
	private Button register;
	
	private DatePickerDialog.OnDateSetListener onDateSetListener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		name = findViewById(R.id.name);
		email = findViewById(R.id.email);
		age = findViewById(R.id.age);
		password = findViewById(R.id.password);
		register = findViewById(R.id.register);
		
		age.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				
				Calendar calendar = Calendar.getInstance();
				
				final int year = calendar.get(Calendar.YEAR);
				
				final int month = calendar.get(Calendar.MONTH);
				
				final int day = calendar.get(Calendar.DAY_OF_MONTH);
				
				DatePickerDialog dialog = new DatePickerDialog(RegisterActivity.this,
						android.R.style.Theme_Holo, onDateSetListener, year, month, day);
				
				dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
				
				dialog.show();
				
				onDateSetListener = new DatePickerDialog.OnDateSetListener()
				{
					@Override
					public void onDateSet(DatePicker datePicker, int i, int i1, int i2)
					{
						
						String date = (month + 1) + "/" + day + "/" + year;
						
						Log.d("TIME", date);
						
						age.setText(date);
					}
				};
			
			}
		});
		
		register.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				Regist();
			}
		});
	}
	
	private void Regist()
	{
		//register.setVisibility(View.GONE);
		
		final String name = this.name.getText().toString().trim();
		final String email = this.email.getText().toString().trim();
		final String age = this.age.getText().toString().trim();
		final String password = this.password.getText().toString().trim();
		
		
		HealthyMate.mFirebaseAuth.createUserWithEmailAndPassword(email, password)
				.addOnCompleteListener(new OnCompleteListener<AuthResult>()
				{
					@Override
					public void onComplete(@NonNull Task<AuthResult> task)
					{
						
						if (task.isSuccessful())
						{
							
							User user = new User(name, email, age);
							
							HealthyMate.mFirebaseUser = task.getResult().getUser();
							
							String userID = HealthyMate.mFirebaseUser.getUid();
							
							HealthyMate.mUsersReference
									.child(userID)
									.setValue(user);
							
							startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
							
							finish();
							
						}
						
					}
				});
	}
	
}
