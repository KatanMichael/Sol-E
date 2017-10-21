package com.example.hadas.ourrobot;

import android.os.AsyncTask;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Created by hadas on 29/08/2017.
 */

public class TaskTest extends AsyncTask<String,Void,String>  {
    private Socket client;
    private int port;
    private String ip;
    private String mess;
    private String messError;
    public static ErrorMsg error;
    private OutputStreamWriter osw;

    /*every client(msg from app) connect to robot with this two parameters*/
    public TaskTest() {
        ip = "10.0.1.75";
        port = 10;
    }

    /*connecting to server (robot) and sending a message*/
   @Override
    protected String doInBackground(String... params) {
       client = null;
       osw = null;
        try {
            client = new Socket(ip, port);
            osw = new OutputStreamWriter(client.getOutputStream());
            mess = params[0];
            osw.write(mess + "\n");
            osw.flush();
            messError = "data send";
        } catch (IOException e) {
            messError = "client fail";
        } finally {
            try {
                if (osw != null) {
                    osw.close();
                }if(client!=null){
                    client.close();
                }
            } catch (IOException e) {
               messError = "close fail";
            }
        }
        return messError;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
    }

/*    @Override
   protected void onPostExecute(String result) {
        error.setError(result);
    }*/
}
