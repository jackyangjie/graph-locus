package com.trs.locus.airplane;

import com.google.common.collect.Lists;
import com.trs.locus.bo.AirBO;
import com.trs.locus.core.GraphData;
import org.apache.commons.lang3.StringUtils;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Element;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraphEdge;
import org.janusgraph.core.JanusGraphTransaction;
import org.janusgraph.core.JanusGraphVertex;
import org.janusgraph.graphdb.idmanagement.IDManager;
import org.janusgraph.graphdb.transaction.StandardJanusGraphTx;

import java.util.List;

/**
 * @Description
 * @DATE 2021.8.10 15:25
 * @Author yangjie
 **/

public class TrsAirGraphDataTx implements GraphData<AirBO> {

    private  JanusGraphTransaction janusGraphTransaction;
    private GraphTraversalSource traversal;

    public TrsAirGraphDataTx(JanusGraphTransaction janusGraphTransaction) {
        this.janusGraphTransaction = janusGraphTransaction;
        this.traversal = janusGraphTransaction.traversal();
    }

    @Override
    public List<Element> createGraphElement(AirBO data) {
        if (StringUtils.isBlank(data.getFlightNo()) || StringUtils.isBlank(data.getFlightDay())){
            return Lists.newArrayList();
        }

        StandardJanusGraphTx janusGraphTx = (StandardJanusGraphTx) this.janusGraphTransaction;
        IDManager idManager = janusGraphTx.getGraph().getIDManager();
//        String sfz = idManager.fromVertexId(data.getSfzh());
//        String flightNo = idManager.fromVertexId(data.getFlightNo());

        Vertex personV = traversal.addV(AirSchemaFactory.PERSON_LABEL_V)
                .property(T.id, data.getSfzh())
                .property(AirSchemaFactory.NAME_PROPERTY, data.getName())
                .property(AirSchemaFactory.SFZH_PROPERTY, data.getSfzh()).next();

        Vertex airPlanV = traversal.addV(AirSchemaFactory.AIR_LABEL_V)
                .property(AirSchemaFactory.FLIGHTNO_PROPERTY, data.getFlightNo())
                .property(T.id, data.getFlightNo()).next();

        String id = data.getSfzh()+"-"+data.getFlightNo()+"-"+data.getFlightDay();
//        String eventId = idManager.fromVertexId(id);
        Vertex  cjV = traversal.addV(AirSchemaFactory.EVENT_LABEL_V)
                .property(AirSchemaFactory.SFZH_PROPERTY,data.getSfzh())
                .property(AirSchemaFactory.FLIGHTNO_PROPERTY,data.getFlightNo())
                .property(AirSchemaFactory.FLIGHTDAY_PROPERTY,data.getFlightDate())
                .property(T.id,id).next();

        Edge pEv = traversal.V(personV).V(cjV).addE(AirSchemaFactory.SIMPLE_LABEL_E).from(personV).next();
        Edge cEa = traversal.V(cjV).V(airPlanV).addE(AirSchemaFactory.SIMPLE_LABEL_E).from(cjV).next();
        List<Element> elements = Lists.newArrayList(personV, airPlanV, cjV, pEv, cEa);
        return elements;
    }

}
