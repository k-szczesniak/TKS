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
    private static final String EXCHANGE_NAME = "exchange_topic";
    private static final String EXCHANGE_TYPE = "topic";

    private static final String REMOVE_USER_KEY = "user.remove";

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
                        log.severe("RentService: Message number: " + sequenceNumber + " failed");
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ConfirmCallback cleanOutstandingConfirms = (sequenceNumber, multiple) -> {
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
            e.printStackTrace();
        }
    }

    public void removeUser(String login) throws IOException {
        log.info("RentService: Sending remove message");
        channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE);
        long sequenceNumber = channel.getNextPublishSeqNo();
        outstandingConfirms.put(sequenceNumber,login);
        channel.basicPublish(EXCHANGE_NAME, REMOVE_USER_KEY,null, login.getBytes(StandardCharsets.UTF_8));
    }
}
