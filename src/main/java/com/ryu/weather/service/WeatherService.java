package com.ryu.weather.service;

import com.ryu.weather.common.Util;
import com.ryu.weather.dto.WeatherDTO;
import com.ryu.weather.entity.WeatherEntity;
import com.ryu.weather.repository.WeatherRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.juli.logging.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class WeatherService {

    //field
    private final WeatherRepository weatherRepository;
    private final ModelMapper mapperToEntity;
    private final ModelMapper mapperToDTO;

    //cons
    public WeatherService(WeatherRepository weatherRepository, ModelMapper mapperToEntity, ModelMapper mapperToDTO) {
        this.weatherRepository = weatherRepository;

        //매핑 규칙 추가
        mapperToEntity.typeMap(WeatherDTO.class, WeatherEntity.class).addMappings(mapper ->{
            mapper.map(src -> src.getLocationNo(), (destination, value) -> destination.getWeatherId().getLocation().setLocationNo((value==null)?0: (int) value));
            mapper.<String>map(src -> src.getWeatherDate(), (destination, value) -> destination.getWeatherId().setWeatherDate(value));
            mapper.<String>map(src -> src.getWeatherTime(), (destination, value) -> destination.getWeatherId().setWeatherTime(value));
        });

        mapperToDTO.typeMap(WeatherEntity.class, WeatherDTO.class).addMappings(mapper ->{
            mapper.map(src -> src.getWeatherId().getLocation().getLocationNo(), (destination, value) -> destination.setLocationNo((value==null)?0: (int) value));
            mapper.<String>map(src -> src.getWeatherId().getLocation().getName(), (destination, value) -> destination.setLocationName(value));
            mapper.<String>map(src -> src.getWeatherId().getWeatherDate(), (destination, value) -> destination.setWeatherDate(value));
            mapper.<String>map(src -> src.getWeatherId().getWeatherTime(), (destination, value) -> destination.setWeatherTime(value));
        });

        this.mapperToEntity = mapperToEntity;
        this.mapperToDTO = mapperToDTO;
    }


    //method
    /**
     * api에서 일기예보 받아온다.
     * 언제? 어디서? 시간자료 받아올 시작 시간, 끝난 시간(최대 7시간 조회 가능)
     * @return
     */
    public List<WeatherDTO> getWeatherFromApiHourly(String when, String where, String stTm, String edTm) throws IOException {
        List<WeatherDTO> list = new ArrayList<>();

        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/AsosHourlyInfoService/getWthrDataList");    /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "="+ Util.weatherServiceKey);                   /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8"));      /*페이지번호 Default : 10*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("10", "UTF-8"));  /*한 페이지 결과 수 Default : 1*/
        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*요청자료형식(XML/JSON) Default : XML*/
        urlBuilder.append("&" + URLEncoder.encode("dataCd","UTF-8") + "=" + URLEncoder.encode("ASOS", "UTF-8"));   /*자료 분류 코드(ASOS)*/
        urlBuilder.append("&" + URLEncoder.encode("dateCd","UTF-8") + "=" + URLEncoder.encode("HR", "UTF-8"));     /*날짜 분류 코드(HR)*/
        urlBuilder.append("&" + URLEncoder.encode("startDt","UTF-8") + "=" + URLEncoder.encode(when, "UTF-8"));       /*조회 기간 시작일(YYYYMMDD)*/
        urlBuilder.append("&" + URLEncoder.encode("startHh","UTF-8") + "=" + URLEncoder.encode(stTm, "UTF-8"));       /*조회 기간 시작시(HH)*/
        urlBuilder.append("&" + URLEncoder.encode("endDt","UTF-8") + "=" + URLEncoder.encode(when, "UTF-8"));         /*조회 기간 종료일(YYYYMMDD) (전일(D-1) 까지 제공)*/
        urlBuilder.append("&" + URLEncoder.encode("endHh","UTF-8") + "=" + URLEncoder.encode(edTm, "UTF-8"));         /*조회 기간 종료시(HH)*/
        urlBuilder.append("&" + URLEncoder.encode("stnIds","UTF-8") + "=" + URLEncoder.encode(where, "UTF-8"));       /*종관기상관측 지점 번호 (활용가이드 하단 첨부 참조)*/
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        log.info("Response code: " + conn.getResponseCode());
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
        log.info(str);
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
            dto.setWeatherDate(temp[0].replace("-", ""));
            dto.setWeatherTime(temp[1].replace(":", ""));
            dto.setTMP(obj.get("ta").toString());
            dto.setVEC(obj.get("wd").toString());
            dto.setWSD(obj.get("ws").toString());
            dto.setDmstMtphNo(obj.get("dmstMtphNo").toString());
            dto.setPCP(obj.get("rn").toString());
            dto.setREH(obj.get("hm").toString());
            dto.setSNO(obj.get("dsnw").toString());
            list.add(dto);
        }


        return list;
    }

    public WeatherDTO getWeatherFromApiDaily(String when, String where) {
        WeatherDTO dto = new WeatherDTO();
        try{
            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/AsosDalyInfoService/getWthrDataList"); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" + Util.DailyWeatherServiceKey); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호 Default : 1*/
            urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수 Default : 10*/
            urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*요청자료형식(XML/JSON) Default : XML*/
            urlBuilder.append("&" + URLEncoder.encode("dataCd","UTF-8") + "=" + URLEncoder.encode("ASOS", "UTF-8")); /*자료 분류 코드(ASOS)*/
            urlBuilder.append("&" + URLEncoder.encode("dateCd","UTF-8") + "=" + URLEncoder.encode("DAY", "UTF-8")); /*날짜 분류 코드(DAY)*/
            urlBuilder.append("&" + URLEncoder.encode("startDt","UTF-8") + "=" + URLEncoder.encode(when, "UTF-8")); /*조회 기간 시작일(YYYYMMDD)*/
            urlBuilder.append("&" + URLEncoder.encode("endDt","UTF-8") + "=" + URLEncoder.encode(when, "UTF-8")); /*조회 기간 종료일(YYYYMMDD) (전일(D-1)까지 제공)*/
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
                obj = (JSONObject) array.get(i);
                dto.setTMX(obj.get("maxTa").toString());
                dto.setTMN(obj.get("minTa").toString());
            }
        }catch (IOException ioException){
            ioException.printStackTrace();
        }catch (Exception exception){
            exception.printStackTrace();
        }

        return dto;

    }

    /**
     * 기상 시간자료를 DB에 입력하는 메서드
     * @param dto api로 부터 받아온 기상 시간자료 데이터
     */
    public void insertWeather(WeatherDTO dto){
        WeatherEntity entity = mapperToEntity.map(dto, WeatherEntity.class);
        weatherRepository.save(entity);
    }

    /**
     *
     * @param when YYYYmmdd
     * @param where 지역묶음번호
     * @return
     */
    public List<WeatherDTO> getWeather(String when, int where){
        List<WeatherEntity> entityList = weatherRepository.findByWeatherId_Location_WeatherCoordinateAndWeatherIdWeatherDate(when, where);
        List<WeatherDTO> dtoList = entityList.stream().map(weatherEntity->mapperToDTO.map(weatherEntity, WeatherDTO.class)).collect(Collectors.toList());
        return dtoList;
    }

}
