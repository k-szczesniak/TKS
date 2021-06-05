package pl.ks.dk.tks;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import lombok.extern.java.Log;
import pl.ks.dk.tks.domainmodel.users.User;
import pl.ks.dk.tks.userinterface.UserUseCase;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

import static pl.ks.dk.tks.Serialization.deserialize;

@Log
@ApplicationScoped
public class MqSubscriber {

    private static final String EXCHANGE_NAME = "user_exchange";
    private static final String BASE_BINDING_KEY = "user.";
    private Connection connection;
    private Channel channel;
    private String queueName;

    @Inject
    private UserUseCase userUseCase;

    private void performAction(User user, String routingKey) {
        switch (routingKey) {
            case "create":
                userUseCase.addUser(user);
                break;
            case "edit":
                userUseCase.updateUser(user, user.getUuid());
                break;
            default:
                break;
        }
    }

    private void init() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(8181);
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME, "topic");
            queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, EXCHANGE_NAME, BASE_BINDING_KEY + "*");
        } catch (TimeoutException | IOException e) {
            e.printStackTrace();
        }
    }

    public void subscribe(@Observes @Initialized(ApplicationScoped.class) Object init) {
        init();
        log.info("[*] Waiting for messages");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            User user = deserialize(delivery.getBody());
            log.warning("[x] Received " + delivery.getEnvelope().getRoutingKey() + ": " + user);
            String routingKey = delivery.getEnvelope().getRoutingKey().replace(BASE_BINDING_KEY, "");
            performAction(user, routingKey);
        };

        try {
            Objects.requireNonNull(channel).basicConsume(queueName, true, deliverCallback, consumerTag -> {
            });
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
