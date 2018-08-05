package app.radiokontho.library.radio;


public interface IRadioManager {

    void startRadio(String streamURL);

    void stopRadio();

    boolean isPlaying();

    void registerListener(RadioListener mRadioListener);

    void setLogging(boolean logging);

    void connect();

}
