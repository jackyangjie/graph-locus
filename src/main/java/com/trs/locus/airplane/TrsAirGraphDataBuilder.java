package com.trs.locus.airplane;

import com.trs.locus.core.GraphData;
import com.trs.locus.metadata.GraphDataBuilder;
import org.janusgraph.core.JanusGraphTransaction;

/**
 * @Description
 * @DATE 2021.8.12 15:01
 * @Author yangjie
 **/

public class TrsAirGraphDataBuilder implements GraphDataBuilder {
    @Override
    public GraphData builder(JanusGraphTransaction transaction) {
        return new TrsAirGraphDataTx(transaction);
    }
}
