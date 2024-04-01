package com.skt.KafkaESpipeline.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class DateUtils {

    public String getDateNowString(){
        LocalDate today = LocalDate.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

        return today.format(formatter);
    }
}
