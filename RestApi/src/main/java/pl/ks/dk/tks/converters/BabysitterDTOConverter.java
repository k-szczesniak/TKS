package pl.ks.dk.tks.converters;

import org.apache.commons.beanutils.BeanUtils;
import pl.ks.dk.tks.domainmodel.babysitters.Babysitter;
import pl.ks.dk.tks.domainmodel.babysitters.TeachingSitter;
import pl.ks.dk.tks.domainmodel.babysitters.TidingSitter;
import pl.ks.dk.tks.dtomodel.babysitters.BabysitterDTO;
import pl.ks.dk.tks.dtomodel.babysitters.TeachingSitterDTO;
import pl.ks.dk.tks.dtomodel.babysitters.TidingSitterDTO;
import pl.ks.dk.tks.exceptions.DTOConverterException;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

public class BabysitterDTOConverter {

    public static Babysitter convertBabysitterDTOToBabysitter(BabysitterDTO babysitterDTO) {
        if (babysitterDTO instanceof TeachingSitterDTO) {
            Babysitter teachingSitter = new TeachingSitter();
            return copyBabysitterDTOToBabysitter(teachingSitter, babysitterDTO);
        }
        if (babysitterDTO instanceof TidingSitterDTO) {
            Babysitter tidingSitter = new TidingSitter();
            return copyBabysitterDTOToBabysitter(tidingSitter, babysitterDTO);
        } else {
            Babysitter babysitter = new Babysitter();
            return copyBabysitterDTOToBabysitter(babysitter, babysitterDTO);
        }
    }

    private static Babysitter copyBabysitterDTOToBabysitter(Babysitter babysitter, BabysitterDTO babysitterDTO)
            throws DTOConverterException {
        try {
            BeanUtils.copyProperties(babysitter, babysitterDTO);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new DTOConverterException("Convert BabysitterDTO to Babysitter error", e);
        }
        return babysitter;
    }

    public static BabysitterDTO convertBabysitterToBabysitterDTO(Babysitter babysitter) {
        if (babysitter instanceof TeachingSitter) {
            BabysitterDTO teachingSitterDTO = new TeachingSitterDTO();
            return copyBabysitterToBabysitterDTO(teachingSitterDTO, babysitter);
        }
        if (babysitter instanceof TidingSitter) {
            BabysitterDTO tidingSitterDTO= new TidingSitterDTO();
            return copyBabysitterToBabysitterDTO(tidingSitterDTO, babysitter);
        } else {
            BabysitterDTO babysitterDTO = new BabysitterDTO();
            return copyBabysitterToBabysitterDTO(babysitterDTO, babysitter);
        }
    }

    private static BabysitterDTO copyBabysitterToBabysitterDTO(BabysitterDTO babysitterDTO, Babysitter babysitter)
            throws DTOConverterException {
        try {
            BeanUtils.copyProperties(babysitterDTO, babysitter);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new DTOConverterException("Convert Babysitter to BabysitterDTO error", e);
        }
        return babysitterDTO;
    }

    public static List<BabysitterDTO> convertBabysitterListToBabysitterDTOList(List<Babysitter> babysitterList) {
        return babysitterList.stream()
                .map(BabysitterDTOConverter::convertBabysitterToBabysitterDTO)
                .collect(Collectors.toList());
    }
}
