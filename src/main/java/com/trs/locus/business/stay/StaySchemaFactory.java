package com.trs.locus.business.stay;

import com.google.common.collect.Lists;
import com.trs.locus.metadata.GraphIndex;
import com.trs.locus.metadata.Property;
import com.trs.locus.metadata.SchemaFactory;
import com.trs.locus.metadata.TrsGraphSchemaFactory;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.Cardinality;

import java.util.Date;
import java.util.List;

/**
 * @Description
 * @DATE 2021.8.24 17:27
 * @Author yangjie
 **/

public class StaySchemaFactory implements SchemaFactory {

    public static final String PERSON_LABEL_V = "object.person";
    public static final String HOTEL_LABEL_V = "object.hotel";
    public static final String EVENT_LABEL_V = "object.stay";

    static final String SIMPLE_LABEL_E = "link.simple";

    public static final String NAME_PROPERTY = "property.name";
    public static final String SFZH_PROPERTY = "property.sfzh";
    public static final String HOTEL_NAME_PROPERTY = "property.hotel";
    public  static final String HOTEL_ROOM_PROPERTY = "property.room";

    public  static final String KFSJ_PROPERTY = "property.kfsj";

    public  static final String TFSJ_PROPERTY = "property.tfsj";


    static final String sfzh_index="sfz_Graph_index";
    static final String name_index="name_Graph_index";
    static final String HOTEL_INDEX="hotel_index";
    static final String HOTEL_ROOM_INDEX="hotel_room_index";
    static final String STAY_TIME_INDEX="stay_time_index";
    static final String flight_day_index="flight_day_index";
    static final String flight_day_v_index="flight_day_v_index";

    private TrsGraphSchemaFactory graphSchemaFactory;

    public StaySchemaFactory(TrsGraphSchemaFactory graphSchemaFactory) {
        this.graphSchemaFactory = graphSchemaFactory;
    }

    @Override
    public boolean createLabel() {

        List<String> labels = Lists.newArrayList(PERSON_LABEL_V, HOTEL_LABEL_V, EVENT_LABEL_V);
        graphSchemaFactory.createVertexLabel(labels);
        return true;
    }

    @Override
    public boolean createProperty() {
        graphSchemaFactory.createProperty(buildProperty());
        return true;
    }

    protected List<Property> buildProperty(){
        Property name = Property.newInstance(NAME_PROPERTY, String.class, Cardinality.SINGLE);
        Property sfz = Property.newInstance(SFZH_PROPERTY, String.class, Cardinality.SINGLE);
        Property hotel = Property.newInstance(HOTEL_NAME_PROPERTY, String.class, Cardinality.SINGLE);
        Property room = Property.newInstance(HOTEL_ROOM_PROPERTY, String.class, Cardinality.SINGLE);
        Property kfsj = Property.newInstance(KFSJ_PROPERTY, Date.class, Cardinality.SINGLE);
        Property tfsj = Property.newInstance(TFSJ_PROPERTY, Date.class, Cardinality.SINGLE);
        List<Property> propList = Lists.newArrayList(name,sfz,hotel,room,kfsj,tfsj);
        return propList;
    }


    @Override
    public boolean createIndex() {
        graphSchemaFactory.createVertexIndex(buildGraphIndices());
//        graphSchemaFactory.createEdgeIndex(buildVertexIndex());
        return false;
    }

    protected List<GraphIndex> buildGraphIndices(){
        GraphIndex sfz_Graph_index = GraphIndex.newInstance(sfzh_index, Vertex.class, Lists.newArrayList(SFZH_PROPERTY), GraphIndex.IndexType.Composite);
        GraphIndex name_Graph_index = GraphIndex.newInstance(name_index, Vertex.class, Lists.newArrayList(NAME_PROPERTY), GraphIndex.IndexType.Composite);
        GraphIndex hotel_index = GraphIndex.newInstance(HOTEL_INDEX, Vertex.class, Lists.newArrayList(HOTEL_NAME_PROPERTY), GraphIndex.IndexType.Composite);
        GraphIndex hotel_room_index = GraphIndex.newInstance(HOTEL_ROOM_INDEX, Vertex.class, Lists.newArrayList(HOTEL_NAME_PROPERTY,HOTEL_ROOM_PROPERTY), GraphIndex.IndexType.Composite);
        GraphIndex stay_time_index = GraphIndex.newInstance(STAY_TIME_INDEX, Vertex.class, Lists.newArrayList(KFSJ_PROPERTY,TFSJ_PROPERTY), GraphIndex.IndexType.Mixed);
        List<GraphIndex> indices = Lists.newArrayList(hotel_index,sfz_Graph_index, name_Graph_index,hotel_room_index, stay_time_index);
        return indices;
    }

    @Override
    public boolean createEdge() {
        graphSchemaFactory.createEdgeLabel(Lists.newArrayList(SIMPLE_LABEL_E));
        return true;
    }

    @Override
    public boolean updateIndexStatus() {
        Triple<String,String,String> sfzTriple = Triple.of(sfzh_index,"graph",null);
        Triple<String,String,String> hotelTriple = Triple.of(HOTEL_INDEX,"graph",null);
        Triple<String,String,String> nameTriple = Triple.of(name_index,"graph",null);
        Triple<String,String,String> hotelRoomTriple = Triple.of(HOTEL_ROOM_INDEX,"graph",null);
        Triple<String,String,String> stayTimeTriple = Triple.of(STAY_TIME_INDEX,"graph",null);
        List<Triple<String,String, String>> pairs = Lists.newArrayList(hotelTriple,sfzTriple,nameTriple,hotelRoomTriple, stayTimeTriple);
        graphSchemaFactory.updateIndexStatus(pairs);
        return false;
    }
}
