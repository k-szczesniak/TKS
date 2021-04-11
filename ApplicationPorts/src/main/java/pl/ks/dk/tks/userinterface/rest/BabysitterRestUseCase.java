package pl.ks.dk.tks.userinterface.rest;

import pl.ks.dk.tks.domainmodel.babysitters.Babysitter;

import java.util.List;

public interface BabysitterRestUseCase {

    Babysitter getBabysitterByKey(String uuid);

    List<Babysitter> getAllBabysitters();

    void addBabysitter(Babysitter babysitter);

    void updateBabysitter(Babysitter babysitter, String uuid);

    void deleteBabysitter(String uuid);
}
