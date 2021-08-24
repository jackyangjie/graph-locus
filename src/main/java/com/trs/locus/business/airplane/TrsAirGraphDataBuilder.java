package com.trs.locus.business.airplane;

import com.trs.locus.business.airplane.bo.AirBO;
import com.trs.locus.core.GraphData;
import com.trs.locus.core.GraphDataBuilder;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.janusgraph.core.JanusGraph;

import java.util.List;

/**
 * @Description
 * @DATE 2021.8.12 15:01
 * @Author yangjie
 **/

public class TrsAirGraphDataBuilder implements GraphDataBuilder<AirBO> {
    @Override
    public GraphData builder(GraphTraversalSource traversal) {
        return new TrsAirGraphDataTx(traversal);
    }

    @Override
    public Runnable builder(JanusGraph graph, List<AirBO> data) {
        return null;
    }

}
