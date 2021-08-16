package com.trs.locus.metadata;

import java.util.List;

/**
 * @Description
 * @DATE 2021.8.10 13:52
 * @Author yangjie
 **/
public interface SchemaFactory {

    boolean createLabel();

    boolean createProperty();

    boolean createIndex();

    boolean createEdge();

    boolean updateIndexStatus();

}
