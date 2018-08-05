package app.radiokontho.mrj;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import app.radiokontho.library.radio.RadioListener;
import app.radiokontho.library.radio.RadioManager;

public class RadioActivity extends Activity implements RadioListener{

    private final String[] RADIO_URL = {"http://95.154.254.157:18259"};

    ImageButton mButtonControlStart, logo_radio;
    TextView mTextViewControl;
    RadioManager mRadioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio);

        mRadioManager = RadioManager.with(getApplicationContext());
        mRadioManager.registerListener(this);
        mRadioManager.setLogging(true);

        initializeUI();

        logo_radio = findViewById(R.id.logo_radio);

        logo_radio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent developer = new Intent(RadioActivity.this, app.radiokontho.mrj.developer.class);
                startActivity(developer);
            }
        });
    }

    public void initializeUI() {
        mButtonControlStart = findViewById(R.id.buttonControlStart);
        mTextViewControl = findViewById(R.id.textviewControl);

        mButtonControlStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mRadioManager.isPlaying())
                    mRadioManager.startRadio(RADIO_URL[0]);
                else
                    mRadioManager.stopRadio();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRadioManager.connect();
    }

    @Override
    public void onRadioLoading() {
        runOnUiThread(new Runnable() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                //TODO Do UI works here.
                mTextViewControl.setText("STATUS : LOADING");
                mButtonControlStart.setBackgroundResource(R.drawable.play_w);
            }
        });
    }

    @Override
    public void onRadioConnected() {

    }

    @Override
    public void onRadioStarted() {
        runOnUiThread(new Runnable() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                //TODO Do UI works here.
                mTextViewControl.setText("STATUS : PLAYING");
                mButtonControlStart.setBackgroundResource(R.drawable.pause_w);
            }
        });
    }

    @Override
    public void onRadioStopped() {
        runOnUiThread(new Runnable() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                //TODO Do UI works here
                mTextViewControl.setText("STATUS : STOPPED");
                mButtonControlStart.setBackgroundResource(R.drawable.play_w);
            }
        });
    }

    @Override
    public void onMetaDataReceived(String s, String s1) {
        //TODO Check metadata values. Singer name, song name or whatever you have.
    }

    @Override
    public void onError() {

    }
}
