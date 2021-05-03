package pl.ks.dk.us.converters;

import pl.ks.dk.us.dtomodel.users.UserDTO;
import pl.ks.dk.us.exceptions.DTOConverterException;
import pl.ks.dk.us.users.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserDTOConverter {

    public static User convertUserDTOToUser(UserDTO userDTO) {
        return copyUserDTOToUser(userDTO);
    }

    public static UserDTO convertUserToUserDTO(User user) {
        return copyUserToUserDTO(user);
    }

    private static User copyUserDTOToUser(UserDTO userDTO) throws DTOConverterException {
        User user = new User(userDTO.getLogin(), userDTO.getName(), userDTO.getSurname(), userDTO.getPassword(),
                userDTO.getRole());
        if (userDTO.getUuid() != null) {
            user.setUuid(userDTO.getUuid());
        }
        user.setActive(userDTO.isActive());
        return user;
    }

    private static UserDTO copyUserToUserDTO(User user) throws DTOConverterException {
        UserDTO userDTO =
                new UserDTO(user.getLogin(), user.getName(), user.getSurname(), user.getPassword(), user.getRole());
        if (user.getUuid() != null) {
            userDTO.setUuid(user.getUuid());
        }
        userDTO.setActive(user.isActive());
        return userDTO;
    }

    public static List<UserDTO> convertUserListToUserDTOList(List<User> userList) {
        return userList.stream()
                .map(UserDTOConverter::convertUserToUserDTO)
                .collect(Collectors.toList());
    }
}
