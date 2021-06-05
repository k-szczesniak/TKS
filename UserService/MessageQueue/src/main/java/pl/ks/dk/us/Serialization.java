package pl.ks.dk.us;

import lombok.extern.slf4j.Slf4j;
import pl.ks.dk.us.users.User;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

@Slf4j
public class Serialization {

    private static JsonObjectBuilder prepareUser(User source) {
        return Json.createObjectBuilder()
                .add("login", source.getLogin())
                .add("name", source.getName())
                .add("surname", source.getSurname())
                .add("password", source.getPassword())
                .add("role", source.getRole());
    }

    public static String userToJsonString(User source) {
        return prepareUser(source).build().toString();
    }

    public static String clientToJsonString(User source, int numberOfChildren, int ageOfTheYoungestChild) {
        JsonObjectBuilder objectBuilder = prepareUser(source);
        return objectBuilder.add("numberOfChildren", numberOfChildren)
                .add("ageOfTheYoungestChild", ageOfTheYoungestChild)
                .build().toString();
    }
}

//    public static byte[] serialize(Serializable source) {
//        byte[] bytes = null;
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        try (ObjectOutputStream out = new ObjectOutputStream(bos)) {
//            if (source instanceof User) {
//                out.writeObject(toJsonString((User) source));
//            } else {
//                out.writeObject(source);
//            }
//            out.flush();
//            bytes = bos.toByteArray();
//        } catch (IOException e) {
//            log.error(e.getMessage());
//        }
//        return Objects.requireNonNull(bytes);
//    }

//    private static Object toObject(byte[] source) {
//        Object o = null;
//        ByteArrayInputStream bis = new ByteArrayInputStream(source);
//        try (ObjectInput in = new ObjectInputStream(bis)) {
//            o = in.readObject();
//        } catch (ClassNotFoundException | IOException e) {
//            log.error(e.getMessage());
//        }
//        return Objects.requireNonNull(o);
//    }

//    private static UserDTO deserialize(JsonObject source) {
//        String userType = source.getString("role");
//        UserDTO user = null;
//        if (containsIgnoreCase(userType, "admin")) {
//            user = new UserDTO();
//        }
//        Objects.requireNonNull(user).setLogin(source.getString("username"));
//        user.setPassword(source.getString("password"));
//        user.setName(source.getString("firstName"));
//        user.setSurname(source.getString("lastName"));
//        user.setActive(source.getBoolean("active"));
//        return user;
//    }

//    public static UserDTO deserializeUser(byte[] source) {
//        Object o = toObject(source);
//        if (o instanceof String) {
//            JsonReader reader = Json.createReader(new StringReader((String) o));
//            JsonObject jsonObject = reader.readObject();
//            return deserialize(jsonObject);
//        } else {
//            return null;
//        }
//    }
//
//    public static List<UserDTO> deserializeUsers(byte[] source) {
//        Object o = toObject(source);
//        List<UserDTO> users = new ArrayList<>();
//        if (o instanceof String) {
//            JsonReader reader = Json.createReader(new StringReader((String) o));
//            JsonArray jsonArray = reader.readArray();
//            for (JsonValue jsonValue : jsonArray) {
//                JsonObject jsonObject = jsonValue.asJsonObject();
//                users.add(deserialize(jsonObject));
//            }
//        }
//        return users;
//    }
