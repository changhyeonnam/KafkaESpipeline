package com.skt.KafkaESpipeline.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.skt.KafkaESpipeline.service.ConsumerPollService;
import com.skt.KafkaESpipeline.service.OpenSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;


@Slf4j
@CrossOrigin(origins = "*")
@Controller
@RequestMapping("/api")
public class DataController {



    private final ConsumerPollService consumerPollService;
    private final OpenSearchService openSearchService;

    @Autowired
    public DataController(ConsumerPollService consumerPollService, OpenSearchService openSearchService){
        this.consumerPollService = consumerPollService;
        this.openSearchService = openSearchService;
    }

    /**
     * poll방식으로 카프카의 데이터를 가져옴.
     * @return 카프카 데이터
     */
    @RequestMapping(value = "/consumer", method = RequestMethod.GET)
    @ResponseBody
    public String consumer(){
        return consumerPollService.consumepoll();
    }


    /**
     * opensearch에서 log를 불러와서 화면에 출력.
     * @param model
     * @return 맵형식의 Log를 모은 리스트
     */
    @RequestMapping(value = "/showLog", method = RequestMethod.GET)
    public String showLog(Model model){
        List<Map<String, Object>> logs = openSearchService.getOpenSearchData();
        System.out.println(logs);
        try {
            model.addAttribute("logs",logs);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return "logs";
    }



}
