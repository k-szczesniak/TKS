package pl.ks.dk.tks;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
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
    private static final int PORT_NUMBER = 5672;
    private static final String USERNAME = "guest";
    private static final String PASSWORD = "guest";
    private static final String EXCHANGE_NAME = "users_exchange";
    private static final String EXCHANGE_TYPE = "topic";

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
            channel.addConfirmListener(confirmCallback,
                    (sequenceNumber, multiple) -> {
                        log.warning("RentService: Message number: " + sequenceNumber + " failed");
                    });
        } catch (Exception e) {
            log.warning("RentService: Init error: " + e.getMessage());
        }
    }

    ConfirmCallback confirmCallback = (sequenceNumber, multiple) -> {
        log.info("RentService: Message number: " + sequenceNumber + " successfully sent");
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
            log.warning("RentService: Connection close error");
        }
    }

    public void deleteUser(String login) throws IOException {
        log.info("RentService: Sending remove message");
        channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE);
        long sequenceNumber = channel.getNextPublishSeqNo();
        outstandingConfirms.put(sequenceNumber,login);
        channel.basicPublish(EXCHANGE_NAME, DELETE_ROUTING_KEY,null, login.getBytes(StandardCharsets.UTF_8));
    }
}
