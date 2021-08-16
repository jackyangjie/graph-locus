package com.trs.locus.airplane;

import com.google.common.collect.Lists;
import com.trs.locus.bo.AirBO;
import com.trs.locus.core.GraphData;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Element;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraphTransaction;

import java.util.List;

/**
 * @Description
 * @DATE 2021.8.10 15:25
 * @Author yangjie
 **/

public class AirGraphDataTx implements GraphData<AirBO> {

    private  JanusGraphTransaction janusGraphTransaction;

    private GraphTraversalSource g ;
    public AirGraphDataTx( JanusGraphTransaction janusGraphTransaction) {
        this.janusGraphTransaction = janusGraphTransaction;
        this.g =  janusGraphTransaction.traversal();
    }

    @Override
    public List<Element> createGraphElement(AirBO data) {

        Vertex personV = query(AirSchemaFactory.PERSON_LABEL_V,AirSchemaFactory.SFZH_PROPERTY, data.getSfzh());
        if(personV == null){
            personV = janusGraphTransaction.addVertex(AirSchemaFactory.PERSON_LABEL_V);
            personV.property(AirSchemaFactory.NAME_PROPERTY,data.getName());
            personV.property(AirSchemaFactory.SFZH_PROPERTY,data.getSfzh());
        }

        Vertex airPlanV = query(AirSchemaFactory.AIR_LABEL_V,AirSchemaFactory.FLIGHTNO_PROPERTY, data.getFlightNo());
        if(airPlanV == null){
            airPlanV = janusGraphTransaction.addVertex(AirSchemaFactory.AIR_LABEL_V);
            airPlanV.property(AirSchemaFactory.FLIGHTNO_PROPERTY,data.getFlightNo());
        }

//        JanusGraphVertex cjV = query(AirSchemaFactory.SFZH_PROPERTY, data.getSfzh());
        GraphTraversal<Vertex, Vertex> traversal = g.V().hasLabel(AirSchemaFactory.EVENT_LABEL_V)
                .has(AirSchemaFactory.SFZH_PROPERTY, data.getSfzh())
                .has(AirSchemaFactory.FLIGHTNO_PROPERTY, data.getFlightNo())
                .has(AirSchemaFactory.FLIGHTDAY_PROPERTY, data.getFlightDate()).limit(1);
//        Iterator<JanusGraphVertex> iterator = janusGraphTransaction.query().has(AirSchemaFactory.SFZH_PROPERTY, data.getSfzh())
//                .has(AirSchemaFactory.FLIGHTNO_PROPERTY, data.getFlightNo())
//                .has(AirSchemaFactory.FLIGHTDAY_PROPERTY, data.getFlightDate()).limit(1).vertices().iterator();
        Vertex cjV = null;
        if (traversal.hasNext()) {
            cjV =  traversal.next();
        }else{
            cjV = janusGraphTransaction.addVertex(AirSchemaFactory.EVENT_LABEL_V);
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
        GraphTraversal<Vertex, Vertex> iterator = g.V().hasLabel(label).has(key, value).limit(1).iterate();
        if (iterator.hasNext()){
            return iterator.next();
        }
       return null;
    }


}
