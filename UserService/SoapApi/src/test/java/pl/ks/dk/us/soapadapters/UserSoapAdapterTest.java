package pl.ks.dk.us.soapadapters;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.ks.dk.model.*;
import pl.ks.dk.model.UserSoapAdapter;
import pl.ks.dk.model.Role;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Formatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class UserSoapAdapterTest {

    static final Logger LOGGER = LoggerFactory.getLogger(UserSoapAdapterTest.class);
    private final UserSoapAdapter userSoapAdapter;

    public UserSoapAdapterTest() throws MalformedURLException {
        Formatter formatter = new Formatter();
        String stringURL = formatter.format("http://localhost:%d/SOAP/UserAPI", soapServices.getMappedPort(8080)).toString();
        UserAPI userAPI = new UserAPI(new URL(stringURL));
        userSoapAdapter = userAPI.getUserSoapAdapterPort();
    }

    @Container
    private static final GenericContainer soapServices = new GenericContainer<>(
            new ImageFromDockerfile()
                    .withDockerfileFromBuilder(builder -> builder
                            .from("payara/server-full:5.2021.2-jdk11")
                            .copy("SOAP.war", "/opt/payara/deployments")
                            .build())
                    .withFileFromPath("SOAP.war", Path.of("target", "SOAP.war"))
    )
            .withExposedPorts(8080, 4848)
            .waitingFor(Wait.forHttp("/SOAP/UserAPI?wsdl").forPort(8080).forStatusCode(200))
            .withLogConsumer(new Slf4jLogConsumer(LOGGER));

    @Test
    void getAllUsersTest() {
        List<User> allUsers = userSoapAdapter.getAllUsers();
        assertEquals(7, allUsers.size());
        assertEquals("aAdamski", allUsers.get(0).getLogin());
    }

    @Test
    void getClientTest() {
        List<User> allUsers = userSoapAdapter.getAllUsers();
        String id = allUsers.get(0).getUuid();

        User user = null;
        try {
            user = userSoapAdapter.getUser(id);
        } catch (UserSoapException_Exception e) {
            e.printStackTrace();
        }

        assertNotNull(user);
        assertEquals(user.getLogin(), allUsers.get(0).getLogin());
    }

    @Test
    void getClientCountTest() {
        assertEquals(7, userSoapAdapter.getUserCount());
    }

    @Test
    void createClientTest() {
        User user = new User();
        user.setLogin("tClient");
        user.setName("testowy");
        user.setSurname("Client");
        user.setRole(Role.CLIENT);
        user.setPassword("testowy123");

        try {
            userSoapAdapter.createUser(user);
        } catch (UserSoapException_Exception e) {
            e.printStackTrace();
        }
        List<User> allUsers = userSoapAdapter.getAllUsers();
        assertEquals(user.getLogin(), allUsers.get(allUsers.size()-1).getLogin());
        assertEquals(user.getName(), allUsers.get(allUsers.size()-1).getName());
    }
}