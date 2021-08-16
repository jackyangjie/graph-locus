package com.trs.locus.airplane;

import com.trs.locus.core.GraphData;
import com.trs.locus.metadata.GraphDataBuilder;
import org.janusgraph.core.JanusGraphTransaction;

/**
 * @Description
 * @DATE 2021.8.16 10:26
 * @Author yangjie
 **/

public class AirGraphDataBuilder implements GraphDataBuilder {
    @Override
    public GraphData builder(JanusGraphTransaction transaction) {
        return new AirGraphDataTx(transaction);
    }
}
