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
import pl.ks.dk.model.BabysitterSoapAdapter;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Formatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class BabysitterSoapAdapterTest {
    static final Logger LOGGER = LoggerFactory.getLogger(BabysitterSoapAdapterTest.class);
    private final BabysitterSoapAdapter babysitterSoapAdapter;

    public BabysitterSoapAdapterTest() throws MalformedURLException {
        Formatter formatter = new Formatter();
        String stringURL = formatter.format("http://localhost:%d/SOAP/BabysitterAPI", soapServices.getMappedPort(8080)).toString();
        BabysitterAPI babysitterAPI = new BabysitterAPI(new URL(stringURL));
        babysitterSoapAdapter = babysitterAPI.getBabysitterSoapAdapterPort();
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
            .waitingFor(Wait.forHttp("/SOAP/BabysitterAPI?wsdl").forPort(8080).forStatusCode(200))
            .withLogConsumer(new Slf4jLogConsumer(LOGGER));

    @Test
    void getAllBabysitters() {
        List<Babysitter> allBabysitters = babysitterSoapAdapter.getAllBabysitters();
        assertEquals(8, allBabysitters.size());
        assertEquals("Anna", allBabysitters.get(0).getName());
    }

    @Test
    void getBabysitterTest() {
        List<Babysitter> allBabysitters = babysitterSoapAdapter.getAllBabysitters();
        String id = allBabysitters.get(0).getUuid();

        Babysitter babysitter = null;
        try {
            babysitter = babysitterSoapAdapter.getBabysitter(id);
        } catch (BabysitterSoapException_Exception e) {
            e.printStackTrace();
        }

        assertNotNull(babysitter);
        assertEquals(babysitter.getName(), allBabysitters.get(0).getName());
    }

    @Test
    void createBabysitterTest() {
        Babysitter babysitter = new Babysitter();
        babysitter.setName("Ala");
        babysitter.setSurname("Ula");
        babysitter.setEmployed(false);
        babysitter.setBasePriceForHour(25.0);
        babysitter.setMaxNumberOfChildrenInTheFamily(4);
        babysitter.setMinChildAge(5);

        try {
            babysitterSoapAdapter.createBabysitter(babysitter);
        } catch (BabysitterSoapException_Exception e) {
            e.printStackTrace();
        }
        List<Babysitter> allBabysitter = babysitterSoapAdapter.getAllBabysitters();
        assertEquals(babysitter.getBasePriceForHour(), allBabysitter.get(allBabysitter.size()-1).getBasePriceForHour());
        assertEquals(babysitter.getName(), allBabysitter.get(allBabysitter.size()-1).getName());
    }

    @Test
    void createTeachingSitterTest() {
        TeachingSitter teachingSitter = new TeachingSitter();
        teachingSitter.setName("Ala");
        teachingSitter.setSurname("Ula");
        teachingSitter.setEmployed(false);
        teachingSitter.setBasePriceForHour(25.0);
        teachingSitter.setMaxNumberOfChildrenInTheFamily(4);
        teachingSitter.setMinChildAge(5);
        teachingSitter.setYearsOfExperienceInTeaching(20);

        try {
            babysitterSoapAdapter.createTeachingSitter(teachingSitter);
        } catch (BabysitterSoapException_Exception e) {
            e.printStackTrace();
        }
        List<Babysitter> allBabysitter = babysitterSoapAdapter.getAllBabysitters();
        assertEquals(teachingSitter.getMaxNumberOfChildrenInTheFamily(), allBabysitter.get(allBabysitter.size()-1).getMaxNumberOfChildrenInTheFamily());
        assertEquals(teachingSitter.getName(), allBabysitter.get(allBabysitter.size()-1).getName());
    }

    @Test
    void createTidingSitterTest() {
        TidingSitter tidingSitter = new TidingSitter();
        tidingSitter.setName("Ala");
        tidingSitter.setSurname("Ula");
        tidingSitter.setEmployed(false);
        tidingSitter.setBasePriceForHour(25.0);
        tidingSitter.setMaxNumberOfChildrenInTheFamily(4);
        tidingSitter.setMinChildAge(5);
        tidingSitter.setValueOfCleaningEquipment(120.0);

        try {
            babysitterSoapAdapter.createTidingSitter(tidingSitter);
        } catch (BabysitterSoapException_Exception e) {
            e.printStackTrace();
        }
        List<Babysitter> allBabysitter = babysitterSoapAdapter.getAllBabysitters();
        assertEquals(tidingSitter.getMaxNumberOfChildrenInTheFamily(), allBabysitter.get(allBabysitter.size()-1).getMaxNumberOfChildrenInTheFamily());
        assertEquals(tidingSitter.getName(), allBabysitter.get(allBabysitter.size()-1).getName());
    }
}