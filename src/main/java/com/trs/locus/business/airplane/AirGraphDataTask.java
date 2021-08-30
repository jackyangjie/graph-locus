package com.trs.locus.business.airplane;

import com.trs.locus.business.airplane.bo.AirBO;
import com.trs.locus.cache.VertexCache;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * @Description
 * @DATE 2021.8.18 9:38
 * @Author yangjie
 **/

public  class AirGraphDataTask implements Runnable{

    private static final Logger log = LoggerFactory.getLogger(AirGraphDataTask.class);
        private GraphTraversalSource g;
        private List<AirBO> datas;
        private Graph threadTx;

        public AirGraphDataTask(Graph threadTx, List<AirBO> datas) {
            this.threadTx = threadTx;
            this.datas = datas;
        }

        @Override
        public void run() {
            try {
                Instant now = Instant.now();
                g = threadTx.traversal();
                for (AirBO data : datas) {
                    Vertex personV = query(AirSchemaFactory.PERSON_LABEL_V, AirSchemaFactory.SFZH_PROPERTY, data.getSfzh());
                    if(personV == null){
//                        personV = newTransaction.addVertex(AirSchemaFactory.PERSON_LABEL_V);
                        personV = g.addV(AirSchemaFactory.PERSON_LABEL_V).next();
                        personV.property(AirSchemaFactory.NAME_PROPERTY,data.getName());
                        personV.property(AirSchemaFactory.SFZH_PROPERTY,data.getSfzh());
                        VertexCache.put(data.getSfzh(),personV);
                    }

                    Vertex airPlanV = query(AirSchemaFactory.AIR_LABEL_V, AirSchemaFactory.FLIGHTNO_PROPERTY, data.getFlightNo());
                    if(airPlanV == null){
                        airPlanV = g.addV(AirSchemaFactory.AIR_LABEL_V).next();
//                        airPlanV =  newTransaction.addVertex(AirSchemaFactory.AIR_LABEL_V);
                        airPlanV.property(AirSchemaFactory.FLIGHTNO_PROPERTY,data.getFlightNo());
                        VertexCache.put(data.getFlightNo(),airPlanV);
                    }

                    Vertex cjV = queryEvent(AirSchemaFactory.EVENT_LABEL_V,data);
                    if (cjV == null) {
                        cjV = g.addV(AirSchemaFactory.EVENT_LABEL_V).next();
//                        cjV =  newTransaction.addVertex(AirSchemaFactory.EVENT_LABEL_V);
                        cjV.property(AirSchemaFactory.SFZH_PROPERTY,data.getSfzh());
                        cjV.property(AirSchemaFactory.FLIGHTNO_PROPERTY,data.getFlightNo());
                        cjV.property(AirSchemaFactory.FLIGHTDAY_PROPERTY,data.getFlightDate());
                    }
                     g.V(personV).as("p").V(cjV).as("e").addE(AirSchemaFactory.SIMPLE_LABEL_E).from("p").next();
                     g.V(cjV).as("e").V(airPlanV).as("a").addE(AirSchemaFactory.SIMPLE_LABEL_E).from("e").next();
//                    Edge edge = personV.addEdge(AirSchemaFactory.SIMPLE_LABEL_E, cjV);
//                    Edge edge1 = cjV.addEdge(AirSchemaFactory.SIMPLE_LABEL_E, airPlanV);
                }

//                g.tx().commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        private Vertex query(String label,String key,String value){
            Vertex vertex = VertexCache.get(value);
            if (vertex == null){
//                Optional<Vertex> optionalVertex = g.V().hasLabel(label).has(key, value).tryNext();
//                if (optionalVertex.isPresent()){
//                    vertex = optionalVertex.get();
//                    VertexCache.put(value,vertex);
//                }
            }
            return vertex;
        }


        private Vertex queryEvent(String label,AirBO data){
            String key = data.getSfzh()+"_"+data.getFlightNo()+"_"+data.getFlightDay();
            Vertex vertex = VertexCache.get(key);
            if (vertex == null){
//                Optional<Vertex> vertexOptional = g.V().hasLabel(label)
//                        .has(AirSchemaFactory.SFZH_PROPERTY, data.getSfzh())
//                        .has(AirSchemaFactory.FLIGHTNO_PROPERTY, data.getFlightNo())
//                        .has(AirSchemaFactory.FLIGHTDAY_PROPERTY, data.getFlightDate()).tryNext();
//                if (vertexOptional.isPresent()){
//                    vertex = vertexOptional.get();
//                    VertexCache.put(key,vertex);
//                }
            }
            return vertex;
        }

}
