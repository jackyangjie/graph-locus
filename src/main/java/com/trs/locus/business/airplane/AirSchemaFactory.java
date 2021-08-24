package com.trs.locus.business.airplane;

import com.google.common.collect.Lists;
import com.trs.locus.metadata.*;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.tinkerpop.gremlin.process.traversal.Order;
import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.Cardinality;

import java.util.Date;
import java.util.List;

/**
 * @Description
 * @DATE 2021.8.10 13:51
 * @Author yangjie
 **/

public class AirSchemaFactory implements SchemaFactory {

   public static final String PERSON_LABEL_V = "object.person";
    public static final String AIR_LABEL_V = "object.air";
    public static final String EVENT_LABEL_V = "object.cj";

    static final String SIMPLE_LABEL_E = "link.simple";

    public static final String NAME_PROPERTY = "property.name";
    public static final String SFZH_PROPERTY = "property.sfzh";
    public static final String FLIGHTNO_PROPERTY = "property.flightNo";
    public  static final String FLIGHTDAY_PROPERTY = "property.flightDay";

    static final String sfzh_index="sfz_Graph_index";
    static final String name_index="name_Graph_index";
    static final String flight_no_index="flight_no_index";
    static final String flight_event_index="flight_event_index";
    static final String flight_day_index="flight_day_index";
    static final String flight_day_v_index="flight_day_v_index";

    public TrsGraphSchemaFactory graphSchemaFactory;

    public AirSchemaFactory(TrsGraphSchemaFactory graphSchemaFactory) {
        this.graphSchemaFactory = graphSchemaFactory;
    }

    @Override
    public boolean createLabel() {

        List<String> labels = Lists.newArrayList(PERSON_LABEL_V, AIR_LABEL_V, EVENT_LABEL_V);
        graphSchemaFactory.createVertexLabel(labels);
        return true;
    }

    @Override
    public boolean createProperty() {
        graphSchemaFactory.createProperty(buildProperty());
        return true;
    }

    protected  List<Property>  buildProperty(){
        Property name = Property.newInstance(NAME_PROPERTY, String.class, Cardinality.SINGLE);
        Property sfz = Property.newInstance(SFZH_PROPERTY, String.class, Cardinality.SINGLE);
        Property flightNo = Property.newInstance(FLIGHTNO_PROPERTY, String.class, Cardinality.SINGLE);
        Property flightDay = Property.newInstance(FLIGHTDAY_PROPERTY, Date.class, Cardinality.SINGLE);
        List<Property> propList = Lists.newArrayList(name,sfz,flightDay,flightNo);
        return propList;
    }

    @Override
    public boolean createIndex() {
        graphSchemaFactory.createVertexIndex(buildGraphIndices());
        graphSchemaFactory.createEdgeIndex(buildVertexIndex());
        return false;
    }

    protected List<GraphIndex> buildGraphIndices(){
        GraphIndex sfz_Graph_index = GraphIndex.newInstance(sfzh_index, Vertex.class, Lists.newArrayList(SFZH_PROPERTY), GraphIndex.IndexType.Composite);
        GraphIndex name_Graph_index = GraphIndex.newInstance(name_index, Vertex.class, Lists.newArrayList(NAME_PROPERTY), GraphIndex.IndexType.Composite);

        GraphIndex flightNo_Graph_index = GraphIndex.newInstance(flight_no_index, Vertex.class, Lists.newArrayList(FLIGHTNO_PROPERTY), GraphIndex.IndexType.Composite);
        GraphIndex flightEvent_Graph_index = GraphIndex.newInstance(flight_event_index, Vertex.class, Lists.newArrayList(FLIGHTNO_PROPERTY,FLIGHTDAY_PROPERTY), GraphIndex.IndexType.Composite);

        GraphIndex flightDay_Graph_index = GraphIndex.newInstance(flight_day_index, Vertex.class, Lists.newArrayList(FLIGHTDAY_PROPERTY), GraphIndex.IndexType.Composite);
        List<GraphIndex> indices = Lists.newArrayList(sfz_Graph_index, flightDay_Graph_index,flightEvent_Graph_index, flightNo_Graph_index);
       return indices;
    }

    protected List<VertexIndex> buildVertexIndex(){
        VertexIndex dayVIndex = new VertexIndex("link.simple", flight_day_v_index, Direction.BOTH, Order.desc, "property.flightDay");
        return Lists.newArrayList(dayVIndex);
    }

    @Override
    public boolean createEdge() {

        graphSchemaFactory.createEdgeLabel(Lists.newArrayList(SIMPLE_LABEL_E));
        return true;
    }

    @Override
    public boolean updateIndexStatus() {
        Triple<String,String,String> sfzPair = Triple.of(sfzh_index,"graph",null);
        Triple<String,String,String> namePair = Triple.of(name_index,"graph",null);
        Triple<String,String,String> flightNoPair = Triple.of(flight_no_index,"graph",null);
        Triple<String,String,String> flightDayPair = Triple.of(flight_day_index,"graph",null);
        Triple<String,String,String> flightDayVPair = Triple.of(flight_day_v_index,"vertex",SIMPLE_LABEL_E);
        Triple<String,String,String> flightEventIndex = Triple.of(flight_event_index,"graph",null);
//        List<Triple<String,String, String>> pairs = Lists.newArrayList(sfzPair, namePair, flightDayPair, flightNoPair, flightDayVPair);
        List<Triple<String,String, String>> pairs = Lists.newArrayList(sfzPair,flightEventIndex, flightDayPair, flightNoPair, flightDayVPair);
        graphSchemaFactory.updateIndexStatus(pairs);
        return false;
    }
}
