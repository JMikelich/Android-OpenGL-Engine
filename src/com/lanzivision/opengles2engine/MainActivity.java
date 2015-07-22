package com.lanzivision.opengles2engine;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.view.Display;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {

	// Our OpenGL Surfaceview
	private GLSurfaceView glSurfaceView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
	// Turn off the window's title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        // Super
	super.onCreate(savedInstanceState);
		
	// Fullscreen mode
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        // Screen size
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        
        // We create our Surfaceview for our OpenGL here.
        glSurfaceView = new GLSurf(this, size.x, size.y);
        
        // Set our view.	
	setContentView(R.layout.activity_main);
		
	// Retrieve our Relative layout from our main layout we just set to our view.
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.gamelayout);
        
        // Attach our surfaceview to our relative layout from our main layout.
        RelativeLayout.LayoutParams glParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        layout.addView(glSurfaceView, glParams);        
     
       
	}

	@Override
	protected void onPause() {
	super.onPause();
		
	// Preserve OpenGL Context so it doesn't reload after switching activities
        glSurfaceView.setPreserveEGLContextOnPause(true);
	glSurfaceView.onPause();
	}

	@Override
	protected void onResume() {
	super.onResume();
	glSurfaceView.onResume();
	}


}
