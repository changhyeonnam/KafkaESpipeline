package com.skt.KafkaESpipeline.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class DateUtils {

    private final String indexPrefix;

    @Autowired
    public DateUtils(@Value("${opensearch.index-prefix}") String indexPrefix){
        this.indexPrefix = indexPrefix;
    }


    public String getDateNowString(){
        LocalDate today = LocalDate.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

        return today.format(formatter);
    }

    public String getIndex(){
        return indexPrefix+getDateNowString();
    }
}
