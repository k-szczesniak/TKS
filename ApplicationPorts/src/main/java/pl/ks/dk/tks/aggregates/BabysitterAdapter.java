package pl.ks.dk.tks.aggregates;

import jakarta.inject.Inject;
import org.apache.commons.beanutils.BeanUtils;
import pl.ks.dk.tks.domainmodel.babysitters.Babysitter;
import pl.ks.dk.tks.domainmodel.babysitters.TeachingSitter;
import pl.ks.dk.tks.domainmodel.babysitters.TidingSitter;
import pl.ks.dk.tks.infrastructure.babysitters.AddBabysitterPort;
import pl.ks.dk.tks.infrastructure.babysitters.DeleteBabysitterPort;
import pl.ks.dk.tks.infrastructure.babysitters.GetBabysitterPort;
import pl.ks.dk.tks.model.babysitters.BabysitterEnt;
import pl.ks.dk.tks.model.babysitters.TeachingSitterEnt;
import pl.ks.dk.tks.model.babysitters.TidingSitterEnt;
import pl.ks.dk.tks.repositories.BabysittersRepositoryEnt;

import javax.enterprise.context.ApplicationScoped;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class BabysitterAdapter implements AddBabysitterPort, DeleteBabysitterPort, GetBabysitterPort {

    @Inject
    private BabysittersRepositoryEnt babysittersRepositoryEnt;

    @Override
    public void addBabysitter(Babysitter babysitter) {
        //converter domain -> ent
        //bre.addElement(babysitter);
    }

    @Override
    public void deleteBabysitter(Babysitter babysitter) {

    }

    @Override
    public Babysitter getBabysitter(String uuid) {
        return null;
    }

    @Override
    public List<Babysitter> getAllBabysitters() {
        return null;
    }

    public static BabysitterEnt convertBabysitterToBabysitterEnt(Babysitter babysitter) {
        if (babysitter instanceof TeachingSitter) {
            BabysitterEnt teachingSitterEnt = new TeachingSitterEnt();
            return copyBabysitterToBabysitterEnt(teachingSitterEnt, babysitter);
        }
        if (babysitter instanceof TidingSitter) {
            BabysitterEnt tidingSitterEnt = new TidingSitterEnt();
            return copyBabysitterToBabysitterEnt(tidingSitterEnt, babysitter);
        } else {
            BabysitterEnt babysitterEnt = new BabysitterEnt();
            return copyBabysitterToBabysitterEnt(babysitterEnt, babysitter);
        }
    }

    public static Babysitter convertBabysitterEntToBabysitter(BabysitterEnt babysitterEnt) {
        if (babysitterEnt instanceof TeachingSitterEnt) {
            Babysitter teachingSitter = new TeachingSitter();
            return copyBabysitterEntToBabysitter(teachingSitter, babysitterEnt);
        }
        if (babysitterEnt instanceof TidingSitterEnt) {
            Babysitter tidingSitter = new TidingSitter();
            return copyBabysitterEntToBabysitter(tidingSitter, babysitterEnt);
        } else {
            Babysitter babysitter = new Babysitter();
            return copyBabysitterEntToBabysitter(babysitter, babysitterEnt);
        }
    }

    private static BabysitterEnt copyBabysitterToBabysitterEnt(BabysitterEnt babysitterEnt, Babysitter babysitter) {
        try {
            BeanUtils.copyProperties(babysitterEnt, babysitter);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return babysitterEnt;
    }

    private static Babysitter copyBabysitterEntToBabysitter(Babysitter babysitter, BabysitterEnt babysitterEnt) {
        try {
            BeanUtils.copyProperties(babysitter, babysitterEnt);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return babysitter;
    }
}
