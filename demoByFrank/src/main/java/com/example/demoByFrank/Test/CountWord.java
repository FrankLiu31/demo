package com.example.demoByFrank.Test;



import java.io.*;
import java.util.*;



public class CountWord {

    private static List<Map.Entry<String, Integer>> textList;
    private static TreeMap<String, Integer> textMap;

    public static TreeMap<String, Integer> getTextMap() {
        return textMap;
    }

    public static List<Map.Entry<String, Integer>> getTextList() {
        return textList;
    }

    static{
        File file = new File("test.txt");
        BufferedReader bf = null;
        try {
            bf = new BufferedReader(new FileReader(file));
            textMap = new TreeMap<>();
            String line;
            StringBuffer sb = new StringBuffer();
            while ((line = bf.readLine()) != null) {
                sb.append(line).append(" ");
            }

            StringTokenizer st = new StringTokenizer(sb.toString(),",. \n");

            while (st.hasMoreTokens()) {
                String key = st.nextToken();
                key = key.substring(0,1).toUpperCase()+key.substring(1);
                if (textMap.get(key) != null) {
                    int value = textMap.get(key);
                    value++;
                    textMap.put(key, value);
                } else {
                    textMap.put(key,1);
                }
            }

            bf.close();
            textList = new ArrayList<>(textMap.entrySet());
            Collections.sort(textList, (a, b) -> b.getValue() - a.getValue());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        textList.forEach(it -> System.out.println(it.getKey() + " " + it.getValue()));
    }
}