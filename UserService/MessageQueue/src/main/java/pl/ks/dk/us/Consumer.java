package pl.ks.dk.us;

import com.rabbitmq.client.*;
import lombok.extern.java.Log;
import pl.ks.dk.us.userinterface.UserUseCase;
import pl.ks.dk.us.users.User;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.xml.rpc.ServiceException;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

@Log
@ApplicationScoped
public class Consumer {
    @Inject
    private UserUseCase userUseCase;

    @Inject
    Publisher publisher;

    private static final String HOST_NAME = "localhost";
    private static final String EXCHANGE_NAME = "exchange_topic";
    private static final String EXCHANGE_TYPE = "topic";

    private static final String CREATE_USER_KEY = "user.create";
    private static final String UPDATE_USER_KEY = "user.update";
    private static final String REMOVE_USER_KEY = "user.remove";

    private ConnectionFactory connectionFactory;
    private Connection connection;
    private Channel channel;
    private String queueName;

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
            channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE);
            channel.basicQos(1);
            queueName = channel.queueDeclare().getQueue();
            bindKeys();
            getMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void closeConnection() {
        try {
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void bindKeys() throws IOException {
        channel.queueBind(queueName, EXCHANGE_NAME, CREATE_USER_KEY);
        channel.queueBind(queueName, EXCHANGE_NAME, UPDATE_USER_KEY);
        channel.queueBind(queueName, EXCHANGE_NAME, REMOVE_USER_KEY);
    }

    private void getMessage() throws IOException {
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            switch (delivery.getEnvelope().getRoutingKey()) {
                case CREATE_USER_KEY: {
                    try {
                        createUser(new String(delivery.getBody(), StandardCharsets.UTF_8));
                    } catch (Exception e) {
                        String login = getUserLogin(delivery);
                        log.info("UserService: Error creating user: " + login + ", sending remove message");
                        publisher.removeUser(login);
                    }
                    break;
                }
                case UPDATE_USER_KEY: {
                    updateUser(new String(delivery.getBody(), StandardCharsets.UTF_8));
                    break;
                }
                case REMOVE_USER_KEY: {
                    log.info("UserService: Received remove message");
                    try {
                        removeUser(new String(delivery.getBody(), StandardCharsets.UTF_8));
                    } catch (Exception e) {
                        log.info("UserService: There was an error deleting the user");
                    }
                }
                default: {
                    break;
                }
            }
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };
        channel.basicConsume(queueName, false, deliverCallback, consumerTag -> {
        });
    }

    private void createUser(String message) throws MessageQueueException {
        log.info("UserService: Attempting to create user");
        User user = prepareUser(message);
        try {
            userUseCase.addUser(user);
            log.info("UserService: User " + user.getLogin() + " has been added");
        } catch (ServiceException e) {
            throw new MessageQueueException("UserService: There was an error adding the user");
        }
    }

    private void updateUser(String message) {
        log.info("UserService: Attempting to update user");
        User user = prepareUser(message);
        try {
            userUseCase.updateUserByLogin(user, user.getLogin());
            log.info("UserService: User " + user.getLogin() + " has been updated");
        } catch (ServiceException e) {
            log.info("UserService: There was an error updating the user");
        }
    }

    private void removeUser(String message) throws MessageQueueException {
        log.info("UserService: Removing user " + message);
        try {
            userUseCase.deleteUser(message);
        } catch (ServiceException e) {
            throw new MessageQueueException("UserService: There was an error deleting the user");
        }
    }

    private User prepareUser(String message) {
        JsonReader reader = Json.createReader(new StringReader(message));
        JsonObject jsonObject = reader.readObject();
        return new User(jsonObject.getString("login"), jsonObject.getString("name"), jsonObject.getString("surname"),
                jsonObject.getString("password"), jsonObject.getString("role"));
    }

    private String getUserLogin(Delivery delivery) {
        JsonReader reader = Json.createReader(new StringReader(
                new String(delivery.getBody(), StandardCharsets.UTF_8)));
        JsonObject jsonObject = reader.readObject();
        String login = jsonObject.getString("login");
        return login;
    }
}
