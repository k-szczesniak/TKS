package pl.ks.dk.us;

import com.rabbitmq.client.*;
import lombok.extern.java.Log;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

@Log
@ApplicationScoped
public class Publisher {
    private static final String HOST_NAME = "localhost";
    private static final String EXCHANGE_NAME = "exchange_topic";
    private static final String EXCHANGE_TYPE = "topic";

    private static final String CREATE_USER_KEY = "user.create";
//    private static final String REMOVE_USER_KEY = "user.remove";
//    private static final String UPDATE_USER_KEY = "user.update";

    private ConnectionFactory connectionFactory;
    private Connection connection;
    private Channel channel;
    ConcurrentNavigableMap<Long, String> outstandingConfirms = new ConcurrentSkipListMap<>();

    @PostConstruct
    public void init(@Observes @Initialized(ApplicationScoped.class) Object init) {
        try {
            connectionFactory = new ConnectionFactory();
            connectionFactory.setHost(HOST_NAME);
            connectionFactory.setPort(5672);
            connectionFactory.setUsername("guest");
            connectionFactory.setPassword("guest");
            connection = connectionFactory.newConnection();
            channel = connection.createChannel();
            channel.confirmSelect();
            channel.addConfirmListener(cleanOutstandingConfirms,
                    (sequenceNumber, multiple) -> {
                        log.severe("UserService: Message number: " + sequenceNumber + " failed");
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ConfirmCallback cleanOutstandingConfirms = (sequenceNumber, multiple) -> {
        log.info("UserService: Message number: " + sequenceNumber + " success");
        if (multiple) {
            ConcurrentNavigableMap<Long, String> confirmed = outstandingConfirms.headMap(
                    sequenceNumber, true
            );
            confirmed.clear();
        } else {
            outstandingConfirms.remove(sequenceNumber);
        }
    };

    @PreDestroy
    public void closeConnection() {
        try {
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createUser(String json) throws IOException{
        channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE);
        long sequenceNumber = channel.getNextPublishSeqNo();
        outstandingConfirms.put(sequenceNumber,json);
        channel.basicPublish(EXCHANGE_NAME, CREATE_USER_KEY,null, json.getBytes(StandardCharsets.UTF_8));
    }

//    public void removeUser(String login) throws IOException {
//        log.info("Hotel: Sending remove message");
//        channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE);
//        long sequenceNumber = channel.getNextPublishSeqNo();
//        outstandingConfirms.put(sequenceNumber,login);
//        channel.basicPublish(EXCHANGE_NAME, REMOVE_USER_KEY,null, login.getBytes(StandardCharsets.UTF_8));

//    }
//
//    public void updateUser(String json) throws IOException {
//        channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE);
//        channel.basicPublish(EXCHANGE_NAME, UPDATE_USER_KEY, null, json.getBytes(StandardCharsets.UTF_8));
//    }
}
