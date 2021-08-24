package com.trs.locus.core;

import com.google.common.collect.Lists;
import com.trs.locus.loaddata.ReadData;
import com.trs.locus.metadata.SchemaFactory;
import com.trs.locus.metadata.TrsGraphFactory;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Transaction;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @Description
 * @DATE 2021.8.10 14:06
 * @Author yangjie
 **/

public class ConcurrentTaskProcess extends TaskProcess {

    private static final Logger log = LoggerFactory.getLogger(ConcurrentTaskProcess.class);
//    private ExecutorService executorService = Executors.newFixedThreadPool(3);
    private int threadNum = 3;
    private BlockingQueue queue = new LinkedBlockingQueue<Runnable>(10);
    ThreadPoolExecutor executors = new ThreadPoolExecutor(threadNum, threadNum,
            0L, TimeUnit.MILLISECONDS, queue,new DataImportThreadFactory(), new BlockRejectedExecutionHandler());

    public ConcurrentTaskProcess(SchemaFactory schemaFactory, GraphDataBuilder graphDataBuilder, ReadData<?> readData) {
        super(schemaFactory, graphDataBuilder, readData);
    }


    @Override
    protected void importData(){
        JanusGraph graphInstance = TrsGraphFactory.getGraphInstance();
        long count = 0;
        List dataList = new ArrayList(TX_BATCH_SIZE);
        while (readData.hasNext()){
            count ++;
            dataList.add(readData.next());
            if (count % TX_BATCH_SIZE == 0){
                executors.submit(graphDataBuilder.builder(graphInstance,Lists.newArrayList(dataList)));
                dataList.clear();
                log.info("当前已经读入的数据量："+count );
            }
        }
            executors.submit(graphDataBuilder.builder(graphInstance,Lists.newArrayList(dataList)));
            log.info("当前已经读入的数据量："+count );

            while (true){
                try {
                    Thread.sleep(3000);
                    System.out.println(
                            String.format("[monitor] [%d/%d] Active: %d, Completed: %d, Task: %d, isShutdown: %s, isTerminated: %s",
                                    this.executors.getPoolSize(),
                                    this.executors.getCorePoolSize(),
                                    this.executors.getActiveCount(),
                                    this.executors.getCompletedTaskCount(),
                                    this.executors.getTaskCount(),
                                    this.executors.isShutdown(),
                                    this.executors.isTerminated()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
    }


    //数据导入线程工厂
    static class DataImportThreadFactory implements ThreadFactory{

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r,"data-import-pool-"+r.hashCode());
        }
    }


    static class BlockRejectedExecutionHandler implements RejectedExecutionHandler{
        //自定义线程池的满的情况下的策略 阻塞
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            try {
                executor.getQueue().put(r);
//                log.info("当前运行任务数：{}，当前队列还有任务数：{}",executor.getActiveCount(),executor.getQueue().size());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
