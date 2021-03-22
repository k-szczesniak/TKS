package pl.ks.dk.tks.repositories;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pl.ks.dk.tks.model.babysitters.BabysitterEnt;
import pl.ks.dk.tks.model.babysitters.TeachingSitterEnt;
import pl.ks.dk.tks.model.babysitters.TidingSitterEnt;
import pl.ks.dk.tks.repositories.exceptions.RepositoryExceptionEnt;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@ApplicationScoped
public class BabysittersRepositoryEnt extends RepositoryEnt<BabysitterEnt> {

    final int SHORT_ID_LENGTH = 8;

    @Override
    public void addElement(BabysitterEnt element) {
        element.setUuid(RandomStringUtils.randomNumeric(SHORT_ID_LENGTH));
        super.addElement(element);
    }

    public void deleteElement(String uuid) {
        super.deleteElement(findByKey(uuid));
    }

    public void updateElement(BabysitterEnt babysitterEnt, String uuid) throws RepositoryExceptionEnt {
        try {
            BeanUtils.copyProperties(findByKey(uuid), babysitterEnt);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RepositoryExceptionEnt("Error during update");
        }
    }

    public List<BabysitterEnt> getBabysittersList() {
        return getElements();
    }

    public BabysitterEnt findByKey(String uuid) throws RepositoryExceptionEnt {
        List<BabysitterEnt> babysitterList = getElements();

        for (BabysitterEnt babysitter : babysitterList) {
            if (babysitter.getUuid().equals(uuid)) {
                return babysitter;
            }
        }
        throw new RepositoryExceptionEnt("Babysitter not found");
    }

    @PostConstruct
    private void initBabysittersList() {
        addElement(new BabysitterEnt("Anna", "Kwiatkowska", 123.0, 12, 4));
        addElement(new BabysitterEnt("Kinga", "Rusin", 50.0, 4, 4));
        addElement(new BabysitterEnt("Joanna", "Krupa", 40.0, 7, 2));
        addElement(new TidingSitterEnt("Renia", "Sprzątająca", 60.0, 1, 2, 40));
        addElement(new TeachingSitterEnt("Jola", "Ucząca", 100.0, 4, 1, 3));
        addElement(new TeachingSitterEnt("Sylwia", "Taczka", 75.0, 4, 6, 10));
        addElement(new TeachingSitterEnt("Bożena", "Jajko", 120.0, 1, 25, 25));
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
}
