package com.trs.query.air;

import com.google.common.collect.Lists;
import com.trs.query.QueryClient;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.tinkerpop.gremlin.driver.Result;
import org.apache.tinkerpop.gremlin.driver.ResultSet;
import org.apache.tinkerpop.gremlin.structure.Property;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description
 * @DATE 2021.8.16 15:30
 * @Author yangjie
 **/

public class AirAccompany {

    private List<Pair<String,Date>> getAirs(String personId){
        String dsl = "g.V().hasLabel('object.cj').has('property.sfzh','"+personId+"')";
        ResultSet submit = QueryClient.getInstance().submit(dsl);
        Iterator<Result> iterator = submit.iterator();
         List<Pair<String,Date>> flightNos = Lists.newArrayList();
        while (iterator.hasNext()){
            Result next = iterator.next();
            Vertex vertex = next.getVertex();
            Property<String> flightNo = vertex.property("property.flightNo");
            Property<Date> flightDay = vertex.property("property.flightDay");
            flightNos.add(Pair.of(flightNo.value(),flightDay.value()));
        }
        return flightNos;
    }


    private void groupByPerson( List<Pair<String,Date>>  airs){
        String dsl = buildQueryDsl(airs);
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
                System.out.println("身份证："+key +" , 同行次数："+count);
                c++;
            }

        }
        System.out.println("共有"+c+"个同行记录");
    }


    private String buildQueryDsl( List<Pair<String,Date>>  airs){
        String dsl = "g.V().hasLabel('object.cj').or(%s).groupCount().by('property.sfzh').unfold()." +
                "filter(select(values).unfold().is(gte(2)))";
        String filter = airs.stream()
                .map(p -> "has('property.flightNo','" + p.getKey() + "').has('property.flightDay',new Date(" + p.getValue().getTime() + "))")
//                .map(p -> "and(has('property.flightNo','" + p.getKey() + "').has('property.flightDay',new Date(" + p.getValue().getTime() + ")))")
                .collect(Collectors.joining(","));
        dsl = String.format(dsl,filter);
        return dsl;
    }

    public void computerAirAccompany(String personId){
        List<Pair<String,Date>>  airs = getAirs(personId);
        groupByPerson(airs);
    }

    public static void main(String[] args) {
        AirAccompany airAccompany = new AirAccompany();
        airAccompany.computerAirAccompany("41312919661111211X");
    }
}
