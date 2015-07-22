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
		
	TextView txtLabel =(TextView)findViewById(R.id.textViewActTwo);
	Button btnTwo = (Button)findViewById(R.id.buttonActTwo);
	btnTwo.setOnClickListener(new OnClickListener() {

	@Override
	public void onClick(View v) {
	
		Intent i = new Intent(context, MainActivity.class);
		startActivity(i);
		
		}
	});
	}



}
