package com.trs.locus.business.airplane;

import com.google.common.collect.Lists;
import com.trs.locus.business.airplane.bo.AirBO;
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
 * @DATE 2021.8.10 15:25
 * @Author yangjie
 **/

public class AirGraphDataTx implements GraphData<AirBO> {

//    private  JanusGraphTransaction janusGraphTransaction;

    private GraphTraversalSource g ;
    public AirGraphDataTx( GraphTraversalSource traversal) {
        this.g =  traversal;
    }

    @Override
    public List<Element> createGraphElement(AirBO data) {

        Vertex personV = query(AirSchemaFactory.PERSON_LABEL_V, AirSchemaFactory.SFZH_PROPERTY, data.getSfzh());
        if(personV == null){
            personV = g.addV(AirSchemaFactory.PERSON_LABEL_V).next();
            personV.property(AirSchemaFactory.NAME_PROPERTY,data.getName());
            personV.property(AirSchemaFactory.SFZH_PROPERTY,data.getSfzh());
            VertexCache.put(data.getSfzh(),personV);
        }

        Vertex airPlanV = query(AirSchemaFactory.AIR_LABEL_V, AirSchemaFactory.FLIGHTNO_PROPERTY, data.getFlightNo());
        if(airPlanV == null){
            airPlanV = g.addV(AirSchemaFactory.AIR_LABEL_V).next();
            airPlanV.property(AirSchemaFactory.FLIGHTNO_PROPERTY,data.getFlightNo());
            VertexCache.put(data.getFlightNo(),airPlanV);
        }

        Vertex cjV = queryEvent(AirSchemaFactory.EVENT_LABEL_V,data);
        if (cjV == null) {
            cjV = g.addV(AirSchemaFactory.EVENT_LABEL_V).next();
            cjV.property(AirSchemaFactory.SFZH_PROPERTY,data.getSfzh());
            cjV.property(AirSchemaFactory.FLIGHTNO_PROPERTY,data.getFlightNo());
            cjV.property(AirSchemaFactory.FLIGHTDAY_PROPERTY,data.getFlightDate());
        }
        Edge pEv = personV.addEdge(AirSchemaFactory.SIMPLE_LABEL_E, cjV);
        Edge cEa = cjV.addEdge(AirSchemaFactory.SIMPLE_LABEL_E, airPlanV);
//        JanusGraphEdge cEa = cjV.addEdge(AirSchemaFactory.SIMPLE_LABEL_E, airPlanV);
        List<Element> elements = Lists.newArrayList(personV, airPlanV, cjV, pEv, cEa);
        return elements;
    }

    private Vertex query(String label,String key,String value){
        Vertex vertex = VertexCache.get(value);
        if (vertex == null){
//            Optional<Vertex> optionalVertex = g.V().hasLabel(label).has(key, value).tryNext();
//            if (optionalVertex.isPresent()){
//                vertex = optionalVertex.get();
//                VertexCache.put(value,vertex);
//            }
        }

       return vertex;
    }


    private Vertex queryEvent(String label,AirBO data){
        String key = data.getSfzh()+"_"+data.getFlightNo()+"_"+data.getFlightDay();
        Vertex vertex = VertexCache.get(key);
        if (vertex == null){
//            Optional<Vertex> vertexOptional = g.V().hasLabel(label)
//                .has(AirSchemaFactory.SFZH_PROPERTY, data.getSfzh())
//                .has(AirSchemaFactory.FLIGHTNO_PROPERTY, data.getFlightNo())
//                .has(AirSchemaFactory.FLIGHTDAY_PROPERTY, data.getFlightDate()).tryNext();
//            if (vertexOptional.isPresent()){
//                vertex = vertexOptional.get();
//                VertexCache.put(key,vertex);
//            }
        }
        return vertex;
    }

}
