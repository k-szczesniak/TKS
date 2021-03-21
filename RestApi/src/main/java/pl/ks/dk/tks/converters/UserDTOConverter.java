package pl.ks.dk.tks.converters;

import org.apache.commons.beanutils.BeanUtils;
import pl.ks.dk.tks.domainmodel.users.Admin;
import pl.ks.dk.tks.domainmodel.users.Client;
import pl.ks.dk.tks.domainmodel.users.SuperUser;
import pl.ks.dk.tks.domainmodel.users.User;
import pl.ks.dk.tks.dtomodel.users.AdminDTO;
import pl.ks.dk.tks.dtomodel.users.ClientDTO;
import pl.ks.dk.tks.dtomodel.users.SuperUserDTO;
import pl.ks.dk.tks.dtomodel.users.UserDTO;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

public class UserDTOConverter {

    public static User convertUserDTOToUser(UserDTO userDTO) {
        if (userDTO instanceof ClientDTO) {
            User client = new Client();
            return copyUserDTOToUser(client, userDTO);
        }
        if (userDTO instanceof SuperUserDTO) {
            User superUser = new SuperUser();
            return copyUserDTOToUser(superUser, userDTO);
        } else {
            User admin = new Admin();
            return copyUserDTOToUser(admin, userDTO);
        }
    }

    //TODO: EXCEPTIONS
    private static User copyUserDTOToUser(User user, UserDTO userDTO) {
        try {
            BeanUtils.copyProperties(user, userDTO);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException("Convert UserDTO to User error", e);
        }
        return user;
    }

    public static UserDTO convertUserToUserDTO(User user) {
        if (user instanceof Client) {
            UserDTO clientDTO = new ClientDTO();
            return copyUserToUserDTO(clientDTO, user);
        }
        if (user instanceof SuperUser) {
            UserDTO superUserDTO = new SuperUserDTO();
            return copyUserToUserDTO(superUserDTO, user);
        } else {
            UserDTO adminDTO = new AdminDTO();
            return copyUserToUserDTO(adminDTO, user);
        }
    }

    //TODO: EXCEPTIONS
    private static UserDTO copyUserToUserDTO(UserDTO userDTO, User user) {
        try {
            BeanUtils.copyProperties(userDTO, user);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException("Convert User to UserEnt error", e);
        }
        return userDTO;
    }

    public static List<UserDTO> convertUserListToUserDTOList(List<User> userList) {
        return userList.stream()
                .map(UserDTOConverter::convertUserToUserDTO)
                .collect(Collectors.toList());
    }
}
