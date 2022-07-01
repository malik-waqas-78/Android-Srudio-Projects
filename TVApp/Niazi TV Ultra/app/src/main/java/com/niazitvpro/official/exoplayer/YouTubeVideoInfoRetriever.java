package com.niazitvpro.official.exoplayer;

import android.net.Uri;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.niazitvpro.official.utils.ReadWriteJsonFileUtils;
import com.niazitvpro.official.utils.RetrofitUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YouTubeVideoInfoRetriever {

    private static final String URL_YOUTUBE_GET_VIDEO_INFO = "https://www.youtube.com/get_video_info?&video_id=";

    public static final String KEY_DASH_VIDEO = "player_response";
    public static final String KEY_HLS_VIDEO = "hlsvp";
    private static String expression = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|" +
            "watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%2F|%2Fv%2F)" +
            "[^#\\&\\?\\n]*";
    public static final String ENCODING_UTF_8 = "UTF-8";
    private TreeMap<String, String> kvpList = new TreeMap<>();
    public String videoId;

    public void retrieve(String url) throws IOException
    {
         videoId = getVideoId(url);
        String targetUrl = URL_YOUTUBE_GET_VIDEO_INFO + videoId;
        try {
            sendGet(targetUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getInfo(String key)
    {
        return kvpList.get(key);
    }

    public String getVideoId(String videoUrl) {
        if (videoUrl == null || videoUrl.trim().length() <= 0){
            return null;
        }
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(videoUrl);
        try {
            if (matcher.find())
                return matcher.group();
        } catch (ArrayIndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void printAll()
    {
        System.out.println("TOTAL VARIABLES=" + kvpList.size());

        for(Map.Entry<String, String> entry : kvpList.entrySet())
        {
            System.out.print( "" + entry.getKey() + "=");
            System.out.println("" + entry.getValue() + "");
        }
    }

    private void parse(String data) throws UnsupportedEncodingException
    {
        String[] splits = data.split("&");
        String kvpStr = "";

        if(splits.length < 1)
        {
            return;
        }

        kvpList.clear();

        for(int i = 0; i < splits.length; ++i)
        {
            kvpStr = splits[i];

            try
            {
                // Data is encoded multiple times
                kvpStr = URLDecoder.decode(kvpStr, ENCODING_UTF_8);
                kvpStr = URLDecoder.decode(kvpStr, ENCODING_UTF_8);

                String[] kvpSplits = kvpStr.split("=", 2);

                if(kvpSplits.length == 2)
                {
                    kvpList.put(kvpSplits[0], kvpSplits[1]);
                }
                else if(kvpSplits.length == 1)
                {
                    kvpList.put(kvpSplits[0], "");
                }
            }
            catch (UnsupportedEncodingException ex)
            {
                throw ex;
            }
        }
    }



    private void sendGet(String url) throws Exception {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        HttpURLConnection httpClient =
                (HttpURLConnection) new URL(url).openConnection();

        // optional default is GET
        httpClient.setRequestMethod("GET");

        //add request header
        httpClient.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; U; Linux x86_64; en-US; rv:1.9.2.8) Gecko/20100723 Firefox/3.6.8");

        int responseCode = httpClient.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(httpClient.getInputStream()))) {

            StringBuilder response = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                response.append(line);
            }

            //print result
            Log.d("response===",""+response.toString());
            parse(response.toString());
            printAll();

        }

    }

    public String getRspYoutubeVideoUrl(String liveUrl){
        try
        {
            retrieve(liveUrl);
            String object = getInfo(YouTubeVideoInfoRetriever.KEY_DASH_VIDEO);
            try {
                JSONObject jsonObject = new JSONObject(object);
                JSONObject streamingData = jsonObject.getJSONObject("streamingData");
                if(streamingData.has("adaptiveFormats")){
                    JSONArray jsonArray = streamingData.getJSONArray("adaptiveFormats");
                    JSONObject videoInfo = jsonArray.getJSONObject(0);
                    liveUrl = videoInfo.getString("url");
                }else {

                    JSONArray jsonArray = streamingData.getJSONArray("formats");
                    JSONObject videoInfo = jsonArray.getJSONObject(0);
                    liveUrl = videoInfo.getString("url");
                }

                return liveUrl;

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.d("url===",""+liveUrl);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return "";
    }

    public  String getYoutubeLink(String url){
        String liveUrl = null;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        Document doc;

        // parse as HTML document
        try {
            doc = Jsoup.connect(url).get();

            for (Element scripts : doc.getElementsByTag("script")) {

                // get data from <script>
                for (DataNode dataNode : scripts.dataNodes()) {
                    // find data which contains backgroundColor:'#FFF'
                    String s = dataNode.getWholeData();

                    if(s.startsWith("var ytplayer =")){
                        System.out.println(""+s);
                        String[] split = s.split("=",3);
                        String response = split[2];
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            JSONObject jsonObject1 = jsonObject.getJSONObject("args");
                            String playerResponse = jsonObject1.getString("player_response");
                            JSONObject playerObject = new JSONObject(playerResponse);
                            if(playerObject.has("streamingData")) {

                                JSONObject streamingData = playerObject.getJSONObject("streamingData");
                                if(streamingData.has("hlsManifestUrl")){
                                    liveUrl= streamingData.getString("hlsManifestUrl");
                                }else if(streamingData.has("formats")){
                                    JSONArray jsonArray = streamingData.getJSONArray("formats");
                                    JSONObject object = jsonArray.getJSONObject(0);
                                    if(object.has("url")){
                                        liveUrl = object.getString("url");
                                    }
                                }else if(streamingData.has("adaptiveFormats")){
                                    JSONArray jsonArray = streamingData.getJSONArray("adaptiveFormats");
                                    JSONObject object = jsonArray.getJSONObject(0);
                                    if(object.has("url")){
                                        liveUrl = object.getString("url");
                                    }
                                }else if(streamingData.has("dashManifestUrl")){
                                    liveUrl= streamingData.getString("dashManifestUrl");
                                }
                            } else {

                                liveUrl = getRspYoutubeVideoUrl(url);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        if(liveUrl==null){
            liveUrl = url;
        }

        return  liveUrl;
    }


}
