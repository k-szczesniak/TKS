package pl.ks.dk.tks.soapadapters;

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
        String stringURL = formatter.format("http://localhost:%d/SOAP/ClientAPI", soapServices.getMappedPort(8080)).toString();
        ClientAPI clientAPI = new ClientAPI(new URL(stringURL));
        userSoapAdapter = clientAPI.getUserSoapAdapterPort();
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
            .waitingFor(Wait.forHttp("/SOAP/ClientAPI?wsdl").forPort(8080).forStatusCode(200))
            .withLogConsumer(new Slf4jLogConsumer(LOGGER));

    @Test
    void getAllUsersTest() {
        List<User> allUsers = userSoapAdapter.getAllUsers();
        assertEquals(9, allUsers.size());
        assertEquals("aAdamski", allUsers.get(0).getLogin());
    }

    @Test
    void getClientTest() {
        List<User> allUsers = userSoapAdapter.getAllUsers();
        String id = allUsers.get(0).getUuid();

        User user = null;
        try {
            user = userSoapAdapter.getClient(id);
        } catch (UserSoapException_Exception e) {
            e.printStackTrace();
        }

        assertNotNull(user);
        assertEquals(user.getLogin(), allUsers.get(0).getLogin());
    }

    @Test
    void getClientCountTest() {
        assertEquals(9, userSoapAdapter.getClientCount());
    }

    @Test
    void createAdminTest() {
        Admin admin = new Admin();
        admin.setLogin("tAdmin");
        admin.setName("testowy");
        admin.setSurname("Admin");
        admin.setRole("Admin");
        admin.setPassword("testowy123");

        try {
            userSoapAdapter.createAdmin(admin);
        } catch (UserSoapException_Exception e) {
            e.printStackTrace();
        }
        List<User> allUsers = userSoapAdapter.getAllUsers();
        assertEquals(admin.getLogin(), allUsers.get(allUsers.size()-1).getLogin());
        assertEquals(admin.getName(), allUsers.get(allUsers.size()-1).getName());
    }

    @Test
    void createSuperUserTest() {
        SuperUser superUser = new SuperUser();
        superUser.setLogin("tSuperUser");
        superUser.setName("testowy");
        superUser.setSurname("SuperUser");
        superUser.setRole("SuperUser");
        superUser.setPassword("testowy123");

        try {
            userSoapAdapter.createSuperUser(superUser);
        } catch (UserSoapException_Exception e) {
            e.printStackTrace();
        }
        List<User> allUsers = userSoapAdapter.getAllUsers();
        assertEquals(superUser.getLogin(), allUsers.get(allUsers.size()-1).getLogin());
        assertEquals(superUser.getName(), allUsers.get(allUsers.size()-1).getName());
    }

    @Test
    void createClientTest() {
        Client client = new Client();
        client.setLogin("tClient");
        client.setName("testowy");
        client.setSurname("Client");
        client.setRole("Client");
        client.setPassword("testowy123");

        try {
            userSoapAdapter.createClient(client);
        } catch (UserSoapException_Exception e) {
            e.printStackTrace();
        }
        List<User> allUsers = userSoapAdapter.getAllUsers();
        assertEquals(client.getLogin(), allUsers.get(allUsers.size()-1).getLogin());
        assertEquals(client.getName(), allUsers.get(allUsers.size()-1).getName());
    }
}