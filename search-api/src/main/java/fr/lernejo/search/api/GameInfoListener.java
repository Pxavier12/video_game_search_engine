package fr.lernejo.search.api;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import java.io.File;
import java.nio.file.Files;


@Component
public class GameInfoListener {
    private final RestHighLevelClient client;
    public GameInfoListener(RestHighLevelClient client) {
        this.client = client;
    }
    @RabbitListener(queues = "game_info")
    void onMessage(String message) {
        try {
            final String infos = loadAsString("mapping.json");
            final String settings = loadAsString("settings.json");
            final boolean indexExists = client.indices().exists(new GetIndexRequest("games"), RequestOptions.DEFAULT);
            if(!indexExists){
                final CreateIndexRequest createIndexRequest = new CreateIndexRequest("games");
                createIndexRequest.settings(settings,XContentType.JSON);
                createIndexRequest.source(infos, XContentType.JSON);
                client.indices().create(createIndexRequest, RequestOptions.DEFAULT);}
            Request request = new Request("POST", "/games/infos/");
            request.setJsonEntity(message);
            client.getLowLevelClient().performRequest(request);
        } catch (Exception e) {throw new RuntimeException(e);}
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
