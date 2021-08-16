package com.trs.locus.airplane;

import com.google.common.collect.Lists;
import com.trs.locus.metadata.*;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import java.util.List;

/**
 * @Description
 * @DATE 2021.8.12 14:38
 * @Author yangjie
 **/

public class KgAirSchemaFactory extends AirSchemaFactory {


    public KgAirSchemaFactory(TrsGraphSchemaFactory graphSchemaFactory) {
        super(graphSchemaFactory);
    }

    @Override
    public boolean createProperty() {
        return true;
    }

    @Override
    public boolean createIndex() {
        List<Property> properties = buildProperty();
        List<GraphIndex> graphIndices = buildGraphIndices();
        List<VertexIndex> vertexIndices = buildVertexIndex();
        GraphIndex flightEvent_Graph_index = GraphIndex.newInstance("sfzh_flightno_flightday_index", Vertex.class, Lists.newArrayList(SFZH_PROPERTY,FLIGHTNO_PROPERTY,FLIGHTDAY_PROPERTY), GraphIndex.IndexType.Mixed);
        graphIndices.add(flightEvent_Graph_index);
        graphSchemaFactory.createPropertyAndIndex(properties,graphIndices,vertexIndices);
        return true;
    }

    @Override
    public boolean updateIndexStatus() {
        return true;
    }
}
