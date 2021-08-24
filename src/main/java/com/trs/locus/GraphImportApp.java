package com.trs.locus;

import com.trs.locus.business.airplane.AirGraphDataBuilder;
import com.trs.locus.business.airplane.AirSchemaFactory;
import com.trs.locus.business.airplane.bo.AirBO;
import com.trs.locus.core.GraphDataBuilder;
import com.trs.locus.core.TaskProcess;
import com.trs.locus.loaddata.CsvReadDataIterator;
import com.trs.locus.loaddata.ReadData;
import com.trs.locus.metadata.SchemaFactory;
import com.trs.locus.metadata.TrsGraphSchemaFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

/**
 * @Description
 * @DATE 2021.8.10 13:48
 * @Author yangjie
 **/
public class GraphImportApp {
   private static final  Logger log = LoggerFactory.getLogger(GraphImportApp.class);
    public static void main(String[] args) {
        GraphImportApp app = new GraphImportApp();
        if(args != null && args.length == 2){
            app.start(args[0],args[1]);
        }
    }

    private void start(String config,String pathName) {
        log.info("启动数据导入任务");
        TrsGraphSchemaFactory graphSchemaFactory = new TrsGraphSchemaFactory(config);
        SchemaFactory schemaFactory = new AirSchemaFactory(graphSchemaFactory);
        GraphDataBuilder builder = new AirGraphDataBuilder();
//        SchemaFactory schemaFactory = new KgAirSchemaFactory(graphSchemaFactory);
//        GraphDataTaskBuilder builder = new TrsAirGraphDataBuilder();
        Function<String, AirBO> conversion = (String line) -> AirBO.stringToAirBO(line);
        ReadData<AirBO> readData = new CsvReadDataIterator(pathName,conversion);
        TaskProcess taskProcess = new TaskProcess(schemaFactory,builder,readData);
//        TaskProcess taskProcess = new ConcurrentTaskProcess(schemaFactory,builder,readData);
        taskProcess.process();
        log.info("数据导入任务完成");
    }



}
