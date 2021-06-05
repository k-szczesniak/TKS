import com.rabbitmq.client.*;
import lombok.extern.java.Log;
import pl.ks.dk.us.dtomodel.users.UserDTO;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

@Log
public class RpcClient {

    private static final String QUEUE_NAME = "rpc_queue";
    private Connection connection;
    private Channel channel;

    private String corrId;

    @PostConstruct
    public void init() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
        } catch (TimeoutException | IOException e) {
            log.warning(e.getMessage());
        }

        corrId = UUID.randomUUID().toString();
    }

    private String prepare(String message) {
        String replyQueueName = null;
        try {
            replyQueueName = channel.queueDeclare().getQueue();
        } catch (IOException e) {
            log.warning(e.getMessage());
        }

        AMQP.BasicProperties props = new AMQP.BasicProperties
                .Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .build();

        try {
            channel.basicPublish("", QUEUE_NAME, props, message.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            log.warning(e.getMessage());
        }
        return replyQueueName;
    }

    public UserDTO get(String username) {
        String replyQueueName = prepare("GET " + username);
        final BlockingQueue<UserDTO> response = new ArrayBlockingQueue<>(1);
        UserDTO result = null;
        try {
            String ctag = channel.basicConsume(replyQueueName, true, (consumerTag, delivery) -> {
//                if (delivery.getProperties().getCorrelationId().equals(corrId)) {
//                    response.offer(Objects.requireNonNull(Serialization.deserialize(delivery.getBody())));
//                }
            }, consumerTag -> {
            });
            result = response.take();
            channel.basicCancel(ctag);
        } catch (InterruptedException | IOException e) {
            log.warning(e.getMessage());
        }
        return result;
    }

//    public List<UserWeb> getAll() {
//        String replyQueueName = prepare("GET_ALL ");
//        final BlockingQueue<List<UserWeb>> response = new ArrayBlockingQueue<>(1);
//        List<UserWeb> result = null;
//        try {
//            String ctag = channel.basicConsume(replyQueueName, true, (consumerTag, delivery) -> {
//                if (delivery.getProperties().getCorrelationId().equals(corrId)) {
//                    response.offer(Objects.requireNonNull(deserializeUsers(delivery.getBody())));
//                }
//            }, consumerTag -> {
//            });
//            result = response.take();
//            channel.basicCancel(ctag);
//        } catch (InterruptedException | IOException e) {
//            log.error(e.getMessage());
//        }
//        return result;
//    }
//
//    public List<UserWeb> filter(String filter) {
//        String replyQueueName = prepare("FILTER " + filter);
//        final BlockingQueue<List<UserWeb>> response = new ArrayBlockingQueue<>(1);
//        List<UserWeb> result = null;
//        try {
//            String ctag = channel.basicConsume(replyQueueName, true, (consumerTag, delivery) -> {
//                if (delivery.getProperties().getCorrelationId().equals(corrId)) {
//                    response.offer(Objects.requireNonNull(deserializeUsers(delivery.getBody())));
//                }
//            }, consumerTag -> {
//            });
//            result = response.take();
//            channel.basicCancel(ctag);
//        } catch (InterruptedException | IOException e) {
//            log.error(e.getMessage());
//        }
//        return result;
//    }

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
