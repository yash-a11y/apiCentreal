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

    // Class to perform Boyer-Moore keyword search
    public static class KeywordMatcher {
        private Map<Character, Integer> badCharSkip;
        private String keyword;

        public KeywordMatcher(String keyword) {
            this.keyword = keyword;
            this.badCharSkip = new HashMap<>();

            // Fill map with the rightmost position of each character in the pattern
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
                        // Use badCharSkip.getOrDefault to handle characters not in the pattern
                        skip = Math.max(1, j - badCharSkip.getOrDefault(currentChar, -1));
                        break;
                    }
                }
                if (skip == 0) return i; // Pattern found
            }
            return textLength; // Pattern not found
        }
    }

    // Count occurrences of the keyword in the page content
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

    // Quick Sort implementation for sorting entries by value in descending order
    public static void quickSort(List<Map.Entry<String, Integer>> list, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(list, low, high);
            quickSort(list, low, pivotIndex - 1);
            quickSort(list, pivotIndex + 1, high);
        }
    }

    // Partition method for Quick Sort
    private static int partition(List<Map.Entry<String, Integer>> list, int low, int high) {
        Map.Entry<String, Integer> pivot = list.get(high);  // pivot element
        int i = low - 1;

        for (int j = low; j < high; j++) {
            // Modify condition to sort in descending order
            if (list.get(j).getValue() > pivot.getValue()) {
                i++;
                Collections.swap(list, i, j);
            }
        }
        Collections.swap(list, i + 1, high);  // Move pivot element to the correct position
        return i + 1;
    }

    // Rank Pages based on keyword frequency

    public static List<Map.Entry<String, Integer>> rankPages(Map<String, String> pages, String keyword) {
        Map<String, Integer> pageFrequencyMap = new HashMap<>();

        // Count occurrences of keyword in each page
        for (Map.Entry<String, String> entry : pages.entrySet()) {
            String pageName = entry.getKey();
            String content = entry.getValue();
            int frequency = countOccurrences(content, keyword);
            pageFrequencyMap.put(pageName, frequency);

//            // Debug: Print the frequency for each page
//            System.out.println("Page: " + pageName + " - Keyword Frequency: " + frequency);
        }

        // Convert Map to List of Map entries
        List<Map.Entry<String, Integer>> sortedPages = new ArrayList<>(pageFrequencyMap.entrySet());

        // Use quick sort to sort the pages by frequency in descending order
        quickSort(sortedPages, 0, sortedPages.size() - 1);

//        // Debug: Print the sorted pages
//        System.out.println("\nSorted pages after quick sort:");
//        for (Map.Entry<String, Integer> entry : sortedPages) {
//            System.out.println(entry.getKey() + ": " + entry.getValue() + " occurrences");
//        }

        // Return sorted list
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

        String[] filenames = {
                "fido.txt","rogers.txt","virgin.txt","telus.txt"
        };

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");

        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        try {
            // Loop through each URL

            int i = 0;
            for (String url : urls) {
                String fileName = filenames[i];
                String content = FileUtil.loadPageFromFile(url, fileName);  // Check if data is cached in a file

                // If content is not cached, perform scraping
                if(url == urls[1]){

                    if (content == null) {
                        driver.get(url);


                        int retryCount = 3;
                        while (retryCount > 0) {
                            try {
                                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ds-tabs-0-tabPanel-0")));
                                WebElement contentElement = driver.findElement(By.tagName("body"));

                                content = contentElement.getText().toLowerCase();
                                break;
                            } catch (TimeoutException e) {
                                retryCount--;
                                if (retryCount == 0) throw e; // Rethrow if all retries fail
                                try {
                                    Thread.sleep(1000); // Delay between retries
                                } catch (InterruptedException ex) {
                                    throw new RuntimeException(ex);
                                }
                            }
                        }


                        FileUtil.savePageToFile(url, content, fileName);  // Save the scraped data


                    }

                }else {
                    if (content == null) {
                        driver.get(url);


                        int retryCount = 3;
                        while (retryCount > 0) {
                            try {
                                WebElement contentElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body")));
                                content = contentElement.getText().toLowerCase();
                                break;
                            } catch (TimeoutException e) {
                                retryCount--;
                                if (retryCount == 0) throw e; // Rethrow if all retries fail
                                try {
                                    Thread.sleep(1000); // Delay between retries
                                } catch (InterruptedException ex) {
                                    throw new RuntimeException(ex);
                                }
                            }
                        }


                        FileUtil.savePageToFile(url, content, fileName);  // Save the scraped data


                    }
                }

                pageContents.put(url, content);  // Add content to the map for ranking

                i++;
            }
        } finally {

            driver.quit();
        }

        // Rank pages based on keyword frequency
        List<Map.Entry<String, Integer>> rankedPages = rankPages(pageContents, keyword);

        for (Map.Entry<String, Integer> entry : rankedPages) {
            System.out.println(entry.getKey() + "," + entry.getValue());
            ranklist.add(entry.getKey() + "," + entry.getValue());
        }

        return ranklist;
    }
}

