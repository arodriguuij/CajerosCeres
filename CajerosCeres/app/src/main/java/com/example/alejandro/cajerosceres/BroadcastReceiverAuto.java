package com.example.alejandro.cajerosceres;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.example.alejandro.cajerosceres.DB_EntidadesBancarias.DataBaseHelperEntidadesBancarias;
import com.example.alejandro.cajerosceres.DB_EntidadesBancarias.EntidadBancaria;

public class BroadcastReceiverAuto extends BroadcastReceiver {

    private DataBaseHelperEntidadesBancarias dbhelper;

    @Override
    public void onReceive(Context context, Intent intent){
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {

            dbhelper = new DataBaseHelperEntidadesBancarias(context);
            crearTablaComisiones();

            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent2 = new Intent(context, ExampleBroadcastReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1234567, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
            // Realizar la repetición cada 60 segundos.
            // Con el primer parámetro estamos indicando que se continue ejecutando aunque el dsipositivo este con la pantalla apagada.
            // En el segundo parámetro se indica a partir de cuando comienza el scheduler, en este ejemplo es desde el momento actual
            // El tercer parámetro indica cada cuanto tiempo.
            // El cuarto hace referencia al receiver que se va a ejecutar.
            am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 6*1000, pendingIntent);

            /*Cancelar*/
            //AlarmManager am = (AlarmManager) HomeActivity.this.getSystemService(ALARM_SERVICE);
            //Intent intent = new Intent(getApplicationContext(), ExampleBroadcastReceiver.class);
            //PendingIntent pendingIntent = PendingIntent.getBroadcast(HomeActivity.this, 1234567, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            //am.cancel(pendingIntent);

            Intent pushIntent = new Intent(context, MainActivity.class);
            context.startService(pushIntent);
        }
    }

    private void crearTablaComisiones(){
        try (Cursor cur = dbhelper.getCursorEntidadBancaria()){
            if(cur.getCount()==0){
                EntidadBancaria eBancoPopular = new EntidadBancaria(1,"BancoPopular",3,3,1,1.87,2,3,3,3,2,3,3,3,2,0,1.8,1.85);
                EntidadBancaria eBancaPueyo = new EntidadBancaria(2,"BancaPueyo",3,3,1,1.87,2,3,3,3,2,3,3,3,2,0,1.8,1.85); /**/
                EntidadBancaria eBankinter = new EntidadBancaria(3,"Bankinter",3,3,1,1.87,2,3,3,3,2,3,3,3,2,0,1.8,1.85);
                EntidadBancaria eBBVA = new EntidadBancaria(4,"BBVA",3,3,1,1.87,2,3,3,3,2,3,3,3,2,0,1.8,1.85);
                EntidadBancaria eCaixa = new EntidadBancaria(5,"Caixa",3,3,1,1.87,2,3,3,3,2,3,3,3,2,0,1.8,1.85);
                EntidadBancaria eCaixaGeral = new EntidadBancaria(6,"CaixaGeral",3,3,1,1.87,2,3,3,3,2,3,3,3,2,0,1.8,1.85);
                EntidadBancaria eCajaAlmendralejo = new EntidadBancaria(7,"CajaAlmendralejo",3,3,1,1.87,2,3,3,3,2,3,3,3,2,0,1.8,1.85);
                EntidadBancaria eCajaBadajoz = new EntidadBancaria(8,"CajaBadajoz",3,3,1,1.87,2,3,3,3,2,3,3,3,2,0,1.8,1.85);
                EntidadBancaria eCajaDuero = new EntidadBancaria(9,"CajaDuero",3,3,1,1.87,2,3,3,3,2,3,3,3,2,0,1.8,1.85);
                EntidadBancaria eCajaExtremadura = new EntidadBancaria(10,"CajaExtremadura",3,3,1,1.87,2,3,3,3,2,3,3,3,2,0,1.8,1.85);
                EntidadBancaria eCajaRural = new EntidadBancaria(11,"CajaRural",3,3,1,1.87,2,3,3,3,2,3,3,3,2,0,1.8,1.85);
                EntidadBancaria eDeutscheBank = new EntidadBancaria(12,"DeutscheBank",3,3,1,1.87,2,3,3,3,2,3,3,3,2,0,1.8,1.85);
                EntidadBancaria eLiberban = new EntidadBancaria(13,"Liberban",3,3,1,1.87,2,3,3,3,2,3,3,3,2,0,1.8,1.85);
                EntidadBancaria ePopular = new EntidadBancaria(14,"Popular",3,3,1,1.87,2,3,3,3,2,3,3,3,2,0,1.8,1.85);
                EntidadBancaria eSabadell = new EntidadBancaria(15,"Sabadell",3,3,1,1.87,2,3,3,3,2,3,3,3,2,0,1.8,1.85);
                EntidadBancaria eSantander = new EntidadBancaria(16,"Santander",3,3,1,1.87,2,3,3,3,2,3,3,3,2,0,1.8,1.85);
                dbhelper.importarCajero(eBancoPopular);
                dbhelper.importarCajero(eBancaPueyo);
                dbhelper.importarCajero(eBankinter);
                dbhelper.importarCajero(eBBVA);
                dbhelper.importarCajero(eCaixa);
                dbhelper.importarCajero(eCaixaGeral);
                dbhelper.importarCajero(eCajaAlmendralejo);
                dbhelper.importarCajero(eCajaBadajoz);
                dbhelper.importarCajero(eCajaDuero);
                dbhelper.importarCajero(eCajaExtremadura);
                dbhelper.importarCajero(eCajaRural);
                dbhelper.importarCajero(eDeutscheBank);
                dbhelper.importarCajero(eLiberban);
                dbhelper.importarCajero(ePopular);
                dbhelper.importarCajero(eSabadell);
                dbhelper.importarCajero(eSantander);
                cur.close();
            }
        }
        dbhelper.close();
    }
}