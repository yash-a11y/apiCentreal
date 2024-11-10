package com.example.apiCentreal;

import com.example.apiCentreal.pageRanking.PageRanking;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class centralservice {

    PageRanking ranking = new PageRanking();

    List<String> getRanking(String key)
    {
        try {
            return  ranking.getRanking(key.toLowerCase());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
