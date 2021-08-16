package com.trs.locus.metadata;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.janusgraph.core.EdgeLabel;
import org.janusgraph.core.PropertyKey;
import org.janusgraph.core.VertexLabel;
import org.janusgraph.core.schema.*;
import org.janusgraph.graphdb.database.management.GraphIndexStatusWatcher;
import org.janusgraph.graphdb.database.management.ManagementSystem;

import java.util.List;
import java.util.logging.Logger;

import static org.janusgraph.core.Multiplicity.MULTI;

/**
 * @Description
 * @DATE 2021.8.3 14:28
 * @Author yangjie
 **/

public class TrsGraphSchemaFactory {

    private String graphConfig;

    public TrsGraphSchemaFactory(String graphConfig) {
        this.graphConfig = graphConfig;
        TrsGraphFactory.getGraphInstance(graphConfig);
    }

    public  final void createVertexLabel(List<String> labelList){
        JanusGraphManagement management = TrsGraphFactory.getGraphInstance().openManagement();
        for (String label : labelList) {
            VertexLabel vertexLabel = management.getVertexLabel(label);
            if (vertexLabel == null){
                management.makeVertexLabel(label).make();
            }
        }
        management.commit();
    }


    public  final void createEdgeLabel(List<String> labelList){
        JanusGraphManagement management = TrsGraphFactory.getGraphInstance().openManagement();
        for (String label : labelList) {
            EdgeLabel edgeLabel = management.getEdgeLabel(label);
            if (edgeLabel == null){
                edgeLabel = management.makeEdgeLabel(label).multiplicity(MULTI).make();
            }
        }
        management.commit();
    }


    public  final void createProperty(List<Property> propList){
        JanusGraphManagement management = TrsGraphFactory.getGraphInstance().openManagement();
        doCreateProperty(management,propList);
        management.commit();
    }

    private final void doCreateProperty(JanusGraphManagement management,List<Property> propList){
        for (Property prop : propList) {
            PropertyKey propertyKey = management.getPropertyKey(prop.getName());
            if (propertyKey == null){
                management.makePropertyKey(prop.getName()).dataType(prop.getType()).cardinality(prop.getCardinality()).make();
            }
        }
    }

    public  final void createVertexIndex( List<GraphIndex> indices){
        JanusGraphManagement management = TrsGraphFactory.getGraphInstance().openManagement();
        doCreateVertexIndex(management,indices);
        management.commit();
    }

    private void doCreateVertexIndex(JanusGraphManagement management,List<GraphIndex> indices){
        for (GraphIndex graphIndex : indices) {
            JanusGraphIndex janusGraphIndex = management.getGraphIndex(graphIndex.getIndexName());
            if (janusGraphIndex == null){
                JanusGraphManagement.IndexBuilder indexBuilder = management.buildIndex(graphIndex.getIndexName(), graphIndex.getIndexClass());
                for (String key : graphIndex.getKey()) {
                    PropertyKey propertyKey = management.getPropertyKey(key);
                    indexBuilder.addKey(propertyKey);
                }
                if (GraphIndex.IndexType.Composite == graphIndex.getType()){
                    janusGraphIndex =  indexBuilder.buildCompositeIndex();
                }else{
//                    janusGraphIndex =  indexBuilder.buildKGMixedIndex("search");
                    janusGraphIndex =  indexBuilder.buildMixedIndex("search");
                }
            }
        }

    }


    public final void createPropertyAndIndex(List<Property> propList, List<GraphIndex> vertexIndexs,List<VertexIndex> indices){
        JanusGraphManagement management = TrsGraphFactory.getGraphInstance().openManagement();
          doCreateProperty(management,propList);
          doCreateVertexIndex(management,vertexIndexs);
          doCreateEdgeIndex(management,indices);
        management.commit();
    }

    public  final void createEdgeIndex(List<VertexIndex> indices){
        JanusGraphManagement management = TrsGraphFactory.getGraphInstance().openManagement();
        doCreateEdgeIndex(management,indices);
        management.commit();
    }

    private final void doCreateEdgeIndex(JanusGraphManagement management,List<VertexIndex> indices){
        for (VertexIndex index : indices) {
            EdgeLabel edgeLabel = management.getEdgeLabel(index.getLabel());
            RelationTypeIndex relationIndex = management.getRelationIndex(edgeLabel, index.getName());
            if (relationIndex == null){
                PropertyKey propertyKey = management.getPropertyKey(index.getKey());
                management.buildEdgeIndex(edgeLabel,index.getName(),index.getDirection(),index.getOrder(),propertyKey);
            }
        }
    }


    public final void updateIndexStatus(List<Triple<String,String,String>> triples){
        JanusGraphManagement management = TrsGraphFactory.getGraphInstance().openManagement();
        List<Pair<String,JanusGraphManagement.IndexJobFuture>> registerList = Lists.newArrayList();
        for (Triple<String,String,String> triple : triples) {
            JanusGraphManagement.IndexJobFuture indexJobFuture = null;
                if ("graph".equals(triple.getMiddle())){
                     indexJobFuture = management.updateIndex(management.getGraphIndex(triple.getLeft()), SchemaAction.REGISTER_INDEX);

                }else{ EdgeLabel edgeLabel = management.getEdgeLabel(triple.getRight());
                    indexJobFuture = management.updateIndex(management.getRelationIndex(edgeLabel, triple.getLeft()), SchemaAction.REGISTER_INDEX);
                }
                registerList.add(Pair.of(triple.getLeft(),indexJobFuture));
        }
        management.commit();

        for (Pair<String,JanusGraphManagement.IndexJobFuture> pair : registerList) {
            if (pair.getRight() != null){
                try {
                    pair.getRight().get();
                    GraphIndexStatusWatcher status = ManagementSystem.awaitGraphIndexStatus(TrsGraphFactory.getGraphInstance(), pair.getLeft()).status(SchemaStatus.REGISTERED);
                    if (status!= null){
                        status.call();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


        JanusGraphManagement mgmt = TrsGraphFactory.getGraphInstance().openManagement();
        Logger.getGlobal().info(mgmt.printSchema());
        Logger.getGlobal().info(mgmt.getOpenInstances().toString());
        for (Triple<String,String,String> triple : triples) {
                if ("graph".equals(triple.getMiddle())){
                    mgmt.updateIndex(mgmt.getGraphIndex(triple.getLeft()), SchemaAction.ENABLE_INDEX);
                }else{
                    EdgeLabel edgeLabel = mgmt.getEdgeLabel(triple.getRight());
                    mgmt.updateIndex(mgmt.getRelationIndex(edgeLabel, triple.getLeft()), SchemaAction.ENABLE_INDEX);
                }
        }
        Logger.getGlobal().info(mgmt.printSchema());
        Logger.getGlobal().info(mgmt.getOpenInstances().toString());
        mgmt.commit();

    }
}
