package com.example.demoByFrank.Test;


import net.minidev.json.JSONObject;
import org.apache.tomcat.util.file.ConfigurationSource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.*;

import java.util.List;
import org.springframework.http.HttpHeaders;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;


@RestController
public class TestController {

    /**
     * 
     * @param n
     */


    @GetMapping(value = "/counter_api/top/{n}", produces = "text/csv")
    public ResponseEntity<ConfigurationSource.Resource> getTopNText(@PathVariable(name = "n") int n) {
        List<Map.Entry<String, Integer>> list = CountWord.getTextList().subList(0,n);

        ByteArrayInputStream byteArrayOutputStream;
        try (
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                // defining the CSV printer
                CSVPrinter csvPrinter = new CSVPrinter(
                        new PrintWriter(out),
                        // withHeader is optional
                        CSVFormat.DEFAULT
                                .withDelimiter('|')
                                //.withHeader(csvHeader)
                );
        ) {
            // populating the CSV content
            for (int i = 0; i < n; i++) {
                csvPrinter.printRecord(list.get(i).getKey(),list.get(i).getValue());
            }

            csvPrinter.flush();
            csvPrinter.close();
            byteArrayOutputStream = new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        InputStreamResource fileInputStream = new InputStreamResource(byteArrayOutputStream);

        String csvFileName = "HighestCounts.csv";

        // setting HTTP headers
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + csvFileName);
        // defining the custom Content-Type
        headers.set(HttpHeaders.CONTENT_TYPE, "text/csv");

        return new ResponseEntity(
                fileInputStream,
                headers,
                HttpStatus.OK
        );

    }

    /**
     * 
     *
     */

    @PostMapping(path="/counter_api/search")
    public @ResponseBody
    ResponseEntity storeCache(@RequestBody JSONObject obj) {

        List <String> array= (ArrayList) obj.get("searchText");
        Map <String,Integer> map = new LinkedHashMap<>();
        for (int i = 0; i < array.size(); i++) {
            Object tmp = CountWord.getTextMap().get(array.get(i));
            if(tmp==null)
                map.put(array.get(i), 0);
            else
                map.put(array.get(i),(int)tmp);
        }

        List <Map.Entry<String, Integer>> result = new ArrayList<>(map.entrySet());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("counts", result);
        return  ResponseEntity.ok(jsonObject);
    }

}
