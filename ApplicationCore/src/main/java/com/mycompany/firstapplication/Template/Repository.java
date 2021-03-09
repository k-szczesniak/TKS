package com.mycompany.firstapplication.Template;


import com.mycompany.firstapplication.Exceptions.RepositoryException;

import java.util.ArrayList;
import java.util.List;

public abstract class Repository<T> {

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
            throw new RepositoryException("Element already exists");
        }
    }

    public void deleteElement(T element) {
        for (T item : elements) {
            if (item == element) {
                elements.remove(element);
                return;
            }
        }
        throw new RepositoryException("Element does not exists");
    }

    public int getNumberOfElements() {
        return elements.size();
    }

    public List<T> getElements() {
        return new ArrayList<>(elements);
    }
}
