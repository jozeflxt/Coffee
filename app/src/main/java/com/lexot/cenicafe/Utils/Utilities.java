package com.lexot.cenicafe.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Utilities {
    public static void writeToFile(File directory, String nameFile, byte[] data)
    {
        try {
            File outputFile = new File(directory, nameFile);
            FileOutputStream stream = new FileOutputStream(outputFile);
            stream.write(data);
            stream.flush();
            stream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE));
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        NetworkInfo mWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        Boolean onlyWifi = PreferenceManager.getDefaultSharedPreferences(context).getBoolean("onlyWifi",true);
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected() && (!onlyWifi || mWifi.isConnected());
    }
    public static String getUser() {
        return "Cenicafe1";
    }

    public static int parseWithDefault(String s, int defaultInt) {
        return s.matches("-?\\d+") ? Integer.parseInt(s) : defaultInt;
    }
}
