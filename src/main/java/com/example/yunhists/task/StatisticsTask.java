package com.example.yunhists.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.yunhists.entity.Thesis;
import com.example.yunhists.entity.User;
import com.example.yunhists.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Component
public class StatisticsTask {

    @Autowired
    ThesisService thesisService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    CategoryLinkService categoryLinkService;

    @Autowired
    UserService userService;

    @Autowired
    ShareService shareService;

    public static String filePath = "src/main/resources/statistics/";

    @Scheduled(cron ="0 55 23 * * ?")
    public void generateGeneralStatistics() throws IOException {
        Map<String, Long> result = new LinkedHashMap<>();
        result.put("thesisCount", thesisService.count());
        result.put("categoryCount", categoryService.count());
        result.put("categoryLinkCount", categoryLinkService.count());
        List<User> list = userService.getAll();
        long userCount = 0;
        for(User s : list) {
            if(s.getEmail().length() != 0) {
                userCount++;
            }
        }
        result.put("userCount", userCount);
        result.put("shareCount", shareService.count());

        ObjectMapper mapper = new ObjectMapper();
        String fileName = "general.json";
        File file = new File(filePath + fileName);
        mapper.writeValue(file, result);
    }

    @Scheduled(cron ="0 55 23 * * ?")
    public void generateThesisCopyrightStatistics() throws IOException {
        Map<String, Long> result = new LinkedHashMap<>();

        QueryWrapper<Thesis> queryWrapper0 = new QueryWrapper<>();
        queryWrapper0.eq("copyright_status", 0);
        result.put("allRightsReserved", thesisService.count(queryWrapper0));

        QueryWrapper<Thesis> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("copyright_status", 1);
        result.put("openAccess", thesisService.count(queryWrapper1));

        QueryWrapper<Thesis> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("copyright_status", 2);
        result.put("publicDomain", thesisService.count(queryWrapper2));

        ObjectMapper mapper = new ObjectMapper();
        String fileName = "copyright.json";
        File file = new File(filePath + fileName);
        mapper.writeValue(file, result);
    }

    @Scheduled(cron ="0 55 23 * * ?")
    public void generateThesisTypeStatistics() throws IOException {
        Map<String, Long> result = new LinkedHashMap<>();

        QueryWrapper<Thesis> queryWrapper0 = new QueryWrapper<>();
        queryWrapper0.eq("type", 0);
        result.put("journal", thesisService.count(queryWrapper0));

        QueryWrapper<Thesis> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("type", 1);
        result.put("collection", thesisService.count(queryWrapper1));

        QueryWrapper<Thesis> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("type", 2);
        result.put("chapter", thesisService.count(queryWrapper2));

        QueryWrapper<Thesis> queryWrapper3 = new QueryWrapper<>();
        queryWrapper3.eq("type", 3);
        result.put("newspaper", thesisService.count(queryWrapper3));

        ObjectMapper mapper = new ObjectMapper();
        String fileName = "thesisType.json";
        File file = new File(filePath + fileName);
        mapper.writeValue(file, result);
    }

    @Scheduled(cron ="0 55 23 * * ?")
    public void generateThesisYearStatistics() throws IOException {

        // 1. Get all years
        HashSet<Integer> yearSet = new HashSet<>();
        List<Thesis> allThesis = thesisService.getAll();
        for(Thesis t : allThesis) {
            if(t.getYear() != null) {
                yearSet.add(t.getYear());
            }
        }
        int max = Collections.max(yearSet);
        int min = Collections.min(yearSet);

        // 2. Prepare HashMap
        Map<Integer, Integer> map = new HashMap<>();
        for(int i = min; i <= max; i++) {
            map.put(i, 0);
        }

        // 3. Count
        for(Thesis t : allThesis) {
            if(t.getYear() != null) {
                map.put(t.getYear(), map.get(t.getYear()) + 1);
            }
        }

        // 4. Sort
        List<Map.Entry<Integer, Integer>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByKey());
        Map<Integer, Integer> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<Integer, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        ObjectMapper mapper = new ObjectMapper();
        String fileName = "year.json";
        File file = new File(filePath + fileName);
        mapper.writeValue(file, sortedMap);
    }

}
