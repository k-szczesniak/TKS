package pl.ks.dk.tks.converters;

import org.apache.commons.beanutils.BeanUtils;
import pl.ks.dk.tks.domainmodel.employments.Employment;
import pl.ks.dk.tks.dtomodel.employments.EmploymentDTO;
import pl.ks.dk.tks.exceptions.DTOConverterException;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

public class EmploymentDTOConverter {

    public static EmploymentDTO convertEmploymentToEmploymentDTO(Employment employment) throws DTOConverterException {
        EmploymentDTO employmentDTO = new EmploymentDTO();
        try {
            BeanUtils.copyProperties(employmentDTO, employment);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new DTOConverterException("Convert Employment to EmploymentDTO error", e);
        }
        return employmentDTO;
    }

    public static List<EmploymentDTO> convertEmploymentListToEmploymentDTOList(List<Employment> employmentList) {
        return employmentList.stream()
                .map(EmploymentDTOConverter::convertEmploymentToEmploymentDTO)
                .collect(Collectors.toList());
    }
}
