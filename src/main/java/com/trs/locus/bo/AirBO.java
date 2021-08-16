package com.trs.locus.bo;

import com.trs.locus.DateUtil;

import java.time.LocalDate;
import java.util.Date;

/**
 * @Description
 * @DATE 2021.8.10 11:19
 * @Author yangjie
 **/

public class AirBO {
    private String id;
    private String name;
    private String sfzh;
    private String flightNo;
    private String flightDay;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSfzh() {
        return sfzh;
    }

    public void setSfzh(String sfzh) {
        this.sfzh = sfzh;
    }

    public String getFlightNo() {
        return flightNo;
    }

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    public String getFlightDay() {
        return flightDay;
    }

    public void setFlightDay(String flightDay) {
        this.flightDay = flightDay;
    }


    public Date getFlightDate(){

        try {
          return   DateUtil.stringToDate(this.flightDay);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(this);
        }
        return new Date();
    }

    public static AirBO stringToAirBO(String json){
        String[] split = json.split(",");
        AirBO airBO = new AirBO();
        airBO.setId(split[0]);
        airBO.setName(split[1]);
        airBO.setSfzh(split[2]);
        airBO.setFlightNo(split[3]);
        airBO.setFlightDay(split[4]);
        return airBO;
    }

    @Override
    public String toString() {
        return "AirBO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", sfzh='" + sfzh + '\'' +
                ", flightNo='" + flightNo + '\'' +
                ", flightDay='" + flightDay + '\'' +
                '}';
    }
}
