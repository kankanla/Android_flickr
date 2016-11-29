package com.example.e560.m1117a.Tools;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by E560 on 2016/11/17.
 * <p>
 */

public class FlickrAPI {

    private HashMap<String, String> search_opt;
    private String API_URL;

    public FlickrAPI() {
        API_URL = "https://api.flickr.com/services/rest/?";
        search_opt = new HashMap<String, String>();
        search_opt.put("method", "flickr.photos.search");
        search_opt.put("api_key", "665f189ad2f511c7c9595aa9a845da03");
        search_opt.put("format", "json");
        search_opt.put("nojsoncallback", "1");
    }

    public HashMap<String, String> getsearch_opt() {
        return search_opt;
    }

    public String getAPI_URL() {
        return API_URL;
    }

    public static String search(String text) throws UnsupportedEncodingException {
        FlickrAPI flickrapi = new FlickrAPI();
        StringBuffer url_string = new StringBuffer();
        HashMap<String, String> search_url = flickrapi.getsearch_opt();
        search_url.put("text", URLEncoder.encode(text, "UTF-8"));
        Set<String> key = search_url.keySet();
        for (String str : key) {
            if (search_url.get(str) != null && search_url.get(str) != "") {
                url_string.append(str + "=" + search_url.get(str) + "&");
            }
        }
//        System.out.println(flickrapi.getAPI_URL() + url_string);
        return flickrapi.getAPI_URL() + url_string;
    }
}