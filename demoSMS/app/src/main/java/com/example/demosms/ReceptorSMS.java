package com.example.demosms;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

/**
 * Created by ilm on 25/11/2014.
 */
public class ReceptorSMS extends BroadcastReceiver {
    private final String SMS_RECEIVED="android.provider.Telephony.SMS_RECEIVED";
    private onRecibeSMS respuesta;

    public void setOnRecibeSMSListener(Activity x){
        respuesta=(onRecibeSMS) x;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(SMS_RECEIVED)) {
            //Esto aborta notificaciones a otros...
            this.abortBroadcast();

            //---get the SMS message passed in---
            Bundle bundle = intent.getExtras();
            SmsMessage[] msgs = null;
            String origen = null;
            String msg = null;

            if (bundle != null) {
                //obtenemos el mensaje original SMS:
                Object[] pdus = (Object[]) bundle.get("pdus");
                msgs = new SmsMessage[pdus.length];
                for (int i = 0; i < msgs.length; i++) {
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    origen = msgs[i].getOriginatingAddress();
                    msg = msgs[i].getMessageBody().toString();
                }

                //informamos a la activity de la llegada del mensaje
                respuesta.onRecibeSMS(origen,msg);
                Toast.makeText(context, "SMS Recibido: ", Toast.LENGTH_LONG).show();

                //continua el proceso normal de broadcast
                // es decir, llega el sms y se almacena en la bandeja de entrada
                this.clearAbortBroadcast();
            }
        }
    }

    //interfaz para la actividad entre el broadcast Receiver y la actividad
    public interface onRecibeSMS{
        public void onRecibeSMS(String origen, String mensaje);
    }

}