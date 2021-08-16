package com.trs.locus.metadata;

import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;

/**
 * @Description
 * @DATE 2021.8.3 14:21
 * @Author yangjie
 **/

public class TrsGraphFactory {


    private static JanusGraph graph;

    public static final JanusGraph getGraphInstance(String path){
        if (graph == null){
             graph = JanusGraphFactory.open(path);
        }
        return graph;
    }

    public static final JanusGraph getNewGraphInstance(){
      if (graph!= null){
          return  JanusGraphFactory.open(graph.configuration());
      }
        return null;
    }

    public static JanusGraph getGraphInstance() {
        return graph;
    }
}
