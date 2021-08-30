package com.trs.locus.core;

import com.trs.locus.loaddata.ReadData;
import com.trs.locus.metadata.SchemaFactory;
import com.trs.locus.metadata.TrsGraphFactory;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Transaction;
import org.janusgraph.core.JanusGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;

/**
 * @Description
 * @DATE 2021.8.10 14:06
 * @Author yangjie
 **/

public class TaskProcess {

    private static final Logger log = LoggerFactory.getLogger(TaskProcess.class);

    protected SchemaFactory schemaFactory;
    protected ReadData<?> readData;

    protected final int TX_BATCH_SIZE = 2000;

    protected GraphDataBuilder graphDataBuilder;

    public TaskProcess(SchemaFactory schemaFactory, GraphDataBuilder graphDataBuilder, ReadData<?> readData) {
        this.schemaFactory = schemaFactory;
        this.readData = readData;
        this.graphDataBuilder = graphDataBuilder;
    }

    public void process(){
        createSchema();
        importData();
    }

    private void createSchema(){

        schemaFactory.createLabel();
        schemaFactory.createProperty();
        schemaFactory.createEdge();
        schemaFactory.createIndex();
        schemaFactory.updateIndexStatus();
    }

    protected void importData(){
        JanusGraph graphInstance = TrsGraphFactory.getGraphInstance();
        GraphTraversalSource traversal = graphInstance.traversal();
        GraphData graphData =  graphDataBuilder.builder(traversal);

        long count = 0;
        Instant start = Instant.now();
        while (readData.hasNext()){
            Object data = readData.next();
            count ++;
            try {
                if (data == null) {continue;}
                graphData.createGraphElement(data);
            } catch (Exception e) {
                log.info("当前错误的数据:{}",data);
                e.printStackTrace();
            }
            if (count % TX_BATCH_SIZE == 0){
                 traversal.tx().commit();
                Instant end = Instant.now();
                log.info("当前已经写入的数据量："+count +" ，用时:"+ Duration.between(start,end).toMillis() +"毫秒");
                start = end;
                traversal = graphInstance.traversal();
                graphData =  graphDataBuilder.builder(traversal);
            }
        }
        if (graphData != null ){
            traversal.tx().commit();
            log.info("当前已经写入的数据量："+count +" ，用时:"+ Duration.between(start,Instant.now()).toMillis() +"毫秒");
        }
    }




}
