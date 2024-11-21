package com.example.apiCentreal.pageRanking;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.cache.annotation.Cacheable;

import java.io.IOException;
import java.time.Duration;
import java.util.*;

public class PageRanking {

    public static class KeywordMatcher {
        private Map<Character, Integer> badCharSkip;
        private String keyword;

        public KeywordMatcher(String keyword) {
            this.keyword = keyword;
            this.badCharSkip = new HashMap<>();
            for (int j = 0; j < keyword.length(); j++) {
                badCharSkip.put(keyword.charAt(j), j);
            }
        }

        public int search(String text) {
            int patternLength = keyword.length();
            int textLength = text.length();
            int skip;
            for (int i = 0; i <= textLength - patternLength; i += skip) {
                skip = 0;
                for (int j = patternLength - 1; j >= 0; j--) {
                    char currentChar = text.charAt(i + j);
                    if (keyword.charAt(j) != currentChar) {
                        skip = Math.max(1, j - badCharSkip.getOrDefault(currentChar, -1));
                        break;
                    }
                }
                if (skip == 0) return i;
            }
            return textLength;
        }
    }

    public static int countOccurrences(String text, String keyword) {
        KeywordMatcher matcher = new KeywordMatcher(keyword);
        int count = 0;
        int offset = 0;
        while (offset <= text.length() - keyword.length()) {
            int matchIndex = matcher.search(text.substring(offset));
            if (matchIndex == text.length() - offset) break;
            count++;
            offset += matchIndex + keyword.length();
        }
        return count;
    }

    public static void quickSort(List<Map.Entry<String, Integer>> list, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(list, low, high);
            quickSort(list, low, pivotIndex - 1);
            quickSort(list, pivotIndex + 1, high);
        }
    }

    private static int partition(List<Map.Entry<String, Integer>> list, int low, int high) {
        Map.Entry<String, Integer> pivot = list.get(high);
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (list.get(j).getValue() > pivot.getValue()) {
                i++;
                Collections.swap(list, i, j);
            }
        }
        Collections.swap(list, i + 1, high);
        return i + 1;
    }

    public static List<Map.Entry<String, Integer>> rankPages(Map<String, String> pages, String keyword) {
        Map<String, Integer> pageFrequencyMap = new HashMap<>();
        for (Map.Entry<String, String> entry : pages.entrySet()) {
            String pageName = entry.getKey();
            String content = entry.getValue();
            int frequency = countOccurrences(content, keyword);
            pageFrequencyMap.put(pageName, frequency);
        }
        List<Map.Entry<String, Integer>> sortedPages = new ArrayList<>(pageFrequencyMap.entrySet());
        quickSort(sortedPages, 0, sortedPages.size() - 1);
        return sortedPages;
    }

    @Cacheable(value = "scrapedData", key = "#someKey")
    public ArrayList<String> getRanking(String keyword) throws IOException {
        ArrayList<String> ranklist = new ArrayList<>();
        Map<String, String> pageContents = new HashMap<>();
        String[] urls = {
                "https://www.fido.ca/phones/bring-your-own-device?icid=F_WIR_CNV_GRM6LG&flowType=byod",
                "https://www.rogers.com/plans?icid=R_WIR_CMH_6WMCMZ",
                "https://www.virginplus.ca/en/plans/postpaid.html#!/?rate=BYOP",
                "https://www.telus.com/en/mobility/plans"
        };
        String[] filenames = {"fido.txt", "rogers.txt", "virgin.txt", "telus.txt"};

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        try {
            for (int i = 0; i < urls.length; i++) {
                String url = urls[i];
                String fileName = filenames[i];
                String content = FileUtil.loadPageFromFile(url, fileName);

                if (content == null || content.trim().equalsIgnoreCase("undefined")) {
                    content = scrapeContent(driver, wait, url);
                    FileUtil.savePageToFile(url, content, fileName);
                }

                pageContents.put(url, content);
            }
        } finally {
            driver.quit();
        }

        List<Map.Entry<String, Integer>> rankedPages = rankPages(pageContents, keyword);
        for (Map.Entry<String, Integer> entry : rankedPages) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + " occurrences");
            ranklist.add(entry.getKey() + ": " + entry.getValue() + " occurrences");
        }
        return ranklist;
    }

    private String scrapeContent(WebDriver driver, WebDriverWait wait, String url) {
        int retryCount = 3;
        while (retryCount > 0) {
            try {
                driver.get(url);
                WebElement contentElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body")));
                return contentElement.getText().toLowerCase();
            } catch (TimeoutException e) {
                retryCount--;
                if (retryCount == 0) throw e;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        throw new RuntimeException("Failed to scrape content after 3 retries");
    }
}