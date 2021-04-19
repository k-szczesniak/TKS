package pl.ks.dk.tks.infrastructure.babysitters;

import pl.ks.dk.tks.domainmodel.babysitters.Babysitter;

import java.util.List;

public interface GetBabysitterPort {

    Babysitter getBabysitter(String uuid);

    List<Babysitter> getAllBabysitters();
}
