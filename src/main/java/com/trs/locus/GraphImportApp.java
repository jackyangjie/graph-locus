package com.trs.locus;

import com.trs.locus.business.airplane.AirGraphDataBuilder;
import com.trs.locus.business.airplane.AirSchemaFactory;
import com.trs.locus.business.airplane.bo.AirBO;
import com.trs.locus.business.stay.StayGraphDataBuilder;
import com.trs.locus.business.stay.StaySchemaFactory;
import com.trs.locus.business.stay.bo.StayBO;
import com.trs.locus.core.CompletableFutureProcess;
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
        if(args != null && args.length == 3){
            app.start(args[0],args[1],args[2]);
        }
    }

    private void start(String config,String pathName,String taskType) {
        log.info("启动数据导入任务");
        TrsGraphSchemaFactory graphSchemaFactory = new TrsGraphSchemaFactory(config);
        TaskProcess taskProcess = null;
        if ("air".equals(taskType)){
             taskProcess = airTask(graphSchemaFactory,pathName);
        }else if("stay".equals(taskType)){
            taskProcess = stayTask(graphSchemaFactory,pathName);
        }
        taskProcess.process();
        log.info("数据导入任务完成");
    }


    private TaskProcess airTask(TrsGraphSchemaFactory graphSchemaFactory,String pathName){
        SchemaFactory schemaFactory = new AirSchemaFactory(graphSchemaFactory);
        GraphDataBuilder builder = new AirGraphDataBuilder();
//        SchemaFactory schemaFactory = new KgAirSchemaFactory(graphSchemaFactory);
//        GraphDataTaskBuilder builder = new TrsAirGraphDataBuilder();
        Function<String, AirBO> conversion = (String line) -> AirBO.stringToAirBO(line);
        ReadData<AirBO> readData = new CsvReadDataIterator(pathName,conversion);
//        TaskProcess taskProcess = new TaskProcess(schemaFactory,builder,readData);
        TaskProcess taskProcess = new CompletableFutureProcess(schemaFactory,builder,readData);
        return taskProcess;
    }


    private TaskProcess stayTask(TrsGraphSchemaFactory graphSchemaFactory,String pathName){
        SchemaFactory schemaFactory = new StaySchemaFactory(graphSchemaFactory);
        GraphDataBuilder builder = new StayGraphDataBuilder();
        Function<String, StayBO> conversion = (String line) -> StayBO.toStay(line);
        ReadData<StayBO> readData = new CsvReadDataIterator(pathName,conversion);
        TaskProcess taskProcess = new TaskProcess(schemaFactory,builder,readData);
        return taskProcess;
    }


}
