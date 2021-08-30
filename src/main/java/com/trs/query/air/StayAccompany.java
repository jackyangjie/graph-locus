package com.trs.query.air;

import com.google.common.collect.Lists;
import com.trs.locus.business.stay.bo.StayBO;
import com.trs.query.QueryClient;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.tinkerpop.gremlin.driver.Result;
import org.apache.tinkerpop.gremlin.driver.ResultSet;
import org.apache.tinkerpop.gremlin.structure.Property;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import java.util.AbstractMap;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description
 * @DATE 2021.8.30 9:02
 * @Author yangjie
 **/

public class StayAccompany {


    private final static long  MIN_15 = 15 * 60 * 1000;

    private List<StayBO> getStays(String personId){
        String dsl = "s.V().hasLabel('object.stay').has('property.sfzh','"+personId+"')";
        ResultSet submit = QueryClient.getInstance().submit(dsl);
        Iterator<Result> iterator = submit.iterator();
        List<StayBO> stays = Lists.newArrayList();
        while (iterator.hasNext()){
            Result next = iterator.next();
            Vertex vertex = next.getVertex();
            Property<String> hotel = vertex.property("property.hotel");
            Property<String> room = vertex.property("property.room");
            Property<Date> kfsj = vertex.property("property.kfsj");
            Property<Date> tfsj = vertex.property("property.tfsj");
            StayBO stay = new StayBO();
            stay.setLgmc(hotel.value());
            stay.setRzfh(room.value());
            stay.setRzsj(kfsj.value().getTime()+"");
            stay.setTfsj(tfsj.value().getTime()+"");
            stays.add(stay);
        }
        return stays;
    }


    private void groupByPerson( List<StayBO>  stays,String dsl){

        ResultSet submit = QueryClient.getInstance().submit(dsl);
        Iterator<Result> iterator = submit.iterator();
        List<String> flightNos = Lists.newArrayList();
        int c = 0 ;
        while (iterator.hasNext()){
            Result next = iterator.next();
            if (next.getObject() instanceof AbstractMap.SimpleEntry){
                AbstractMap.SimpleEntry<String,Long> entry = (AbstractMap.SimpleEntry) next.getObject();
                String key = entry.getKey();
                Long count = entry.getValue();
                System.out.println("身份证："+key +" , 同住次数："+count);
                c++;
            }

        }
        System.out.println("共有"+c+"个同行记录");
    }


    private String buildSameRoomDsl( List<StayBO>  stays){
        String dsl = "s.V().hasLabel('object.stay').or(%s)";
        String filter = stays.stream()
                .map(p -> "has('property.hotel','" + p.getLgmc() + "')" +
                          ".has('property.room','"+p.getRzfh()+"')"+
                          ".has('property.kfsj',new Date(" + p.getRzsj() + "))"+
                          ".has('property.tfsj',new Date(" + p.getTfsj() + "))"
                  )
                .collect(Collectors.joining(","));
        dsl = String.format(dsl,filter);
        return dsl;
    }

    private String buildNotSameRoomDsl( List<StayBO>  stays){
        String dsl = "s.V().hasLabel('object.stay').or(%s).groupCount().by('property.sfzh').unfold()." +
                "filter(select(values).unfold().is(gte(2)))";
        String filter = stays.stream()
                .map(p -> "has('property.hotel','" + p.getLgmc() + "')" +
//                        ".has('property.room','"+p.getRzfh()+"')"+
                        ".has('property.kfsj',"+betweenTime(p.getRzsj())+")"+
                        ".has('property.tfsj',"+betweenTime(p.getTfsj())+")"
                )
                .collect(Collectors.joining(","));
        dsl = String.format(dsl,filter);

        return dsl;
    }

    private String  betweenTime(String dateLong){
        long time = Long.parseLong(dateLong);

        return "between(new Date("+(time-MIN_15) +"),new Date("+(time + MIN_15)+"))";
    }

    public void computerStayAccompany(String personId){
        List<StayBO>  stays = getStays(personId);

        String dsl = buildSameRoomDsl(stays);

        groupByPerson(stays,dsl);

        String dsl2 = buildNotSameRoomDsl(stays);

        groupByPerson(stays,dsl2);
    }

    public static void main(String[] args) {
        StayAccompany stayAccompany = new StayAccompany();
        stayAccompany.computerStayAccompany("511028197212165515");
    }
}
