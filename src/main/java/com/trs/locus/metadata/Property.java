package com.trs.locus.metadata;

import org.janusgraph.core.Cardinality;

/**
 * @Description
 * @DATE 2021.8.3 14:38
 * @Author yangjie
 **/

public class Property {
    private String name;
    private Class<?> type;
    private Cardinality cardinality;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public Cardinality getCardinality() {
        return cardinality;
    }

    public void setCardinality(Cardinality cardinality) {
        this.cardinality = cardinality;
    }

    public static  Property newInstance(String uri,Class<?> type,Cardinality cardinality){
        Property prop = new Property();
        prop.setType(type);
        prop.setName(uri);
        prop.setCardinality(cardinality);
        return prop;
    }
}
