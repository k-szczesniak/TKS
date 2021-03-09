package com.mycompany.firstapplication.Users;

import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class UserDTOWrapper {

    private static UserDTO userWrapper(Admin user) {
        return new UserDTO(user.getUuid(), user.getLogin(), user.getRole(), user.getName(), user.getSurname());
    }

    private static UserDTO userWrapper(SuperUser user) {
        return new UserDTO(user.getUuid(), user.getLogin(), user.getRole(), user.getName(), user.getSurname());
    }

    private static UserDTO userWrapper(Client user) {
        return new UserDTO(user.getUuid(), user.getLogin(), user.getRole(), user.getName(), user.getSurname(),
                user.getNumberOfChildren(),
                user.getAgeOfTheYoungestChild());
    }

    public static List<UserDTO> listWrapper(List<User> userList) {
        List<UserDTO> wrappedList = new ArrayList<>();
        for (User user : userList) {
            wrappedList.add(wrap(user));
        }
        return wrappedList;
    }

    public static UserDTO wrap(User user) {
        if (user instanceof Client) {
            return userWrapper(createClientFromUser(user));
        } else if (user instanceof Admin){
            return userWrapper((Admin)user);
        } else {
            return userWrapper((SuperUser) user);
        }
    }

    private static Client createClientFromUser(User user) {
        Client client = new Client();
        try {
            BeanUtils.copyProperties(client, user);
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return client;
    }
}
