package pl.ks.dk.tks.aggregates;

import jakarta.inject.Inject;
import pl.ks.dk.tks.domainmodel.babysitters.Babysitter;
import pl.ks.dk.tks.infrastructure.babysitters.AddBabysitterPort;
import pl.ks.dk.tks.infrastructure.babysitters.DeleteBabysitterPort;
import pl.ks.dk.tks.repositories.BabysittersRepositoryEnt;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BabysitterAdapter implements AddBabysitterPort, DeleteBabysitterPort {
    @Inject
    private BabysittersRepositoryEnt bre;

    @Override public void addBabysitter(Babysitter babysitter) {
        //converter domain -> ent
        //bre.addElement(babysitter);
    }

    @Override public void deleteBabysitter(Babysitter babysitter) {

    }
}
