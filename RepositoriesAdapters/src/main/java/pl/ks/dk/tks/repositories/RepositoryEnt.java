package pl.ks.dk.tks.repositories;


import pl.ks.dk.tks.repositories.exceptions.RepositoryExceptionEnt;

import java.util.ArrayList;
import java.util.List;

public abstract class RepositoryEnt<T> {

    private List<T> elements = new ArrayList<>();

    public boolean checkIfTheElementIsPresent(T element) {
        for (T item : elements) {
            if (item == element) {
                return true;
            }
        }
        return false;
    }

    public void addElement(T element) {
        if (!checkIfTheElementIsPresent(element)) {
            elements.add(element);
        } else {
            throw new RepositoryExceptionEnt("Element already exists");
        }
    }

    public void deleteElement(T element) {
        for (T item : elements) {
            if (item == element) {
                elements.remove(element);
                return;
            }
        }
        throw new RepositoryExceptionEnt("Element does not exists");
    }

    public int getNumberOfElements() {
        return elements.size();
    }

    public List<T> getElements() {
        return new ArrayList<>(elements);
    }
}
