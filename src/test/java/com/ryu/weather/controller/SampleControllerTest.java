package com.ryu.weather.controller;

import com.ryu.weather.common.Util;
import jdk.nashorn.internal.parser.JSONParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class SampleControllerTest {


    //단기 예보 조회
    @Test
    public void getWetherShort() throws IOException {
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst");

        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" + Util.getServiceKey());
        /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8"));
        /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("1000", "UTF-8"));
        /*요청자료형식(XML/JSON) Default: XML*/
        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8"));
        /*‘21년 6월 28일 발표*/
        urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode(Util.getToday(), "UTF-8"));
        /*06시 발표(정시단위) */
        urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode("0500", "UTF-8"));
        /*예보지점의 X 좌표값*/
        urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode("88", "UTF-8"));
        /*예보지점의 Y 좌표값*/
        urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode("89", "UTF-8"));

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());

        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();



        try {
            String str = sb.toString();
            //System.out.println(str);
            JSONObject obj = new JSONObject(str);
            obj = (JSONObject) obj.get("response");
            obj = (JSONObject) obj.get("body");
            obj = (JSONObject) obj.get("items");
            JSONArray array = (JSONArray) obj.get("item");

            for (int i = 0; i < array.length(); i++){
                obj = (JSONObject) array.get(i);
//              System.out.print(obj.get("fcstDate") + ",");
//              System.out.print(obj.get("fcstTime") + ",");
//              System.out.print(obj.get("category") + ",");
//              System.out.println(obj.get("fcstValue"));
                if(obj.get("fcstDate").equals(Util.getSomeDayMore(1))) {
                    System.out.print(obj.get("category") + ",");
                    System.out.println(obj.get("fcstValue"));
                }

                if(obj.get("fcstDate").equals(Util.getSomeDayMore(2))) {
                    break;
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getDateTime() {
        System.out.println(Util.getSomeDayMore(1));
    }






}