package com.chierici.speechrecog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StartSpeechServiceReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, SpeechService.class);
        context.startService(service);
    }
}
