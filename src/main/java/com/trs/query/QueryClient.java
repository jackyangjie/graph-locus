package com.trs.query;

import org.apache.tinkerpop.gremlin.driver.Client;
import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.driver.ResultSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description
 * @DATE 2021.8.16 15:26
 * @Author yangjie
 **/

public class QueryClient {
   private static final Logger log = LoggerFactory.getLogger(QueryClient.class);
    private static final QueryClient INSTANCE = new QueryClient();
    private String filename = "graph.yml";
    private static  Client client;

    private synchronized Client getGraphClient(){
        try {
            if (client == null){
                this.getClass().getClassLoader().getResources(filename);
                Cluster cluster = Cluster.open(filename);
                client = cluster.connect();
            }
            return client;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static  final QueryClient  getInstance(){
        return INSTANCE;
    }


    public ResultSet submit(String dsl){
        log.info("query dsl : {}",dsl);
        ResultSet submit = getInstance().getGraphClient().submit(dsl);
        return submit;
    }
}
