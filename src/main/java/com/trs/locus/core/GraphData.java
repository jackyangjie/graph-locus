package com.trs.locus.core;

import org.apache.tinkerpop.gremlin.structure.Element;

import java.util.List;

/**
 * @Description
 * @DATE 2021.8.10 15:14
 * @Author yangjie
 **/

public interface GraphData<T> {

    List<Element> createGraphElement(T data);


}
