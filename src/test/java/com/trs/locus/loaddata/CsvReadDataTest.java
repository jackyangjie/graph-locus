package com.trs.locus.loaddata;

import com.trs.locus.DateUtil;
import com.trs.locus.business.airplane.AirSchemaFactory;
import com.trs.locus.business.airplane.bo.AirBO;
import com.trs.locus.business.stay.bo.StayBO;
import com.trs.locus.metadata.TrsGraphFactory;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraph;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

/**
 * @Description
 * @DATE 2021.8.10 13:03
 * @Author yangjie
 **/

public class CsvReadDataTest {

    @Test
    @Ignore
    public void readTest() throws IOException {
        String pathName = "C:\\Users\\71908\\Desktop\\数据库导出 .txt";
        ReadData<AirBO> csvReadData = new CsvReadDataIterator(pathName,null);
        while (csvReadData.hasNext()){
            AirBO next = csvReadData.next();
            System.out.println(next.getId());
        }
    }
    @Test
    public void dateUtil(){
//        String str = "2011/12/04";
        String str = "201407160218";
        System.out.println(DateUtil.stringToDate2(str));
    }


    @Test
    public void test(){
        String pathName = "C:\\Users\\71908\\Desktop\\stay.txt";
        Function<String, StayBO> conversion = (String line) -> StayBO.toStay(line);
        ReadData<StayBO> csvReadData = new CsvReadDataIterator(pathName,conversion);
        while (csvReadData.hasNext()){
            StayBO next = csvReadData.next();
            System.out.println(next.getName());
        }
    }

    @Test
    public void testQueryVertex(){
        String config = "E:\\janus\\janusgraph-import\\target\\classes\\janusgraph-hbase-es.properties";
        JanusGraph graphInstance = TrsGraphFactory.getGraphInstance(config);
        GraphTraversalSource g = graphInstance.traversal();
        List<Vertex> next = g.V().hasLabel(AirSchemaFactory.EVENT_LABEL_V)
                .has(AirSchemaFactory.SFZH_PROPERTY, "441111197412213131")
                .has(AirSchemaFactory.FLIGHTNO_PROPERTY, "3U8610")
                .has(AirSchemaFactory.FLIGHTDAY_PROPERTY, DateUtil.stringToDate("2011/12/4")).next(10);
        for (Vertex vertex : next) {
            System.out.println(next);
        }

    }
}
