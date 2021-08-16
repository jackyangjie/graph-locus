package com.trs.query.air;

import com.google.common.collect.Lists;
import com.trs.query.QueryClient;
import org.apache.tinkerpop.gremlin.driver.Client;
import org.apache.tinkerpop.gremlin.driver.Result;
import org.apache.tinkerpop.gremlin.driver.ResultSet;
import org.apache.tinkerpop.gremlin.structure.Property;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.VertexProperty;

import java.util.*;

/**
 * @Description
 * @DATE 2021.8.16 15:30
 * @Author yangjie
 **/

public class AirAccompany {

    private List<String> getAirs(String personId){
        String dsl = "g.V().hasLabel('object.person').has('property.sfzh','"+personId+"').repeat(out()).until(loops().is(2))";
        ResultSet submit = QueryClient.getInstance().submit(dsl);
        Iterator<Result> iterator = submit.iterator();
         List<String> flightNos = Lists.newArrayList();
        while (iterator.hasNext()){
            Result next = iterator.next();
            Vertex vertex = next.getVertex();
            Property<String> property = vertex.property("property.flightNo");
            String flightNo = property.value();
            flightNos.add(flightNo);
        }
        return flightNos;
    }


    private void groupByPerson(List<String> airs){
        String join = String.join("','", airs);
        String dsl = "g.V().hasLabel('object.air').has('property.flightNo',within('"+join+"')).repeat(inE().outV()).until(loops().is(2)).groupCount().by('property.sfzh').unfold()." +
                "filter(select(values).unfold().is(gte(2)))";
        ResultSet submit = QueryClient.getInstance().submit(dsl);
        Iterator<Result> iterator = submit.iterator();
        List<String> flightNos = Lists.newArrayList();
        while (iterator.hasNext()){
            Result next = iterator.next();
            if (next.getObject() instanceof AbstractMap.SimpleEntry){
                AbstractMap.SimpleEntry<String,Long> entry = (AbstractMap.SimpleEntry) next.getObject();
                String key = entry.getKey();
                Long count = entry.getValue();
                System.out.println(key);
            }

        }
    }


    public void computerAirAccompany(String personId){
        List<String> airs = getAirs(personId);
        groupByPerson(airs);
    }

    public static void main(String[] args) {
        AirAccompany airAccompany = new AirAccompany();
        airAccompany.computerAirAccompany("142112198714131312");
    }
}
