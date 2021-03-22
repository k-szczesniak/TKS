package pl.ks.dk.tks.converters;

import pl.ks.dk.tks.domainmodel.employments.Employment;
import pl.ks.dk.tks.dtomodel.employments.EmploymentDTO;
import pl.ks.dk.tks.dtomodel.users.ClientDTO;

import java.util.List;
import java.util.stream.Collectors;

public class EmploymentDTOConverter {

    public static EmploymentDTO convertEmploymentToEmploymentDTO(Employment employment) {
        EmploymentDTO employmentDTO = new EmploymentDTO();
        employmentDTO
                .setBabysitter(BabysitterDTOConverter.convertBabysitterToBabysitterDTO(employment.getBabysitter()));
        employmentDTO.setClient((ClientDTO) UserDTOConverter.convertUserToUserDTO(employment.getClient()));
        employmentDTO.setEndOfEmployment(employment.getEndOfEmployment());
        employmentDTO.setBeginningOfEmployment(employment.getBeginningOfEmployment());
        employmentDTO.setUuid(employment.getUuid());

        return employmentDTO;
    }

    public static List<EmploymentDTO> convertEmploymentListToEmploymentDTOList(List<Employment> employmentList) {
        return employmentList.stream()
                .map(EmploymentDTOConverter::convertEmploymentToEmploymentDTO)
                .collect(Collectors.toList());
    }
}
