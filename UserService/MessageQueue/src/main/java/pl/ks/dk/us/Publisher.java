package pl.ks.dk.us;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.java.Log;
import pl.ks.dk.us.users.User;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

@Log
@ApplicationScoped
public class Publisher {

    private static final String HOST_NAME = "localhost";
    private static final int PORT_NUMBER = 5672;
    private static final String USERNAME = "guest";
    private static final String PASSWORD = "guest";
    private static final String EXCHANGE_NAME = "users_exchange";
    private static final String EXCHANGE_TYPE = "topic";

    private static final String CREATE_ROUTING_KEY = "user.create";
    private static final String UPDATE_ROUTING_KEY = "user.update";
    private static final String DELETE_ROUTING_KEY = "user.delete";

    private ConnectionFactory connectionFactory;
    private Connection connection;
    private Channel channel;
    ConcurrentNavigableMap<Long, String> outstandingConfirms = new ConcurrentSkipListMap<>();

    @PostConstruct
    public void init(@Observes @Initialized(ApplicationScoped.class) Object init) {
        try {
            connectionFactory = new ConnectionFactory();
            connectionFactory.setHost(HOST_NAME);
            connectionFactory.setPort(PORT_NUMBER);
            connectionFactory.setUsername(USERNAME);
            connectionFactory.setPassword(PASSWORD);
            connection = connectionFactory.newConnection();
            channel = connection.createChannel();
            channel.confirmSelect();
            channel.addConfirmListener(cleanOutstandingConfirms, handleNack);
        } catch (Exception e) {
            log.warning("UserService: Init error: " + e.getMessage());
        }
    }

    ConfirmCallback cleanOutstandingConfirms = (sequenceNumber, multiple) -> {
        log.info("UserService: Message number: " + sequenceNumber + " successfully sent");
        if (multiple) {
            ConcurrentNavigableMap<Long, String> confirmed = outstandingConfirms.headMap(
                    sequenceNumber, true
            );
            confirmed.clear();
        } else {
            outstandingConfirms.remove(sequenceNumber);
        }
    };

    ConfirmCallback handleNack = (sequenceNumber, multiple) -> {
        log.warning("UserService: Message number: " + sequenceNumber + " failed");
        cleanOutstandingConfirms.handle(sequenceNumber, multiple);
    };

    @PreDestroy
    public void closeConnection() {
        try {
            connection.close();
        } catch (IOException e) {
            log.warning("UserService: Connection close error");
        }
    }

    public void createUser(String json) throws IOException {
        log.info("UserService: Sending create message");
        channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE);
        long sequenceNumber = channel.getNextPublishSeqNo();
        outstandingConfirms.put(sequenceNumber, json);
        channel.basicPublish(EXCHANGE_NAME, CREATE_ROUTING_KEY, null, json.getBytes(StandardCharsets.UTF_8));
    }

    public void updateUser(String json) throws IOException {
        log.info("UserService: Sending update message");
        channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE);
        long sequenceNumber = channel.getNextPublishSeqNo();
        outstandingConfirms.put(sequenceNumber, json);
        channel.basicPublish(EXCHANGE_NAME, UPDATE_ROUTING_KEY, null, json.getBytes(StandardCharsets.UTF_8));
    }

    public void deleteUser(String login) throws IOException {
        log.info("UserService: Sending remove message");
        channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE);
        long sequenceNumber = channel.getNextPublishSeqNo();
        outstandingConfirms.put(sequenceNumber, login);
        channel.basicPublish(EXCHANGE_NAME, DELETE_ROUTING_KEY, null, login.getBytes(StandardCharsets.UTF_8));
    }

    public static class Serialization {
        private static JsonObjectBuilder prepareUser(User source) {
            return Json.createObjectBuilder()
                    .add("login", source.getLogin())
                    .add("name", source.getName())
                    .add("surname", source.getSurname())
                    .add("password", source.getPassword())
                    .add("role", source.getRole());
        }

        public static String userToJsonString(User source) {
            return prepareUser(source).build().toString();
        }

        public static String clientToJsonString(User source, int numberOfChildren, int ageOfTheYoungestChild) {
            JsonObjectBuilder objectBuilder = prepareUser(source);
            return objectBuilder.add("numberOfChildren", numberOfChildren)
                    .add("ageOfTheYoungestChild", ageOfTheYoungestChild)
                    .build().toString();
        }
    }
}
