package pl.ks.dk.tks.infrastructure.babysitters;

import pl.ks.dk.tks.domainmodel.babysitters.Babysitter;

public interface DeleteBabysitterPort {

    void deleteBabysitter(Babysitter babysitter);
}
