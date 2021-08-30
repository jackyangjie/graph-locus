package com.trs.locus.common;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @Description
 * @DATE 2021.8.25 13:50
 * @Author yangjie
 **/

public class ErrorData {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static ErrorData INSTANCE;
    private  BlockingQueue<String> deque = new ArrayBlockingQueue<String>(1000);

    private ErrorData(){
            SyncWriteErrorData syncWriteErrorData = new SyncWriteErrorData(deque,getErrorDataFileName());
            Thread thread = new Thread(syncWriteErrorData);
            thread.setDaemon(true);
            thread.start();
    }

    public static   ErrorData newInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ErrorData();
        }
        return INSTANCE ;
    }

    private  String getErrorDataFileName(){
        String property = System.getProperty("user.dir");
        return property+"/error-data.txt";
    }



      class SyncWriteErrorData implements Runnable{

         String fileName;
         BlockingQueue<String> deque;

         public SyncWriteErrorData(BlockingQueue<String> deque,String fileName) {
             this.fileName = fileName;
             this.deque = deque;
         }

         @Override
        public void run() {
             logger.info("错误数据文件路径：{}",fileName);
           try(FileOutputStream fos = new FileOutputStream(fileName, true);
               FileChannel channel = fos.getChannel();){
                while (true){
                    String line = deque.poll();
                    if (line == null) {continue;}
                    line = line + "\r\n";
                    ByteBuffer buf = ByteBuffer.wrap((line).getBytes());
                    buf.put(line.getBytes());
                    buf.flip();
                    channel.write(buf);
                  }
           } catch (FileNotFoundException e) {
               e.printStackTrace();
           } catch (IOException e) {
               e.printStackTrace();
           }
        }
    }




    public  void putData(String data){
        try {
            deque.put(data);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
