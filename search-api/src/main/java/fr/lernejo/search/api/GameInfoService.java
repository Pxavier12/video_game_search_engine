package fr.lernejo.search.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class GameInfoService {

    private ObjectMapper objectMapper;
    private RestHighLevelClient client;


    public GameInfoService(ObjectMapper objectMapper, RestHighLevelClient client) {
        this.objectMapper = objectMapper;
        this.client = client;
    }

    private List<MessagePojo> getSearchResult(SearchResponse response) {

        SearchHit[] searchHit = response.getHits().getHits();
        List<MessagePojo> gameInfos = new ArrayList<>();
        if (searchHit.length > 0) {
            Arrays.stream(searchHit)
                .forEach(hit -> gameInfos.add(objectMapper.convertValue(hit.getSourceAsMap(), MessagePojo.class)));
        }

        return gameInfos;
    }

    public List<MessagePojo> searchData(String query)  {
        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        QueryStringQueryBuilder queryBuilder = QueryBuilders.queryStringQuery(query)
            .field("title")
            .field("thumbnail")
            .field("short_description")
            .field("genre")
            .field("publisher")
            .field("developer")
            .field("release_date")
            .defaultOperator(Operator.AND);

        searchSourceBuilder.query(queryBuilder);
        searchRequest.source(searchSourceBuilder);

        SearchResponse response = null;
        try {
            response = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return getSearchResult(response);

    }
}
