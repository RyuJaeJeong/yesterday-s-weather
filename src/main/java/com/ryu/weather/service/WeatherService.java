package com.ryu.weather.service;

import com.ryu.weather.common.Util;
import com.ryu.weather.dto.WeatherDTO;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class WeatherService {


    //method
    public List<WeatherDTO> getWeatherFromApi(Map<String, Object> param){
        String when = (String) param.get("when");
        String where = (String) param.get("where");
        String stTm = (String) param.get("stTm");
        String edTm = (String) param.get("edTm");
        List<WeatherDTO> list = new ArrayList<>();

        try{
            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/AsosHourlyInfoService/getWthrDataList"); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "="+ Util.weatherServiceKey); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호 Default : 10*/
            urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수 Default : 1*/
            urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*요청자료형식(XML/JSON) Default : XML*/
            urlBuilder.append("&" + URLEncoder.encode("dataCd","UTF-8") + "=" + URLEncoder.encode("ASOS", "UTF-8")); /*자료 분류 코드(ASOS)*/
            urlBuilder.append("&" + URLEncoder.encode("dateCd","UTF-8") + "=" + URLEncoder.encode("HR", "UTF-8")); /*날짜 분류 코드(HR)*/
            urlBuilder.append("&" + URLEncoder.encode("startDt","UTF-8") + "=" + URLEncoder.encode(when, "UTF-8")); /*조회 기간 시작일(YYYYMMDD)*/
            urlBuilder.append("&" + URLEncoder.encode("startHh","UTF-8") + "=" + URLEncoder.encode(stTm, "UTF-8")); /*조회 기간 시작시(HH)*/
            urlBuilder.append("&" + URLEncoder.encode("endDt","UTF-8") + "=" + URLEncoder.encode(when, "UTF-8")); /*조회 기간 종료일(YYYYMMDD) (전일(D-1) 까지 제공)*/
            urlBuilder.append("&" + URLEncoder.encode("endHh","UTF-8") + "=" + URLEncoder.encode(edTm, "UTF-8")); /*조회 기간 종료시(HH)*/
            urlBuilder.append("&" + URLEncoder.encode("stnIds","UTF-8") + "=" + URLEncoder.encode(where, "UTF-8")); /*종관기상관측 지점 번호 (활용가이드 하단 첨부 참조)*/
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
            String str = sb.toString();
            JSONObject obj = new JSONObject(str);
            obj = (JSONObject) obj.get("response");
            obj = (JSONObject) obj.get("body");
            obj = (JSONObject) obj.get("items");
            JSONArray array = (JSONArray) obj.get("item");
            for (int i = 0; i < array.length(); i++) {
                WeatherDTO dto = new WeatherDTO();
                obj = (JSONObject) array.get(i);
                dto.setCoordinate(obj.get("stnId").toString());
                dto.setLocationName(obj.get("stnNm").toString());
                String [] temp = obj.get("tm").toString().split(" ");
                dto.setWeatherDate(temp[0]);
                dto.setWeatherTime(temp[1]);
                dto.setTA(obj.get("ta").toString());
                dto.setWD(obj.get("wd").toString());
                dto.setWS(obj.get("ws").toString());
                dto.setDmstMtphNo(obj.get("dmstMtphNo").toString());
                dto.setRN(obj.get("rn").toString());
                dto.setHM(obj.get("hm").toString());
                dto.setDSNW(obj.get("dsnw").toString());
                list.add(dto);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return list;
    }

}
