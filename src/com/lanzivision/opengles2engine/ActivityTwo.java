package com.lanzivision.opengles2engine;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ActivityTwo extends Activity {

	private static Context context;
	

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		context = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_two);
		
		 int age = getIntent().getIntExtra("user-age", -1);
		String name = getIntent().getStringExtra("user-name");
		
		TextView txtLabel =(TextView)findViewById(R.id.textViewActTwo);
		
		//txtLabel.setText("Your name is " + name +" you are " +age+ " years old.");
		
		
		Button btnTwo = (Button)findViewById(R.id.buttonActTwo);
		
		btnTwo.setOnClickListener(new OnClickListener() {
			
			
			
			@Override
			public void onClick(View v) {
				//Intent intent = new Intent();
				//intent.putExtra("dog-years", 210);
				//setResult(RESULT_OK, intent);
				//Intent goToMainActivity = new Intent(context,MainActivity.class);
				  // goToMainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Will clear out your activity history stack till now
				   //startActivity(goToMainActivity);
				//Intent intent = new Intent(context,MainActivity.class);
				//context.startActivity(intent);
				
			
				
				Intent i = new Intent(context, MainActivity.class);
				startActivity(i);

				
				
			}
		});
	}



}
