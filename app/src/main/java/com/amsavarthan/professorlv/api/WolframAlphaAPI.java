package com.amsavarthan.professorlv.api;

import android.util.Log;

import com.amsavarthan.professorlv.parser.StatesXMLParser;
import com.amsavarthan.professorlv.utils.Utils;
import com.amsavarthan.professorlv.models.Solution;
import com.amsavarthan.professorlv.parser.SolutionXMLParser;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WolframAlphaAPI {

    // Get query results

    public static ArrayList<Solution> getQueryResult(String query) {

        String resultXml = makeRestCall(getFormattedUrl(query));

        if (resultXml != null)
            return SolutionXMLParser.parseResultXml(resultXml, query);

        return null;
    }

    // Do URL formatting before making REST call

    public static String getFormattedUrl(String query) {

        String finalQuery = StringUtils.remove(query, " gif");
        finalQuery = StringUtils.remove(finalQuery, ".gif");
        finalQuery = StringUtils.remove(finalQuery, " png");
        finalQuery = StringUtils.remove(finalQuery, " .png");
        finalQuery = StringUtils.remove(finalQuery, " jpg");
        finalQuery = StringUtils.remove(finalQuery, " .jpg");
        finalQuery = StringUtils.remove(finalQuery, " jpeg");
        finalQuery = StringUtils.remove(finalQuery, " .jpeg");

        if (StringUtils.isEmpty(finalQuery))
            finalQuery = query;

        try {
            String uri= Utils.WOLFRAM_BASE_URL + "input=" + URLEncoder.encode(finalQuery, "utf-8") + "&appid=" + Utils.WOLFRAM_APP_ID;
            Log.i("url",uri);
            return uri;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getStateFormattedUrl(String query,String input) {

        String finalQuery = StringUtils.remove(query, " gif");
        finalQuery = StringUtils.remove(finalQuery, ".gif");
        finalQuery = StringUtils.remove(finalQuery, " png");
        finalQuery = StringUtils.remove(finalQuery, " .png");
        finalQuery = StringUtils.remove(finalQuery, " jpg");
        finalQuery = StringUtils.remove(finalQuery, " .jpg");
        finalQuery = StringUtils.remove(finalQuery, " jpeg");
        finalQuery = StringUtils.remove(finalQuery, " .jpeg");

        if (StringUtils.isEmpty(finalQuery))
            finalQuery = query;

        try {
            String uri= Utils.WOLFRAM_BASE_URL + "input=" + URLEncoder.encode(finalQuery, "utf-8") + "&appid=" + Utils.WOLFRAM_APP_ID+Utils.WOLFRAM_POD_STATE_BASE_URL+input.replace(" ","%20");
            Log.i("url",uri);
            return uri;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String makeRestCall(String url) {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = null;

        try {
            response = client.newCall(request).execute();
            if (response != null)
                return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    public static ArrayList<Solution> getStateQueryResult(String query,String title,String input,String name) {
        String resultXml = makeRestCall(getStateFormattedUrl(query,input));

        if (resultXml != null)
            return StatesXMLParser.parseResultXml(resultXml, query,title,name);

        return null;
    }
}
