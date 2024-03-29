package fr.lernejo.fileinjector;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class Launcher {
    public static void main(String[] args) throws IOException {
        try (AbstractApplicationContext springContext = new AnnotationConfigApplicationContext(Launcher.class)) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                List<InfoPojo> messages = Arrays.asList(mapper.readValue(Paths.get(args[0]).toFile(), InfoPojo[].class));
                RabbitTemplate sender = springContext.getBean(RabbitTemplate.class);
                messages.forEach(
                    (message)-> {
                        //sender.convertAndSend("game_info", mapper.writeValueAsString(message));
                        sender.setMessageConverter(new Jackson2JsonMessageConverter());
                        sender.convertAndSend("", "game_info", message, msg -> {
                            msg.getMessageProperties().getHeaders().put("game_id", message.id());
                            return msg;
                        });
                    }
                );
            } catch (Exception ex) {System.out.println(ex);}
        }
    }
}
