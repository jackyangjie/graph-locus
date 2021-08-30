package com.trs.locus.business.stay;

import com.google.common.collect.Lists;
import com.trs.locus.business.airplane.AirSchemaFactory;
import com.trs.locus.business.stay.bo.StayBO;
import com.trs.locus.cache.VertexCache;
import com.trs.locus.core.GraphData;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Element;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import java.util.List;
import java.util.Optional;

/**
 * @Description
 * @DATE 2021.8.24 17:28
 * @Author yangjie
 **/

public class StayGraphDataTx implements GraphData<StayBO> {
    private GraphTraversalSource g ;
    public StayGraphDataTx( GraphTraversalSource traversal) {
        this.g =  traversal;
    }

    @Override
    public List<Element> createGraphElement(StayBO data) {
        Vertex personV = query(StaySchemaFactory.PERSON_LABEL_V, StaySchemaFactory.SFZH_PROPERTY, data.getZjhm());
        if(personV == null){
            personV = g.addV(StaySchemaFactory.PERSON_LABEL_V).next();
            personV.property(StaySchemaFactory.NAME_PROPERTY,data.getName());
            personV.property(StaySchemaFactory.SFZH_PROPERTY,data.getZjhm());
            VertexCache.put(data.getZjhm(),personV);
        }

        Vertex hotel = query(StaySchemaFactory.HOTEL_LABEL_V, StaySchemaFactory.HOTEL_NAME_PROPERTY, data.getLgmc());
        if(hotel == null){
            hotel = g.addV(StaySchemaFactory.HOTEL_LABEL_V).next();
            hotel.property(StaySchemaFactory.HOTEL_NAME_PROPERTY,data.getLgmc());
            VertexCache.put(data.getLgmc(),hotel);
        }

        Vertex stay = queryEvent(StaySchemaFactory.EVENT_LABEL_V,data);
        if (stay == null) {
            stay = g.addV(StaySchemaFactory.EVENT_LABEL_V).next();
            stay.property(StaySchemaFactory.SFZH_PROPERTY,data.getZjhm());
            stay.property(StaySchemaFactory.HOTEL_NAME_PROPERTY,data.getLgmc());
            stay.property(StaySchemaFactory.HOTEL_ROOM_PROPERTY,data.getRzfh());
            stay.property(StaySchemaFactory.KFSJ_PROPERTY,data.getRzsjDate());
            stay.property(StaySchemaFactory.TFSJ_PROPERTY,data.getTfsjDate());
        }
        Edge pEv = personV.addEdge(StaySchemaFactory.SIMPLE_LABEL_E, stay);
        Edge cEa = stay.addEdge(StaySchemaFactory.SIMPLE_LABEL_E, hotel);
//        JanusGraphEdge cEa = cjV.addEdge(AirSchemaFactory.SIMPLE_LABEL_E, airPlanV);
        List<Element> elements = Lists.newArrayList(personV, hotel, stay, pEv, cEa);
        return elements;
    }

    private Vertex query(String label,String key,String value){
        Vertex vertex = VertexCache.get(value);
//        if (vertex == null){
//            Optional<Vertex> optionalVertex = g.V().hasLabel(label).has(key, value).tryNext();
//            if (optionalVertex.isPresent()){
//                vertex = optionalVertex.get();
//                VertexCache.put(value,vertex);
//            }
//        }

        return vertex;
    }


    private Vertex queryEvent(String label,StayBO data){
//        String key = data.getZjhm()+"_"+data.getLgmc()+"_"+data.getRzfh();
//        Vertex vertex = VertexCache.get(key);
//        if (vertex == null){
//            Optional<Vertex> vertexOptional = g.V().hasLabel(label)
//                    .has(AirSchemaFactory.SFZH_PROPERTY, data.getSfzh())
//                    .has(AirSchemaFactory.FLIGHTNO_PROPERTY, data.getFlightNo())
//                    .has(AirSchemaFactory.FLIGHTDAY_PROPERTY, data.getFlightDate()).tryNext();
//            if (vertexOptional.isPresent()){
//                vertex = vertexOptional.get();
//                VertexCache.put(key,vertex);
//            }
//        }
//        return vertex;
        return null;
    }

}
