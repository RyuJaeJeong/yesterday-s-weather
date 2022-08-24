package com.ryu.weather.service;

import com.ryu.weather.common.Util;
import com.ryu.weather.dto.ForecastDTO;
import com.ryu.weather.entity.ForecastEntity;
import com.ryu.weather.entity.ForecastId;
import com.ryu.weather.repository.ForecastRepository;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ForecastService {

    @Autowired
    ForecastRepository repository;
    ModelMapper modelMapper = new ModelMapper();

    //일기예보를 입력하는 메서드
    public void insertForecast(ForecastDTO dto){
        modelMapper.typeMap(ForecastDTO.class, ForecastEntity.class).addMappings(mapper ->{     //매핑규칙추가.
            mapper.<String>map(src -> src.getCoordinate(), (destination, value) -> destination.getForecastId().getLocation().setCoordinate(value));
            mapper.<String>map(src -> src.getFcstDate(), (destination, value) -> destination.getForecastId().setFcstDate(value));
            mapper.<String>map(src -> src.getFcstTime(), (destination, value) -> destination.getForecastId().setFcstTime(value));
        });

        ForecastEntity entity = modelMapper.map(dto, ForecastEntity.class);
        repository.save(entity);
    }

    //위치 좌표를 받아서 내일의 시간 별 날씨데이터를 리스트로 리턴해준다.
    //API에서 오늘 예보된 기상예보만 조회 해 올수있음 ==> 오늘날짜로 base_date 고정
    //행이 너무 많음 ==> 6, 9, 12, 15, 18, 21시의 데이터만 받아 오도록 고정
    public List<ForecastDTO> getForecastFromApi(String where) throws IOException {
        List list = new ArrayList();

        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst");
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" + Util.getServiceKey());
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("1000", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode(Util.getSomeDayMore(-1), "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode("0500", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode(where.substring(0, 3), "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode(where.substring(3, 6), "UTF-8"));

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
            JSONObject obj = new JSONObject(str);
            obj = (JSONObject) obj.get("response");
            obj = (JSONObject) obj.get("body");
            obj = (JSONObject) obj.get("items");
            JSONArray array = (JSONArray) obj.get("item");
            forArray : for (int i = 0; i < array.length(); i++){
                ForecastDTO dto = new ForecastDTO();
                ForecastId id = new ForecastId();
                obj = (JSONObject) array.get(i);
                if(obj.get("fcstDate").equals(Util.getSomeDayMore(1))) {    //예보일이 내일 일때
                    forDto : for (int j = i; j<=i+12; j++) {
                        obj = (JSONObject) array.get(j);
                            switch (obj.get("category").toString()) {
                                case "POP" : dto.setPOP(obj.get("fcstValue").toString()); break;
                                case "PTY" : dto.setPTY(obj.get("fcstValue").toString()); break;
                                case "PCP" : dto.setPCP(obj.get("fcstValue").toString()); break;
                                case "REH" : dto.setREH(obj.get("fcstValue").toString()); break;
                                case "SNO" : dto.setSNO(obj.get("fcstValue").toString()); break;
                                case "SKY" : dto.setSKY(obj.get("fcstValue").toString()); break;
                                case "TMP" : dto.setTMP(obj.get("fcstValue").toString()); break;
                                case "TMN" : dto.setTMN(obj.get("fcstValue").toString()); break;
                                case "TMX" : dto.setTMX(obj.get("fcstValue").toString()); break;
                                case "UUU" : dto.setUUU(obj.get("fcstValue").toString()); break;
                                case "VVV" : dto.setVVV(obj.get("fcstValue").toString()); break;
                                case "WAV" : dto.setWAV(obj.get("fcstValue").toString()); break;
                                case "VEC" : dto.setVEC(obj.get("fcstValue").toString()); break;
                                case "WSD" : dto.setWSD(obj.get("fcstValue").toString()); break;
                            }

                        if(obj.get("fcstTime").equals("0600")||obj.get("fcstTime").equals("1500")){

                            if(obj.get("category").equals("TMN")||obj.get("category").equals("TMX")){
                                dto.setCoordinate(where);
                                dto.setFcstDate(obj.get("fcstDate").toString());
                                dto.setFcstTime(obj.get("fcstTime").toString());
                                list.add(dto);
                                i+=12;
                                break forDto;
                            }else {
                                continue forDto;
                            }

                        }else {
                            if(obj.get("category").equals("SNO")){
                                dto.setCoordinate(where);
                                dto.setFcstDate(obj.get("fcstDate").toString());
                                dto.setFcstTime(obj.get("fcstTime").toString());
                                list.add(dto);
                                i+=11;
                                break forDto;
                            }else {
                                continue forDto;
                            }
                        }
                    }   //END forDto

                }

                if(obj.get("fcstDate").equals(Util.getSomeDayMore(2))) {
                    break;
                }

            }   //END forArray


        } catch (JSONException e) {
            log.warn("===== getForecastFromApi failed =====");
            e.printStackTrace();
        }

        return list;
    }

    public List<ForecastDTO> getForecast(String when, String where) throws IOException {
        List<ForecastEntity> entityList = repository.findByForecastId_Location_CoordinateAndForecastIdFcstDate(when, where);
        modelMapper.typeMap(ForecastEntity.class, ForecastDTO.class).addMappings(mapper ->{     //매핑규칙추가.
            mapper.<String>map(src -> src.getForecastId().getLocation().getCoordinate(), (destination, value) -> destination.setCoordinate(value));
            mapper.<String>map(src -> src.getForecastId().getLocation().getName(), (destination, value) -> destination.setLocationName(value));
            mapper.<String>map(src -> src.getForecastId().getFcstDate(), (destination, value) -> destination.setFcstDate(value));
            mapper.<String>map(src -> src.getForecastId().getFcstTime(), (destination, value) -> destination.setFcstTime(value));
        });
        List<ForecastDTO> dtoList = entityList.stream().map(ForecastEntity->modelMapper.map(ForecastEntity, ForecastDTO.class)).collect(Collectors.toList());
        return dtoList;
    }






}
