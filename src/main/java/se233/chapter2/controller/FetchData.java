package se233.chapter2.controller;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import se233.chapter2.Launcher;
import se233.chapter2.model.CurrencyEntity;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class FetchData {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static List<CurrencyEntity> fetchRange(String symbol, int N) {
        String dateEnd = LocalDate.now().format(formatter);
        String dateStart = LocalDate.now().minusDays(N).format(formatter);

        String baseCurrency = Launcher.getBaseCurrency();
        String urlStr = String.format(
                "https://cmu.to/SE233currencyapi?base=%s&symbol=%s&start_date=%s&end_date=%s",baseCurrency, symbol, dateStart, dateEnd
        );

        List<CurrencyEntity> histList = new ArrayList<>();
        try {
            String retrievedJson = IOUtils.toString(new URL(urlStr), Charset.defaultCharset());
            JSONObject jsonOBJ = new JSONObject(retrievedJson).getJSONObject("rates");
            Iterator<String> keysToCopyIterator = jsonOBJ.keys();
            while (keysToCopyIterator.hasNext()) {
                String key = keysToCopyIterator.next();
                Double rate = Double.parseDouble(jsonOBJ.get(key).toString());
                histList.add(new CurrencyEntity(rate, key));
            }
            histList.sort(Comparator.comparing(CurrencyEntity::getTimestamp));
        } catch (MalformedURLException e) {
            System.err.println("Encounter a Malformed Url exception");
        } catch (IOException e) {
            System.err.println("Encounter an IO exception");
        }
        return histList;
    }
}