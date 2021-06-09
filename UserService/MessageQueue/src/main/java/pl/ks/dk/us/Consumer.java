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
    private String queueName;

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
            channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE);
            channel.basicQos(1);
            queueName = channel.queueDeclare().getQueue();
            bindQueueWithChannel();
            getMessage();
        } catch (Exception e) {
            log.warning("UserService: Init error: " + e.getMessage());
        }
    }

    @PreDestroy
    public void closeConnection() {
        try {
            connection.close();
        } catch (IOException e) {
            log.warning("UserService: Connection close error");
        }
    }

    private void bindQueueWithChannel() throws IOException {
        channel.queueBind(queueName, EXCHANGE_NAME, CREATE_ROUTING_KEY);
        channel.queueBind(queueName, EXCHANGE_NAME, UPDATE_ROUTING_KEY);
        channel.queueBind(queueName, EXCHANGE_NAME, DELETE_ROUTING_KEY);
    }

    private void getMessage() throws IOException {
        channel.basicConsume(queueName, false, deliverCallback, cancelCallback -> {
        });
    }

    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
        switch (delivery.getEnvelope().getRoutingKey()) {
            case CREATE_ROUTING_KEY: {
                try {
                    createUser(new String(delivery.getBody(), StandardCharsets.UTF_8));
                } catch (Exception e) {
                    if (e.getMessage().contains("Login")) {
                        log.info("UserService: " + e.getMessage());
                    } else {
                        String login = getUserLogin(delivery);
                        log.info("UserService: Error creating user: " + login + ", sending remove message");
                        publisher.deleteUser(login);
                    }
                }
                break;
            }
            case UPDATE_ROUTING_KEY: {
                try {
                    updateUser(new String(delivery.getBody(), StandardCharsets.UTF_8));
                } catch (Exception e) {
                    log.info("UserService: There was an error updating the user");
                }
                break;
            }
            case DELETE_ROUTING_KEY: {
                log.info("UserService: Received remove message");
                try {
                    deleteUser(new String(delivery.getBody(), StandardCharsets.UTF_8));
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

    private void createUser(String message) throws ServiceException {
        log.info("UserService: Attempting to create user");
        User user = prepareUser(message);
        userUseCase.addUser(user);
        log.info("UserService: User " + user.getLogin() + " has been added");
    }

    private void updateUser(String message) throws ServiceException {
        log.info("UserService: Attempting to update user");
        User user = prepareUser(message);
        user.setUuid(getUserUuid(message));
        userUseCase.updateUserByLogin(user, user.getLogin());
        log.info("UserService: User " + user.getLogin() + " has been updated");
    }

    private void deleteUser(String message) throws ServiceException {
        log.info("UserService: Removing user " + message);
        userUseCase.deleteUser(message);
    }

    private User prepareUser(String message) {
        JsonReader reader = Json.createReader(new StringReader(message));
        JsonObject jsonObject = reader.readObject();
        return new User(jsonObject.getString("login"), jsonObject.getString("name"), jsonObject.getString("surname"),
                jsonObject.getString("password"), jsonObject.getString("role"));
    }

    private String getUserUuid(String message) {
        JsonReader reader = Json.createReader(new StringReader(message));
        JsonObject jsonObject = reader.readObject();
        return userUseCase.getUserByLogin(jsonObject.getString("login")).getUuid();
    }

    private String getUserLogin(Delivery delivery) {
        JsonReader reader = Json.createReader(new StringReader(
                new String(delivery.getBody(), StandardCharsets.UTF_8)));
        JsonObject jsonObject = reader.readObject();
        String login = jsonObject.getString("login");
        return login;
    }
}
