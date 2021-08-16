package com.trs.query;

import org.apache.tinkerpop.gremlin.driver.Client;
import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.driver.ResultSet;

/**
 * @Description
 * @DATE 2021.8.16 15:26
 * @Author yangjie
 **/

public class QueryClient {

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
        ResultSet submit = getInstance().getGraphClient().submit(dsl);
        return submit;
    }
}
