package com.trs.locus.loaddata;


import java.io.*;
import java.util.function.Function;

/**
 * @Description
 * @DATE 2021.8.10 13:29
 * @Author yangjie
 **/

public class CsvReadDataIterator<T> implements ReadData<T> {

    private String pathname;

    private BufferedReader br;

    private T current;

    private Function<String,T> conversion;

    public CsvReadDataIterator(String pathname,Function<String,T> conversion) {
        this.pathname = pathname;
        this.conversion = conversion;
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
                current = conversion.apply(line);
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public T next() {
        return current;

    }
}
