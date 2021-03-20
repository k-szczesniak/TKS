package pl.ks.dk.tks.services;

import pl.ks.dk.tks.domainmodel.babysitters.Babysitter;
import pl.ks.dk.tks.infrastructure.babysitters.AddBabysitterPort;
import pl.ks.dk.tks.infrastructure.babysitters.DeleteBabysitterPort;
import pl.ks.dk.tks.userinterface.BabysitterUseCase;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

//TODO: OCZYSCIC REPO I SERWISY Z ZBEDNYCH METOD

@ApplicationScoped
public class BabysittersService implements BabysitterUseCase {

    @Inject
    private DeleteBabysitterPort deleteBabysitterPort;

    @Inject
    private AddBabysitterPort addBabysitterPort;


    public void addBabysitter(Babysitter babysitter) {
        addBabysitterPort.addBabysitter(babysitter);
    }


    ////////////////////////////////////////////////////////////

    @Inject
    private BabysittersRepository babysittersRepository;

    @Inject
    private EmploymentsService employmentsService;

    private List<Babysitter> currentBabysitters;

    public List<Babysitter> getCurrentBabysitters() {
        return currentBabysitters;
    }

    public void setCurrentBabysitters(List<Babysitter> currentBabysitters) {
        this.currentBabysitters = currentBabysitters;
    }

    public BabysittersService() {
    }

    public List<Babysitter> getBabysittersList() {
        return babysittersRepository.getElements();
    }

    public BabysittersRepository getBabysittersRepository() {
        return babysittersRepository;
    }

    public BabysittersService(BabysittersRepository babysittersRepository) {
        this.babysittersRepository = babysittersRepository;
    }


    public void deleteBabysitter(Babysitter babysitter) {
        if(!babysitter.isEmployed()) {
            babysittersRepository.deleteElement(babysitter);
        } else throw new BabysitterException("An employed babysitter cannot be removed");
    }

    public void deleteBabysitterFromEmploymentList(Babysitter babysitter) {
        for (Employment employment : employmentsService.getAllEmploymentsForBabysitter(babysitter)) {
            employment.setBabysitter(null);
        }
    }

    public Babysitter[] getAllBabysittersArray() {
        return babysittersRepository.getBabysittersList().toArray(new Babysitter[0]);
    }

    public int getNumberOfBabysitters() {
        return babysittersRepository.getNumberOfElements();
    }


    public List<Babysitter> getListWithAppropriateBabysitters(int minAge, int numberOfChildren) {

        List<Babysitter> allBabysittersInRepository = babysittersRepository.getElements();
        List<Babysitter> appropriateBabysitters = new ArrayList<>();

        for (Babysitter babysitter : allBabysittersInRepository) {
            if (babysitter.getMinChildAge() <= minAge &&
                    babysitter.getMaxNumberOfChildrenInTheFamily() >= numberOfChildren) {
                appropriateBabysitters.add(babysitter);
            }
        }
        return appropriateBabysitters;
    }

    @PostConstruct
    public void initCurrentPersons() {
        currentBabysitters = getBabysittersRepository().getBabysittersList();
    }

    public Babysitter findByKey(String uuid) {
        return babysittersRepository.findByKey(uuid);
    }
}
