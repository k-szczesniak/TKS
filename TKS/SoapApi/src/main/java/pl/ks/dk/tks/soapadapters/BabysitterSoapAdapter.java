package pl.ks.dk.tks.soapadapters;

import pl.ks.dk.tks.services.exceptions.ServiceException;
import pl.ks.dk.tks.soapmodel.babysitters.BabysitterSOAP;
import pl.ks.dk.tks.soapmodel.babysitters.BabysitterSOAPConverter;
import pl.ks.dk.tks.soapmodel.babysitters.TeachingSitterSOAP;
import pl.ks.dk.tks.soapmodel.babysitters.TidingSitterSOAP;
import pl.ks.dk.tks.soapmodel.exceptions.BabysitterSoapException;
import pl.ks.dk.tks.userinterface.BabysitterUseCase;

import javax.inject.Inject;
import javax.jws.WebService;
import java.util.List;

@WebService(serviceName = "BabysitterAPI")
public class BabysitterSoapAdapter {

    @Inject
    private BabysitterUseCase babysitterUseCase;


    public BabysitterSOAP getBabysitter(String uuid) throws BabysitterSoapException {
        try {
            return BabysitterSOAPConverter.convertBabysitterToBabysitterSOAP(babysitterUseCase.getBabysitterByKey(uuid));
        } catch (ServiceException e) {
            throw new BabysitterSoapException(e);
        }
    }

    public List<BabysitterSOAP> getAllBabysitters() {
        return BabysitterSOAPConverter.convertBabysitterListToBabysitterSOAPList(babysitterUseCase.getAllBabysitters());
    }

    public void createBabysitter(BabysitterSOAP babysitterSOAP) throws BabysitterSoapException {
        try {
            babysitterUseCase.addBabysitter(BabysitterSOAPConverter.convertBabysitterSOAPToBabysitter(babysitterSOAP));
        } catch (Exception e) {
            throw new BabysitterSoapException(e);
        }
    }

    public void createTidingSitter(TidingSitterSOAP tidingSitterSOAP) throws BabysitterSoapException {
        try {
            babysitterUseCase.addBabysitter(BabysitterSOAPConverter.convertBabysitterSOAPToBabysitter(tidingSitterSOAP));
        } catch (Exception e) {
            throw new BabysitterSoapException(e);
        }
    }

    public void createTeachingSitter(TeachingSitterSOAP teachingSitterSOAP) throws BabysitterSoapException {
        try {
            babysitterUseCase.addBabysitter(BabysitterSOAPConverter.convertBabysitterSOAPToBabysitter(teachingSitterSOAP));
        } catch (Exception e) {
            throw new BabysitterSoapException(e);
        }
    }
}
