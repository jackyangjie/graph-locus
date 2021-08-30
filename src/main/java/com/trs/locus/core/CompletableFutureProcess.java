package com.trs.locus.core;

import com.google.common.collect.Lists;
import com.trs.locus.loaddata.ReadData;
import com.trs.locus.metadata.SchemaFactory;
import com.trs.locus.metadata.TrsGraphFactory;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.janusgraph.core.JanusGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @Description
 * @DATE 2021.8.30 11:16
 * @Author yangjie
 **/

public class CompletableFutureProcess  extends TaskProcess{

    private static final Logger log = LoggerFactory.getLogger(CompletableFutureProcess.class);

    private int threadNum = 3;
    private BlockingQueue queue = new LinkedBlockingQueue<Runnable>(3);
    ThreadPoolExecutor executors = new ThreadPoolExecutor(threadNum, threadNum,
            0L, TimeUnit.MILLISECONDS, queue,new ConcurrentTaskProcess.DataImportThreadFactory(), new ConcurrentTaskProcess.BlockRejectedExecutionHandler());

    public CompletableFutureProcess(SchemaFactory schemaFactory, GraphDataBuilder graphDataBuilder, ReadData<?> readData) {
        super(schemaFactory, graphDataBuilder, readData);
    }



    @Override
    protected void importData(){
        JanusGraph graphInstance = TrsGraphFactory.getGraphInstance();
        long count = 0;
        List dataList = new ArrayList(TX_BATCH_SIZE);
        CountDownLatch  countDownLatch = new CountDownLatch(threadNum * 2);
        Graph threadedTx = graphInstance.tx().createThreadedTx();
        Instant now = Instant.now();
        while (readData.hasNext()){
            count ++;
            dataList.add(readData.next());
            if (count % TX_BATCH_SIZE == 0){
                CountDownLatch finalCountDownLatch = countDownLatch;
                CompletableFuture
                        .runAsync(graphDataBuilder.builder(threadedTx, Lists.newArrayList(dataList)),executors)
                        .whenComplete((v, e) ->finalCountDownLatch.countDown());
                dataList.clear();
                log.info("当前已经读入的数据量："+count );
                    if (count %( TX_BATCH_SIZE * threadNum * 2) == 0 ){
                        try {
                            countDownLatch.await(30,TimeUnit.SECONDS);
                            threadedTx.tx().commit();
                            log.info("当事务{}，提交耗时：{}毫秒",threadedTx.tx().hashCode(), Duration.between(now, Instant.now()).toMillis());
                            threadedTx = graphInstance.tx().createThreadedTx();
                            now = Instant.now();
                            countDownLatch = new CountDownLatch(threadNum * 2);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
            }
        }
        threadedTx.tx().commit();
        log.info("当前已经读入的数据量："+count );


    }

}
