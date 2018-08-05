package app.radiokontho.library.radio;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class StreamLinkDecoder extends AsyncTask<Void, Void, String> {

    /**
     * Stream url
     */
    private String streamUrl;

    /**
     * ResponseBuild for @streamUrl
     */
    private StringBuilder response;

    /**
     * Constructor
     */
    StreamLinkDecoder(String streamUrl) {
        this.streamUrl = streamUrl;
    }

    /**
     * DoInBackground method will create a connection
     * to stream link and check the file if there is
     * http stream link inside that file.
     * @return http stream link
     */
    @Override
    protected String doInBackground(Void... params) {
        try {
            URL website = new URL(streamUrl);
            URLConnection connection;
            connection = website.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                if (inputLine.contains("http")) {
                    int startIndex = inputLine.indexOf("http");
                    String streamUrl = inputLine.substring(startIndex);
                    response.append(streamUrl);
                    break;
                }
            }

            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        if (response == null)
            return "";
        return response.toString();
    }

}
