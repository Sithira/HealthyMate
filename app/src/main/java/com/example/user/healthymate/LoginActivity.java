package com.example.user.healthymate;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class LoginActivity extends AppCompatActivity
{
	private EditText email, password;
	private Button login;
	private TextView register_here;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		email = findViewById(R.id.email);
		password = findViewById(R.id.password);
		login = findViewById(R.id.login);
		register_here = findViewById(R.id.register_here);
		
		login.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				String mEmail = email.getText().toString();
				String mPass = password.getText().toString();
				
				if (!mEmail.isEmpty() || !mPass.isEmpty())
				{
					
					HealthyMate.mFirebaseAuth.signInWithEmailAndPassword(mEmail, mPass)
							.addOnCompleteListener(new OnCompleteListener<AuthResult>()
							{
								@Override
								public void onComplete(@NonNull Task<AuthResult> task)
								{
								
									if (task.isSuccessful())
									{
										startActivity(new Intent(LoginActivity.this, HomeActivity.class));
										
										finish();
									}
									
								}
							});
				}
				else
				{
					email.setError("Username cannot be empty");
					password.setError("Password cannot be empty");
				}
			}
		});
		
		register_here.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
				
			}
		});
		
	}
	
}
