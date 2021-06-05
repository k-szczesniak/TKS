import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.java.Log;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.TimeoutException;

@Log
public class Publisher {
    private static final String EXCHANGE_NAME = "user_exchange";
    private Connection connection;
    private Channel channel;

    @PostConstruct
    public void init() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.confirmSelect();
            channel.addConfirmListener(
                    (sequenceNumber, multiple) -> log.info("[x] Message has been ack-ed"),
                    (sequenceNumber, multiple) -> log.info("[x] Message has been nack-ed")
            );
            channel.exchangeDeclare(EXCHANGE_NAME, "topic");
        } catch (TimeoutException | IOException e) {
            log.warning(e.getMessage());
        }
    }

    public void publish(Serializable source, String routingKey) {
        try {
            channel.basicPublish(EXCHANGE_NAME, routingKey ,null, Serialization.serialize(source));
        } catch (IOException e) {
            log.warning(e.getMessage());
        }
    }

    @PreDestroy
    public void dispose() {
        try {
            channel.close();
            connection.close();
        } catch (TimeoutException | IOException e) {
            log.warning(e.getMessage());
        }
    }
}
