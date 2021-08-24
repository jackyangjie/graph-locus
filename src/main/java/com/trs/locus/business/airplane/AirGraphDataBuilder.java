package com.trs.locus.business.airplane;

import com.trs.locus.business.airplane.bo.AirBO;
import com.trs.locus.core.GraphData;
import com.trs.locus.core.GraphDataBuilder;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.janusgraph.core.JanusGraph;

import java.util.List;

/**
 * @Description
 * @DATE 2021.8.16 10:26
 * @Author yangjie
 **/

public class AirGraphDataBuilder implements GraphDataBuilder<AirBO> {
    @Override
    public GraphData builder(GraphTraversalSource traversal) {
        return new AirGraphDataTx(traversal);
    }

    @Override
    public Runnable builder(JanusGraph graph, List<AirBO> data) {
        return new AirGraphDataTask(graph,data);
    }


}
