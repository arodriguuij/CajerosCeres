package com.example.alejandro.cajerosceres;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;

import com.example.alejandro.cajerosceres.DB_EntidadesBancarias.DataBaseHelperEntidadesBancarias;

public class HomeActivity extends AppCompatActivity {
    private DataBaseHelperEntidadesBancarias dbhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        final int action = event.getAction();
        if(action == MotionEvent.ACTION_DOWN) {
            //startService(new Intent(HomeActivity.this, BroadcastReceiverAuto.class));
            Intent intentBroadcastReceiver = new Intent(getApplicationContext(), BroadcastReceiverAuto.class);
            sendBroadcast(intentBroadcastReceiver);
            /*
            BroadcastReceiver broadcastReceiver = new BroadcastReceiverAuto();
            //BroadcastReceiverAuto.setMainActivityHandler(this);    // Le pasamos este activity para vincularlos
            IntentFilter callInterceptorIntentFilter = new IntentFilter("android.intent.action.ANY_ACTION");
            registerReceiver(broadcastReceiver, callInterceptorIntentFilter);

            /*Intent intentBroadcastReceiver = new Intent(getApplicationContext(), BroadcastReceiverAuto.class);
            startActivity(intentBroadcastReceiver);*/
            Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent2);
        }
        return super.onTouchEvent(event);
    }
}
