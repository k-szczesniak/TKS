package pl.ks.dk.tks;

import com.rabbitmq.client.*;
import lombok.extern.java.Log;
import pl.ks.dk.tks.domainmodel.users.Admin;
import pl.ks.dk.tks.domainmodel.users.Client;
import pl.ks.dk.tks.domainmodel.users.SuperUser;
import pl.ks.dk.tks.domainmodel.users.User;
import pl.ks.dk.tks.userinterface.UserUseCase;

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
    private Publisher publisher;

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
            log.warning("RentService: Init error: " + e.getMessage());
        }
    }

    @PreDestroy
    public void closeConnection() {
        try {
            connection.close();
        } catch (IOException e) {
            log.warning("RentService: Connection close error");
        }
    }

    private void bindQueueWithChannel() throws IOException {
        channel.queueBind(queueName, EXCHANGE_NAME, CREATE_ROUTING_KEY);
        channel.queueBind(queueName, EXCHANGE_NAME, UPDATE_ROUTING_KEY);
        channel.queueBind(queueName, EXCHANGE_NAME, DELETE_ROUTING_KEY);
    }

    private void getMessage() throws IOException {
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            switch (delivery.getEnvelope().getRoutingKey()) {
                case CREATE_ROUTING_KEY: {
                    try {
                        createUser(new String(delivery.getBody(), StandardCharsets.UTF_8));
                    } catch (Exception e) {
                        if (e.getMessage().contains("Login")) {
                            log.info("RentService: " + e.getMessage());
                        } else {
                            String login = getUserLogin(delivery);
                            log.info("RentService: Error creating user: " + login + ", sending remove message");
                            publisher.deleteUser(login);
                        }
                    }
                    break;
                }
                case UPDATE_ROUTING_KEY: {
                    try {
                        updateUser(new String(delivery.getBody(), StandardCharsets.UTF_8));
                    } catch (Exception e) {
                        log.info("RentService: There was an error updating the user");
                    }
                    break;
                }
                case DELETE_ROUTING_KEY: {
                    try {
                        removeUser(new String(delivery.getBody(), StandardCharsets.UTF_8));
                    } catch (Exception e) {
                        log.info("RentService: There was an error deleting the user");
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

    private void createUser(String message) throws ServiceException {
        log.info("RentService: Attempting to create user");
        User user = prepareUser(message);
        if (user != null) {
            userUseCase.addUser(user);
            log.info("RentService: User " + user.getLogin() + " has been added");
        } else {
            log.info("RentService: There was an error adding the user");
        }
    }

    private void updateUser(String message) throws ServiceException {
        log.info("RentService: Attempting to update user");
        User user = prepareUser(message);
        if (user != null) {
            user.setUuid(getUserUuid(message));
            userUseCase.updateUserByLogin(user, user.getLogin());
            log.info("RentService: User " + user.getLogin() + "has been updated");
        } else {
            log.info("RentService: There was an error updating the user");
        }
    }

    private void removeUser(String message) throws ServiceException {
        log.info("RentService: Removing user " + message);
        userUseCase.deleteUser(message);
    }

    private User prepareUser(String message) {
        JsonReader reader = Json.createReader(new StringReader(message));
        JsonObject jsonObject = reader.readObject();
        if (jsonObject.getString("role").equalsIgnoreCase("admin")) {
            return new Admin(jsonObject.getString("login"), jsonObject.getString("name"),
                    jsonObject.getString("surname"),
                    jsonObject.getString("password"), jsonObject.getString("role"));
        } else if (jsonObject.getString("role").equalsIgnoreCase("superUser")) {
            return new SuperUser(jsonObject.getString("login"), jsonObject.getString("name"),
                    jsonObject.getString("surname"),
                    jsonObject.getString("password"), jsonObject.getString("role"));
        } else if (jsonObject.getString("role").equalsIgnoreCase("client")) {
            return new Client(jsonObject.getString("login"), jsonObject.getString("name"),
                    jsonObject.getString("surname"),
                    jsonObject.getString("password"), jsonObject.getString("role"),
                    jsonObject.getInt("numberOfChildren"), jsonObject.getInt("ageOfTheYoungestChild"));
        }
        return null;
    }

    private String getUserUuid(String message) {
        JsonReader reader = Json.createReader(new StringReader(message));
        JsonObject jsonObject = reader.readObject();
        return userUseCase.getUserByLogin(jsonObject.getString("login")).getUuid();
    }

    private String getUserLogin(Delivery delivery) {
        JsonReader reader = Json
                .createReader(new StringReader(new String(delivery.getBody(), StandardCharsets.UTF_8)));
        JsonObject jsonObject = reader.readObject();
        String login = jsonObject.getString("login");
        return login;
    }
}
