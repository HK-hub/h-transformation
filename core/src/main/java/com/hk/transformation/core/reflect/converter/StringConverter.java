package com.hk.transformation.core.reflect.converter;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import org.apache.commons.lang3.StringUtils;

public class StringConverter {
    public static String[] convertCsvStringToArray(String csvString) {
        if (StringUtils.isEmpty(csvString)) {
            return new String[0];
        }
        return csvString.split(",");
    }

    public static String[] convertJsonStringToArray(String jsonString) {
        try {

            JsonArray jsonArray = JsonParser.parseString(jsonString).getAsJsonArray();
            String[] array = new String[jsonArray.size()];
            for (int i = 0; i < jsonArray.size(); i++) {
                array[i] = jsonArray.get(i).getAsString();
            }
            return array;
        } catch (Exception e) {
            return new String[0];
        }
    }

    public static String[] convertListStringToArray(String listString) {
        if (StringUtils.isEmpty(listString)) {
            return new String[0];
        }
        listString = listString.replaceAll("\\[|\\]", "");
        return listString.split(",");
    }

    public static String[] convertStringToArray(String inputString) {
        inputString = inputString.trim();

        String[] array = convertCsvStringToArray(inputString);
        if (array.length == 0) {
            array = convertJsonStringToArray(inputString);
        }
        if (array.length == 0) {
            array = convertListStringToArray(inputString);
        }
        return array;
    }

    public static void main(String[] args) {
        String string1 = "hy5hy,ggt,5h4th";
        String string2 = "{\"\":\"\",\"\":\"\",\"\":\"\"}";
        String string3 = "[\"\", \"\",\"\"]";

        String[] array1 = convertStringToArray(string1);
        String[] array2 = convertStringToArray(string2);
        String[] array3 = convertStringToArray(string3);

        for (String element : array1) {
            System.out.println(element);
        }

        for (String element : array2) {
            System.out.println(element);
        }

        for (String element : array3) {
            System.out.println(element);
        }
    }
}
