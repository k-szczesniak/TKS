package com.mycompany.firstapplication.Babysitters;

import com.mycompany.firstapplication.Employment.Employment;
import com.mycompany.firstapplication.Employment.EmploymentsManager;
import com.mycompany.firstapplication.Exceptions.BabysitterException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class BabysittersManager implements Serializable {

    @Inject
    private BabysittersRepository babysittersRepository;

    @Inject
    private EmploymentsManager employmentsManager;

    private List<Babysitter> currentBabysitters;

    public List<Babysitter> getCurrentBabysitters() {
        return currentBabysitters;
    }

    public void setCurrentBabysitters(List<Babysitter> currentBabysitters) {
        this.currentBabysitters = currentBabysitters;
    }

    public BabysittersManager() {
    }

    public List<Babysitter> getBabysittersList() {
        return babysittersRepository.getElements();
    }

    public BabysittersRepository getBabysittersRepository() {
        return babysittersRepository;
    }

    public BabysittersManager(BabysittersRepository babysittersRepository) {
        this.babysittersRepository = babysittersRepository;
    }

    public void addBabysitter(Babysitter babysitter) {
        babysittersRepository.addElement(babysitter);
    }

    public void deleteBabysitter(Babysitter babysitter) {
        if(!babysitter.isEmployed()) {
            babysittersRepository.deleteElement(babysitter);
        } else throw new BabysitterException("An employed babysitter cannot be removed");
    }

    public void deleteBabysitterFromEmploymentList(Babysitter babysitter) {
        for (Employment employment : employmentsManager.getAllEmploymentsForBabysitter(babysitter)) {
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
