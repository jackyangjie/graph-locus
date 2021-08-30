package com.trs.locus.business.stay;

import com.trs.locus.business.stay.bo.StayBO;
import com.trs.locus.core.GraphData;
import com.trs.locus.core.GraphDataBuilder;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.janusgraph.core.JanusGraph;

import java.util.List;

/**
 * @Description
 * @DATE 2021.8.24 17:28
 * @Author yangjie
 **/

public class StayGraphDataBuilder implements GraphDataBuilder<StayBO> {
    @Override
    public GraphData builder(GraphTraversalSource traversal) {
        return new StayGraphDataTx(traversal);
    }

    @Override
    public Runnable builder(Graph graph, List<StayBO> data) {
        return null;
    }
}
