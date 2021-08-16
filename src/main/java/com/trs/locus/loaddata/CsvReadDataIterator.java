package com.trs.locus.loaddata;

import com.trs.locus.bo.AirBO;

import java.io.*;

/**
 * @Description
 * @DATE 2021.8.10 13:29
 * @Author yangjie
 **/

public class CsvReadDataIterator implements ReadData<AirBO> {

    private String pathname;

    private BufferedReader br;

    private AirBO current;

    public CsvReadDataIterator(String pathname) {
        this.pathname = pathname;
    }

    public void initCsvReadDataIterator(){
        try {
            FileInputStream fileInputStream = new FileInputStream(pathname);
            this.br = new BufferedReader(new InputStreamReader(fileInputStream));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean hasNext() {
        if (br == null){
            initCsvReadDataIterator();
            try {
                br.readLine(); //跳过第一行表头
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            String line = br.readLine();
            if (line != null) {
                line = line.replaceAll("\"","");
                current = AirBO.stringToAirBO(line);
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public AirBO next() {
        return current;

    }
}
