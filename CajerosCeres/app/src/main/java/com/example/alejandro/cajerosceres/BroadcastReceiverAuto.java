package com.example.alejandro.cajerosceres;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BroadcastReceiverAuto extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent){

        // Intent para cargar los datos de los cajeros automáticos del JSON
        Intent intentBroadcastReceiver = new Intent(context, ExampleBroadcastReceiver.class);
        context.sendBroadcast(intentBroadcastReceiver);

        // Alarma para cargar los datos de los cajeros automáticos del JSON cada 24 horas
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent2 = new Intent(context, ExampleBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1234567, intent2, PendingIntent.FLAG_UPDATE_CURRENT);

        // Realizar la repetición cada 24 horas: 6*1000*60*24.
        // Con el primer parámetro estamos indicando que se continue ejecutando aunque el dsipositivo este con la pantalla apagada.
        // En el segundo parámetro se indica a partir de cuando comienza el scheduler, en este ejemplo es desde el momento actual
        // El tercer parámetro indica cada cuanto tiempo.
        // El cuarto hace referencia al receiver que se va a ejecutar.
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 6*1000*60*24, pendingIntent);
    }
}