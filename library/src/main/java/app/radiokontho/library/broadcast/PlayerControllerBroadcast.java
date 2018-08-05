package app.radiokontho.library.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import app.radiokontho.library.radio.RadioManager;
import app.radiokontho.library.radio.RadioPlayerService;

public class PlayerControllerBroadcast extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {

        boolean isRadioServiceBinded = RadioManager.getService() != null;

        String action = intent.getAction();

        if (action != null) {
            if(action.equals(RadioPlayerService.NOTIFICATION_INTENT_PLAY_PAUSE)
                    && isRadioServiceBinded){
                if(RadioManager.getService().isPlaying())
                    RadioManager.getService().stop();
                else
                    RadioManager.getService().resume();
            }else if(action.equals(RadioPlayerService.NOTIFICATION_INTENT_CANCEL)
                    && isRadioServiceBinded){
                RadioManager.getService().stopFromNotification();
            }
        }

    }
}
