package com.example.apiCentreal;

import com.example.apiCentreal.pageRanking.PageRanking;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class centralservice {

    PageRanking ranking = new PageRanking();

    List<String> getRanking(String key)
    {
        return  ranking.getRanking(key.toLowerCase());
    }
}
