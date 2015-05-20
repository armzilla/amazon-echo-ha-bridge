package com.armzilla.ha.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;

// Usage JSONObject json = readJsonFromUrl("https://graph.facebook.com/19292868552");

public class JsonReader {

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String urlString) throws IOException, JSONException {
        return readJsonFromUrl(urlString, null);
    }

    public static JSONObject readJsonFromUrl(String urlString, String authorizationkey) throws IOException, JSONException {
        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();
        if (authorizationkey != null) {
            conn.addRequestProperty("Authorization", "Bearer " + authorizationkey);
        }
        InputStream is = url.openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            System.out.print("URL returned Text:[" + jsonText + "]\n");
            // Hack....ui is returning a converted array of json for the list of endpoints
            // but since I only have one it always returns 1 so for now I will just strip
            // leading and trailing [ ] to convert to a single json
            // this will break if I add a second endpoint but I don't know the syntax of the
            // output with more than one so I need to see to corretly parse and convert into a list.
            // not sure if it is [{....},{....}] or [{....}{.....}] or something else
            if (jsonText.charAt(0) == '[') {
                jsonText = jsonText.substring(1,jsonText.length()-2); // Strip 1st and last char...

            }
            System.out.println("About to parse [ ] from text Text:[" + jsonText + "]");
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    public static JSONArray readJsonArrayFromUrl(String urlString) throws IOException, JSONException {
        return readJsonArrayFromUrl(urlString, null);
    }

    public static JSONArray readJsonArrayFromUrl(String urlString, String authorizationkey) throws IOException, JSONException {
        URL url = new URL(urlString);
        System.out.println("Url:" + url);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        if (authorizationkey != null) {
            System.out.println("Adding Header Property Authorization: Bearer " + authorizationkey);
            conn.addRequestProperty("Authorization", "Bearer " + authorizationkey);
        }
        InputStream is = conn.getInputStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            System.out.println("About to parse Text:" + jsonText + "");
            JSONArray json = new JSONArray(jsonText);
            return json;
        } finally {
            is.close();
        }
    }
}
