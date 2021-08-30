package com.trs.locus.business.stay.bo;

import com.trs.locus.DateUtil;
import com.trs.locus.common.ErrorData;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * @Description
 * @DATE 2021.8.24 17:18
 * @Author yangjie
 **/

public class StayBO {
    private String name;
    private String zjhm;
    private String rzsj;
    private String rzfh;
    private String tfsj;
    private String lgmc;

    public StayBO() {
    }

    public StayBO(String name, String zjhm, String rzsj, String rzfh, String tfsj, String lgmc) {
        this.name = name;
        this.zjhm = zjhm;
        this.rzsj = rzsj;
        this.rzfh = rzfh;
        this.tfsj = tfsj;
        this.lgmc = lgmc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZjhm() {
        return zjhm;
    }

    public void setZjhm(String zjhm) {
        this.zjhm = zjhm;
    }

    public String getRzsj() {
        return rzsj;
    }

    public void setRzsj(String rzsj) {
        this.rzsj = rzsj;
    }

    public String getRzfh() {
        return rzfh;
    }

    public void setRzfh(String rzfh) {
        this.rzfh = rzfh;
    }

    public String getTfsj() {
        return tfsj;
    }

    public void setTfsj(String tfsj) {
        this.tfsj = tfsj;
    }

    public String getLgmc() {
        return lgmc;
    }

    public void setLgmc(String lgmc) {
        this.lgmc = lgmc;
    }

    public Date getTfsjDate(){
        try {
            return   DateUtil.stringToDate2(this.tfsj);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(this);
        }
        return new Date();
    }
    public Date getRzsjDate(){
        try {
            return  DateUtil.stringToDate2(this.rzsj);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(this);
        }
        return new Date();
    }

    public static StayBO toStay(String line){
        String[] split = line.split(",");
        StayBO stayBO = null;
        try {
             stayBO = new StayBO(split[0], split[1], split[2], split[3], split[4], split[5]);
             if (StringUtils.isBlank(stayBO.getZjhm()) ||StringUtils.isBlank(stayBO.getName())
               ||StringUtils.isBlank(stayBO.getRzfh()) || StringUtils.isBlank(stayBO.getRzsj())||
                     StringUtils.isBlank(stayBO.getTfsj())||StringUtils.isBlank(stayBO.getLgmc())){
                 return null;
             }
        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println(line);
            ErrorData.newInstance().putData(line);
        }
        return  stayBO;
    }

    @Override
    public String toString() {
        return "StayBO{" +
                "name='" + name + '\'' +
                ", zjhm='" + zjhm + '\'' +
                ", rzsj='" + rzsj + '\'' +
                ", rzfh='" + rzfh + '\'' +
                ", tfsj='" + tfsj + '\'' +
                ", lgmc='" + lgmc + '\'' +
                '}';
    }
}
