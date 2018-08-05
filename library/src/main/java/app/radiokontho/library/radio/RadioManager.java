package app.radiokontho.library.radio;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class RadioManager implements IRadioManager {

    /**
     * Logging enable/disable
     */
    private static boolean isLogging = false;

    /**
     * Singleton
     */
    @SuppressLint("StaticFieldLeak")
    private static RadioManager instance = null;

    /**
     * RadioPlayerService
     */
    private static RadioPlayerService mService;

    /**
     * Context
     */
    private Context mContext;

    /**
     * Listeners
     */
    private List<RadioListener> mRadioListenerQueue;

    /**
     * Service connected/Disconnected lock
     */
    private boolean isServiceConnected;

    /**
     * Private constructor because of Singleton pattern
     *
     */
    private RadioManager(Context mContext) {
        this.mContext = mContext;
        mRadioListenerQueue = new ArrayList<>();
        isServiceConnected = false;
    }

    /**
     * Singleton
     *
     *
     */
    public static RadioManager with(Context mContext) {
        if (instance == null)
            instance = new RadioManager(mContext);
        return instance;
    }

    /**
     * get current service instance
     * @return RadioPlayerService
     */
    public static RadioPlayerService getService(){
        return mService;
    }

    /**
     * Start Radio Streaming
     *
     */
    @Override
    public void startRadio(String streamURL) {
        mService.play(streamURL);
    }

    /**
     * Stop Radio Streaming
     */
    @Override
    public void stopRadio() {
        mService.stop();
    }

    /**
     * Check if radio is playing
     *
     */
    @Override
    public boolean isPlaying() {
        log("IsPlaying : " + mService.isPlaying());
        return mService.isPlaying();
    }

    /**
     * Register listener to listen radio service actions
     *
     */
    @Override
    public void registerListener(RadioListener mRadioListener) {
        if (isServiceConnected)
            mService.registerListener(mRadioListener);
        else
            mRadioListenerQueue.add(mRadioListener);
    }

    /**
     * Set/Unset Logging
     *
     */
    @Override
    public void setLogging(boolean logging) {
        isLogging = logging;
    }

    /**
     * Connect radio player service
     */
    @Override
    public void connect() {
        log("Requested to connect service.");
        Intent intent = new Intent(mContext, RadioPlayerService.class);
        mContext.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }


    /**
     * Connection
     */
    private ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName arg0, IBinder binder) {

            log("Service Connected.");

            mService = ((RadioPlayerService.LocalBinder) binder).getService();
            mService.setLogging(isLogging);
            isServiceConnected = true;

            if (!mRadioListenerQueue.isEmpty()) {
                for (RadioListener mRadioListener : mRadioListenerQueue) {
                    registerListener(mRadioListener);
                    mRadioListener.onRadioConnected();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        }
    };

    /**
     * Logger
     */
    private void log(String log) {
        if (isLogging)
            Log.v("RadioManager", "RadioManagerLog : " + log);
    }

}
