package pl.ks.dk.tks;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
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
import javax.validation.ValidationException;
import javax.xml.rpc.ServiceException;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

@Log
@ApplicationScoped
public class Receiver {
    @Inject
    private UserUseCase userUseCase;

    @Inject
    private Publisher publisher;

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
                        log.info("RentService: Received create user message");
                        createUser(new String(delivery.getBody(), StandardCharsets.UTF_8));
                    } catch (ValidationException e) {
                        JsonReader reader = Json
                                .createReader(new StringReader(new String(delivery.getBody(), StandardCharsets.UTF_8)));
                        JsonObject jsonObject = reader.readObject();
                        String login = jsonObject.getString("login");
                        log.info(e.getMessage());
                        log.info("RentService:Exception when creating user, sending remove message with login: " +
                                login);
//                        publisher.removeUser(login);
                    } catch (Exception e) {
                        log.severe(e.getMessage());
                    }
                    break;
                }
                case UPDATE_USER_KEY: {
                    updateUser(new String(delivery.getBody(), StandardCharsets.UTF_8));
                    break;
                }
//                case REMOVE_USER_KEY: {
//                    removeUser(new String(delivery.getBody(), StandardCharsets.UTF_8));
//                }
                default: {
                    break;
                }
            }
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };
        channel.basicConsume(queueName, false, deliverCallback, consumerTag -> {
        });
    }

    private void createUser(String message) {
        log.info("RentService: Attempting to create user");
        User user = prepareUser(message);
        if (user != null) {
            try {
                userUseCase.addUser(user);
                log.info("RentService: User " + user.getLogin() + " has been added");
            } catch (ServiceException e) {
                log.info("RentService: There was an error adding the user");
            }
        } else {
            log.info("RentService: There was an error adding the user");
        }
    }

    private void updateUser(String message) {
        log.info("RentService: Attempting to update user");
        User user = prepareUser(message);
        if (user != null) {
            try {
                userUseCase.updateUserByLogin(user, user.getLogin());
                log.info("RentService: User " + user.getLogin() + "has been updated");
            } catch (ServiceException e) {
                log.info("RentService: There was an error updating the user");
            }
        } else {
            log.info("RentService: There was an error updating the user");
        }
    }

//    private void removeUser(String message) {
//        userService.removeUser(message);
//    }

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

    //TODO: PRYWATNA METODA DO POBIERANIA WARTOSCI ZE JSONA

}
