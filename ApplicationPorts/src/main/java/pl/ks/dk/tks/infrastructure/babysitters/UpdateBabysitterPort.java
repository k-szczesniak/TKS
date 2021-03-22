package pl.ks.dk.tks.infrastructure.babysitters;

import pl.ks.dk.tks.domainmodel.babysitters.Babysitter;

public interface UpdateBabysitterPort {
    void updateBabysitter(Babysitter babysitter, String key);
}
