package com.ryu.weather.service;

import com.ryu.weather.common.Util;
import com.ryu.weather.dto.ForecastDTO;
import com.ryu.weather.entity.ForecastEntity;
import com.ryu.weather.entity.ForecastID;
import com.ryu.weather.repository.ForecastRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
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


    //field
    private final ForecastRepository forecastRepository;
    private final ModelMapper mapperToEntity;
    private final ModelMapper mapperToDTO;

    //cons
    public ForecastService(ForecastRepository forecastRepository, ModelMapper mapperToEntity, ModelMapper mapperToDTO) {
        this.forecastRepository = forecastRepository;

        //매핑 규칙 추가
        mapperToEntity.typeMap(ForecastDTO.class, ForecastEntity.class).addMappings(mapper ->{
            mapper.map(src -> src.getLocationNo(), (destination, value) -> destination.getForecastId().getLocation().setLocationNo((value==null)?0: (int) value));
            mapper.<String>map(src -> src.getFcstDate(), (destination, value) -> destination.getForecastId().setFcstDate(value));
            mapper.<String>map(src -> src.getFcstTime(), (destination, value) -> destination.getForecastId().setFcstTime(value));
        });

        mapperToDTO.typeMap(ForecastEntity.class, ForecastDTO.class).addMappings(mapper ->{     //매핑규칙추가.
            mapper.map(src -> src.getForecastId().getLocation().getLocationNo(), (destination, value) -> destination.setLocationNo((value==null)?0: (int) value));
            mapper.<String>map(src -> src.getForecastId().getLocation().getName(), (destination, value) -> destination.setLocationName(value));
            mapper.<String>map(src -> src.getForecastId().getFcstDate(), (destination, value) -> destination.setFcstDate(value));
            mapper.<String>map(src -> src.getForecastId().getFcstTime(), (destination, value) -> destination.setFcstTime(value));
        });

        this.mapperToEntity = mapperToEntity;
        this.mapperToDTO = mapperToDTO;
    }

    /**
     * 일기예보를 DB에 입력하는 메서드
     * @param dto api로 부터 받아온 일기예보 데이터
     */
    public void insertForecast(ForecastDTO dto){
        try {
            ForecastEntity entity = mapperToEntity.map(dto, ForecastEntity.class);
            forecastRepository.save(entity);
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    /**
     * 위치 좌표를 받아서 api로 부터 내일의 시간 별 날씨데이터를 리스트로 리턴해준다.
     * @param where 날씨데이터를 조회 할 위치 좌표
     * @return 시간별 날씨 데이터 리턴 값
     */
    public List<ForecastDTO> getForecastFromApi(String where) {
        List list = new ArrayList();

        try {
              StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst");
              urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" + Util.forecastServiceKey);
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
              forArray : for (int i = 0; i < array.length(); i++){
                  ForecastDTO dto = new ForecastDTO();
                  ForecastID id = new ForecastID();
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
                                  case "VEC" : dto.setVEC(obj.get("fcstValue").toString()); break;
                                  case "WSD" : dto.setWSD(obj.get("fcstValue").toString()); break;
                                  case "UUU" :
                                  case "VVV" :
                                  case "WAV" :
                                                break;

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
        }catch (Exception e){
            log.warn("===== getForecastFromApi failed =====");
            e.printStackTrace();
        }
        return list;
    }

    /**
     * DB에서 ~
     * @param when 위치 좌표
     * @param where 언제
     * @return
     */
    public List<ForecastDTO> getForecast(String when, String where) {
        List<ForecastDTO> dtoList = new ArrayList<>();
        try{
            List<ForecastEntity> entityList = forecastRepository.findByForecastId_Location_CoordinateAndForecastIdFcstDate(when, where);
            dtoList = entityList.stream().map(ForecastEntity->mapperToDTO.map(ForecastEntity, ForecastDTO.class)).collect(Collectors.toList());
        }catch (Exception e){
            e.printStackTrace();
        }
        return dtoList;
    }






}
