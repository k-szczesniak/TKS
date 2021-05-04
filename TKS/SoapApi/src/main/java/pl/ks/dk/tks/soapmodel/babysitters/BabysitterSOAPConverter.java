package pl.ks.dk.tks.soapmodel.babysitters;

import org.apache.commons.beanutils.BeanUtils;
import pl.ks.dk.tks.domainmodel.babysitters.Babysitter;
import pl.ks.dk.tks.domainmodel.babysitters.TeachingSitter;
import pl.ks.dk.tks.domainmodel.babysitters.TidingSitter;
import pl.ks.dk.tks.soapmodel.exceptions.SOAPConverterException;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

public class BabysitterSOAPConverter {

    public static Babysitter convertBabysitterSOAPToBabysitter(BabysitterSOAP babysitterSOAP) {
        if (babysitterSOAP instanceof TeachingSitterSOAP) {
            Babysitter teachingSitter = new TeachingSitter();
            return copyBabysitterSOAPToBabysitter(teachingSitter, babysitterSOAP);
        }
        if (babysitterSOAP instanceof TidingSitterSOAP) {
            Babysitter tidingSitter = new TidingSitter();
            return copyBabysitterSOAPToBabysitter(tidingSitter, babysitterSOAP);
        } else {
            Babysitter babysitter = new Babysitter();
            return copyBabysitterSOAPToBabysitter(babysitter, babysitterSOAP);
        }
    }

    private static Babysitter copyBabysitterSOAPToBabysitter(Babysitter babysitter, BabysitterSOAP babysitterSOAP)
            throws SOAPConverterException {
        try {
            BeanUtils.copyProperties(babysitter, babysitterSOAP);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new SOAPConverterException("Convert BabysitterSOAP to Babysitter error", e);
        }
        return babysitter;
    }

    public static BabysitterSOAP convertBabysitterToBabysitterSOAP(Babysitter babysitter) {
        if (babysitter instanceof TeachingSitter) {
            BabysitterSOAP teachingSitterSOAP = new TeachingSitterSOAP();
            return copyBabysitterToBabysitterSOAP(teachingSitterSOAP, babysitter);
        }
        if (babysitter instanceof TidingSitter) {
            BabysitterSOAP tidingSitterSOAP= new TidingSitterSOAP();
            return copyBabysitterToBabysitterSOAP(tidingSitterSOAP, babysitter);
        } else {
            BabysitterSOAP babysitterSOAP = new BabysitterSOAP();
            return copyBabysitterToBabysitterSOAP(babysitterSOAP, babysitter);
        }
    }

    private static BabysitterSOAP copyBabysitterToBabysitterSOAP(BabysitterSOAP babysitterSOAP, Babysitter babysitter)
            throws SOAPConverterException {
        try {
            BeanUtils.copyProperties(babysitterSOAP, babysitter);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new SOAPConverterException("Convert Babysitter to BabysitterSOAP error", e);
        }
        return babysitterSOAP;
    }

    public static List<BabysitterSOAP> convertBabysitterListToBabysitterSOAPList(List<Babysitter> babysitterList) {
        return babysitterList.stream()
                .map(BabysitterSOAPConverter::convertBabysitterToBabysitterSOAP)
                .collect(Collectors.toList());
    }
}
