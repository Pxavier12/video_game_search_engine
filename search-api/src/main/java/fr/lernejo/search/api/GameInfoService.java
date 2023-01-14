package fr.lernejo.search.api;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;

@Service
public class GameInfoService {

    private final RestHighLevelClient restHighLevelClient;
    public GameInfoService(RestHighLevelClient restClient) {
        this.restHighLevelClient = restClient;
    }
   public Iterable<Game> searchData(String query)  {
       ArrayList hits = new ArrayList();
       SearchRequest rqst = new SearchRequest().source(SearchSourceBuilder.searchSource().query(new QueryStringQueryBuilder(query)));
       try {
           this.restHighLevelClient.search(rqst, RequestOptions.DEFAULT).getHits().forEach(hit -> hits.add(hit.getSourceAsMap()));
       } catch (IOException e) {
           throw new RuntimeException(e);
       }
       return hits;
    }
}

