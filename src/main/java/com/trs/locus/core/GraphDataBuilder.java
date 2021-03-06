package com.trs.locus.core;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.janusgraph.core.JanusGraph;

import java.util.List;

/**
 * @Description
 * @DATE 2021.8.12 14:57
 * @Author yangjie
 **/
public interface GraphDataBuilder<T> {

    GraphData builder(GraphTraversalSource traversal);

    Runnable builder(Graph graph, List<T> data);
}
