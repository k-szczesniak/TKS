package pl.ks.dk.tks.services;

import pl.ks.dk.tks.domainmodel.babysitters.Babysitter;
import pl.ks.dk.tks.exceptions.AdapterException;
import pl.ks.dk.tks.infrastructure.babysitters.AddBabysitterPort;
import pl.ks.dk.tks.infrastructure.babysitters.DeleteBabysitterPort;
import pl.ks.dk.tks.infrastructure.babysitters.GetBabysitterPort;
import pl.ks.dk.tks.infrastructure.babysitters.UpdateBabysitterPort;
import pl.ks.dk.tks.services.exceptions.ServiceException;
import pl.ks.dk.tks.userinterface.rest.BabysitterRestUseCase;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class BabysittersService implements BabysitterRestUseCase {

    @Inject
    private DeleteBabysitterPort deleteBabysitterPort;

    @Inject
    private AddBabysitterPort addBabysitterPort;

    @Inject
    private GetBabysitterPort getBabysitterPort;

    @Inject
    private UpdateBabysitterPort updateBabysitterPort;

    @Override
    public Babysitter getBabysitterByKey(String uuid) throws ServiceException {
        Babysitter babysitter;
        try {
            babysitter = getBabysitterPort.getBabysitter(uuid);
        } catch (AdapterException adapterException) {
            throw new ServiceException(adapterException.getMessage(), adapterException);
        }
        return babysitter;
    }

    @Override
    public List<Babysitter> getAllBabysitters() {
        return getBabysitterPort.getAllBabysitters();
    }

    @Override
    public void addBabysitter(Babysitter babysitter) throws ServiceException {
        try {
            addBabysitterPort.addBabysitter(babysitter);
        } catch (AdapterException adapterException) {
            throw new ServiceException(adapterException.getMessage(), adapterException);
        }
    }

    @Override
    public void updateBabysitter(Babysitter babysitter, String uuid) {
        try {
            updateBabysitterPort.updateBabysitter(babysitter, uuid);
        } catch (AdapterException adapterException) {
            throw new ServiceException(adapterException.getMessage(),adapterException);
        }
    }

    @Override
    public void deleteBabysitter(String uuid) throws ServiceException {
        if (!getBabysitterByKey(uuid).isEmployed()) {
            try {
                deleteBabysitterPort.deleteBabysitter(uuid);
            } catch (AdapterException adapterException) {
                throw new ServiceException(adapterException.getMessage(), adapterException);
            }
        } else throw new ServiceException("An employed babysitter cannot be removed");
    }
}
