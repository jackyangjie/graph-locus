package com.trs.locus.loaddata;

import com.trs.locus.bo.AirBO;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * @Description
 * @DATE 2021.8.10 11:21
 * @Author yangjie
 **/

public class CsvReadData implements ReadData<AirBO>{

    private String pathname;

    public CsvReadData(String pathname) {
        this.pathname = pathname;
    }

    public AirBO  readLine () throws IOException {
        File file = new File(pathname);
        try(FileInputStream fileInputStream = new FileInputStream(file);){
        FileChannel channel = fileInputStream.getChannel();
        int capacity  = 1024 * 1024;
        ByteBuffer byteBuffer = ByteBuffer.allocate(capacity);
        StringBuffer buffer = new StringBuffer();
        while (channel.read(byteBuffer) != -1){
            byteBuffer.clear();
            byte[] array = byteBuffer.array();
            String st = new String(array);
            System.out.println(st);
           }
        }
     return  null;
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public AirBO next() {

        try {
            Stream<String> lines = Files.lines(Paths.get(pathname));
            lines.skip(0).map(l -> AirBO.stringToAirBO(l) ).forEach(
                 a -> System.out.println(a.getId())
            );

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
