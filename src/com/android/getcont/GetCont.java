package com.android.getcont;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Button;

public class GetCont extends Activity {
	
	Button btnSendSMS;		
	IntentFilter intentFilter;	
	
	public static final String PREFS_NAME = "MyPrefsFile";
		
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_cont); 
        
        boolean firstrun = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).getBoolean("firstrun", true);
        if (firstrun)
        {				       
        	/* Save the state of the Application, FirstRun over */
        	getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
            .edit()
            .putBoolean("firstrun", false)
            .commit();

        	/* For saving the Security Code for the Application on the FirstRun */
            Intent j = new Intent(this, SavePword.class);
            j.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(j); 
        
        }		
    }
}