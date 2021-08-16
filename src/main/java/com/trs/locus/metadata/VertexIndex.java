package com.trs.locus.metadata;

import org.apache.tinkerpop.gremlin.process.traversal.Order;
import org.apache.tinkerpop.gremlin.structure.Direction;

/**
 * @Description
 * @DATE 2021.8.10 15:01
 * @Author yangjie
 **/

public class VertexIndex {
    private String label;
    private String name;
    private Direction direction;
    private Order order;
    private String key;

    public VertexIndex(String label, String name, Direction direction, Order order, String key) {
        this.label = label;
        this.name = name;
        this.direction = direction;
        this.order = order;
        this.key = key;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
