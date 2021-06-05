package pl.ks.dk.tks;

import lombok.extern.java.Log;
import pl.ks.dk.tks.domainmodel.users.Admin;
import pl.ks.dk.tks.domainmodel.users.Client;
import pl.ks.dk.tks.domainmodel.users.SuperUser;
import pl.ks.dk.tks.domainmodel.users.User;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.*;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;

@Log
public class Serialization {

    private static Object toObject(byte[] source) {
        Object o = null;
        ByteArrayInputStream bis = new ByteArrayInputStream(source);
        try (ObjectInput in = new ObjectInputStream(bis)) {
            o = in.readObject();
        } catch (ClassNotFoundException | IOException e) {
            log.info(e.getMessage());
        }
        return Objects.requireNonNull(o);
    }

    public static User deserialize(byte[] source) {
        Object o = toObject(source);
        if (o instanceof String) {
            JsonReader reader = Json.createReader(new StringReader((String) o));
            JsonObject root = reader.readObject();
            String userType = root.keySet().iterator().next();
            JsonObject jsonObject = root.getJsonObject(userType);
            User user = null;
            if (containsIgnoreCase(userType, "admin")) {
                user = new Admin();
            } else if (containsIgnoreCase(userType, "client")) {
                user = new Client();
            } else if (containsIgnoreCase(userType, "superUser")) {
                user = new SuperUser();
            }
            Objects.requireNonNull(user).setLogin(jsonObject.getString("username"));
            user.setName(jsonObject.getString("firstName"));
            user.setSurname(jsonObject.getString("lastName"));
            user.setActive(jsonObject.getBoolean("active"));
            return user;

        } else {
            return null;
        }

    }
}
