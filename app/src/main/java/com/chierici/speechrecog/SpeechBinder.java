package com.chierici.speechrecog;

import android.os.Binder;

public class SpeechBinder extends Binder {
    private SpeechService speechService;

    public SpeechBinder(SpeechService speechService) {
        this.speechService = speechService;
    }

    public SpeechService getSpeechService() {
        return speechService;
    }
}