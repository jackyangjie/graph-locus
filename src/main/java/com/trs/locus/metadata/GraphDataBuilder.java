package com.trs.locus.metadata;

import com.trs.locus.core.GraphData;
import org.janusgraph.core.JanusGraphTransaction;

/**
 * @Description
 * @DATE 2021.8.12 14:57
 * @Author yangjie
 **/
public interface GraphDataBuilder {

    GraphData builder(JanusGraphTransaction transaction);
}
