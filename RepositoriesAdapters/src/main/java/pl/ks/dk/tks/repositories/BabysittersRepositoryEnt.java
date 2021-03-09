package pl.ks.dk.tks.repositories;


import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pl.ks.dk.tks.model.babysitters.BabysitterEnt;
import pl.ks.dk.tks.model.babysitters.TeachingSitterEnt;
import pl.ks.dk.tks.model.babysitters.TidingSitterEnt;
import pl.ks.dk.tks.model.exceptions.RepositoryExceptionEnt;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class BabysittersRepositoryEnt extends RepositoryEnt<BabysitterEnt> {

    final int SHORT_ID_LENGTH = 8;

    @Override public void addElement(BabysitterEnt element) {
        element.setUuid(RandomStringUtils.randomNumeric(SHORT_ID_LENGTH));
        super.addElement(element);
    }

    public List<BabysitterEnt> getBabysittersList() {
        return getElements();
    }

    public BabysitterEnt findByKey(String uuid) {
        List<BabysitterEnt> babysitterList = getElements();

        for (BabysitterEnt babysitter : babysitterList) {
            if (babysitter.getUuid().equals(uuid)) {
                return babysitter;
            }
        }
        throw new RepositoryExceptionEnt("Element not found");
    }

    public String toString() {
        ToStringBuilder stringBuilder = new ToStringBuilder(this);
        stringBuilder.append(System.getProperty("line.separator"));

        for (BabysitterEnt babysitter : getElements()) {
            stringBuilder.append("babysitter", babysitter.toString());
            stringBuilder.append(System.getProperty("line.separator"));
        }

        return stringBuilder.toString();
    }

    public List<BabysitterEnt> showSelectedBabysitter(String id) {
        List<BabysitterEnt> temporaryBabysittersList = new ArrayList<>();
        temporaryBabysittersList.add(findByKey(id));
        return temporaryBabysittersList;
    }

    @Override
    public void deleteElement(BabysitterEnt babysitter) {
        super.deleteElement(babysitter);
    }

    @PostConstruct
    private void initBabysittersList() {
        addElement(new BabysitterEnt("Anna", "Kwiatkowska", 123, 12, 4));
        addElement(new BabysitterEnt("Kinga", "Rusin", 50, 4, 4));
        addElement(new BabysitterEnt("Joanna", "Krupa", 40, 7, 2));
        addElement(new TidingSitterEnt("Renia", "Sprzątająca", 60, 1, 2, 40));
        addElement(new TeachingSitterEnt("Jola", "Ucząca", 100, 4, 1, 3));
        addElement(new TeachingSitterEnt("Sylwia", "Taczka", 75, 4, 6, 10));
        addElement(new TeachingSitterEnt("Bożena", "Jajko", 120, 1, 25, 25));
    }
}
