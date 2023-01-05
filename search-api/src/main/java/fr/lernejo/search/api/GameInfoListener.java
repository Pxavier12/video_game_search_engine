package fr.lernejo.search.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

@Component
public class GameInfoListener {

    private RestHighLevelClient client;


    public GameInfoListener(RestHighLevelClient client) {
        this.client = client;
    }

    @RabbitListener(queues = AmqpConfiguration.GAME_INFO_QUEUE)
    void onMessage(String message) throws JsonProcessingException {
        try {
            final String infos = loadAsString("mapping.json");
            final String settings = loadAsString("settings.json");
            ObjectMapper mapper = new ObjectMapper();
            final boolean indexExists = client.indices().exists(new GetIndexRequest("games"), RequestOptions.DEFAULT);

            if(!indexExists){
                final CreateIndexRequest createIndexRequest = new CreateIndexRequest("games");
                createIndexRequest.settings(settings,XContentType.JSON);
                createIndexRequest.source(infos, XContentType.JSON);
                client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
            }

            Request request = new Request("POST", "/games/infos/");
            request.setJsonEntity(message);
            client.getLowLevelClient().performRequest(request);


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static String loadAsString(final String path) {
        try {
            final File resource = new ClassPathResource(path).getFile();

            return new String(Files.readAllBytes(resource.toPath()));
        } catch (final Exception e) {
            return null;
        }
    }
}
