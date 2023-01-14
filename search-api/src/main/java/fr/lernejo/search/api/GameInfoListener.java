package fr.lernejo.search.api;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class GameInfoListener {
    private final RestHighLevelClient restHighLevelClient;
    public GameInfoListener(RestHighLevelClient restClient) {
        this.restHighLevelClient = restClient;
    }

    @RabbitListener(queues = "game_info")
    public void onMessage(String message, @Header("game_id") String id) throws IOException {
        IndexRequest index = new IndexRequest("games");
        index.id(id);
        index.source(message, XContentType.JSON);
        this.restHighLevelClient.index(index, RequestOptions.DEFAULT);
    }

}
