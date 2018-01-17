package com.android.getcont;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.widget.TextView;
import android.widget.Toast;
import android.telephony.SmsManager;
import android.util.Log;

public class GetNumber extends Activity {

	TextView TView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_number);
        
        TView = (TextView) findViewById(R.id.textView2);       
        
        String defaultName="Arjun Mathew Dan";
        String defaultNumber="5556";
        Bundle extras = getIntent().getExtras();

        
        if (extras!=null)
        {
        	defaultName = extras.getString("Name");
        	defaultNumber = extras.getString("Number");
        }
        	
        	if (defaultName!=null && defaultName.length()>0) {
					
			        String tag = "AMD";        
			        
			     	/* Find contact based on name */
			        
			        ContentResolver cr = getContentResolver();

			        // Case Insensitive			  
			        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI,
			                null, ContactsContract.Contacts.DISPLAY_NAME + " LIKE '%" + defaultName + "%'", null, null);			        

			        
/*			        // Case Sensitive		        
			        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
			            "DISPLAY_NAME = '" + defaultName + "'", null, null);
*/
			        if (cursor.moveToFirst()) {
			            String contactId =
			                cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
			            
			            /* Get all phone numbers */
			            
			            Cursor phones = cr.query(Phone.CONTENT_URI, null,
			                Phone.CONTACT_ID + " = " + contactId, null, null);
			            while (phones.moveToNext()) {
			                String number = phones.getString(phones.getColumnIndex(Phone.NUMBER));
			                int type = phones.getInt(phones.getColumnIndex(Phone.TYPE));
			                switch (type) {
			                    case Phone.TYPE_HOME:
			                        /* do something with the Home number here */
			                        break;
			                    case Phone.TYPE_MOBILE:
			                    	Log.d(tag, number);
			                    	Toast.makeText(GetNumber.this, "Mobile : " + number, Toast.LENGTH_LONG).show();
			                    	sendSMS(defaultNumber, number);
			                    	TView.setText("Contact details of " + defaultName + " sent to " + defaultNumber);
			                        /* do something with the Mobile number here */
			                        break;
			                    case Phone.TYPE_WORK:
			                        /* do something with the Work number here */
			                        break;
			                    }
			            }

			            phones.close();
			        }
			        cursor.close();            																																										
				}
        		//finish();
			}
    private void sendSMS(String phoneNumber, String message)
    {        
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";
 
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
            new Intent(SENT), 0);
 
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
            new Intent(DELIVERED), 0);
 
        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS sent", 
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure", 
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service", 
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU", 
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off", 
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));
 
        //---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS delivered", 
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered", 
                                Toast.LENGTH_SHORT).show();
                        break;                        
                }
            }
        }, new IntentFilter(DELIVERED));        
 
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);        
    } 
}
