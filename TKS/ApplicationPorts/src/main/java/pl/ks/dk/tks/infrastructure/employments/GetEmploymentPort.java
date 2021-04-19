package pl.ks.dk.tks.infrastructure.employments;

import pl.ks.dk.tks.domainmodel.employments.Employment;

import java.util.List;

public interface GetEmploymentPort {

    List<Employment> getActualEmploymentsForClient(String key);
}
