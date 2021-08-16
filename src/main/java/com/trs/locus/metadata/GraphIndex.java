package com.trs.locus.metadata;

import org.apache.tinkerpop.gremlin.structure.Element;

import java.util.List;

/**
 * @Description
 * @DATE 2021.8.10 14:40
 * @Author yangjie
 **/

public class GraphIndex {

    private String indexName;
    private  Class<? extends Element> indexClass;
    private List<String> key;
    private IndexType type;


    public GraphIndex(String indexName, Class<? extends Element> indexClass, List<String> key, IndexType type) {
        this.indexName = indexName;
        this.indexClass = indexClass;
        this.key = key;
        this.type = type;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public  Class<? extends Element> getIndexClass() {
        return indexClass;
    }

    public void setIndexClass( Class<? extends Element> indexClass) {
        this.indexClass = indexClass;
    }

    public List<String> getKey() {
        return key;
    }

    public void setKey(List<String> key) {
        this.key = key;
    }

    public IndexType getType() {
        return type;
    }

    public void setType(IndexType type) {
        this.type = type;
    }

    public   enum IndexType{
        Composite,Mixed;
    }

    public static GraphIndex newInstance(String name, Class<? extends Element> indexClass, List<String> key, IndexType type){
        return new GraphIndex(name,indexClass,key,type);
    }

}
