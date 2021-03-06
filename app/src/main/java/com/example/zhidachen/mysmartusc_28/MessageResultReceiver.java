package com.example.zhidachen.mysmartusc_28;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

public class MessageResultReceiver extends ResultReceiver {

    private Receiver mReceiver;

    public MessageResultReceiver(Handler handler) {
        super(handler);
    }

    public interface Receiver {
        public void onReceiveResult(int resultCode, Bundle resultData);

    }

    public void setReceiver(Receiver receiver) {
        mReceiver = receiver;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {

        if (mReceiver != null) {
            mReceiver.onReceiveResult(resultCode, resultData);
        }
    }

}
