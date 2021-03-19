package pl.ks.dk.tks.userinterface.babysitters;

import pl.ks.dk.tks.domainmodel.babysitters.Babysitter;

public interface DeleteBabysitterPort {

    void deleteBabysitter(Babysitter babysitter);
}
