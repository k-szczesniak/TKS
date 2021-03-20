package pl.ks.dk.tks.userinterface;

import pl.ks.dk.tks.domainmodel.babysitters.Babysitter;

import java.util.List;

public interface BabysitterUseCase {

    Babysitter getBabysitterByKey(String uuid);

    List<Babysitter> getAllBabysitters();

    void addBabysitter(Babysitter babysitter);

    void deleteBabysitter(Babysitter babysitter);
}
