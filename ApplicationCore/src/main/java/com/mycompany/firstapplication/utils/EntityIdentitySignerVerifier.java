package com.mycompany.firstapplication.utils;

import com.mycompany.firstapplication.Interfaces.EntityToSign;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.shaded.json.JSONObject;

import java.text.ParseException;

public class EntityIdentitySignerVerifier {

    private static final String SECRET =
            "09SEVheeEsOTYLDZAJylVmlHb4XadBtgABGKZB5wmKVexgWU" +
                    "-006mSwGPlkWUCN0d75bfYtpsqecmsIGVtoCrPcN1h7MEAmH5HlisCPGuAlaBQRJtrMNM5uZRWaTRoXEhDchSqcAtl0hk_Fsb3VjlziIobgtbMs0DC8xctSW0eUqJ8W7hPyMllosTeb085sL26nmmWpQRC9ImYedx9FxYQFdr4XsNiU3l8Y5yMXVeFq6NqDL5BTcG2ximw2uHtHoIqtxYcggE6S2yKfeGQW7BMLsaBY6flym11zzezgeOO8NC2yJlZbvA2aHdLw7v-Dz-6TLzKjbCE5r8oWUFXoraA";

    public static boolean validateEntitySignature(String eTag) {
        try {
            final JWSObject jwsObject = JWSObject.parse(eTag);
            final JWSVerifier verifier = new MACVerifier(SECRET);
            return jwsObject.verify(verifier);
        } catch (ParseException | JOSEException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean verifyEntityIntegrity(String tagValue, EntityToSign entity) {
        try {
            String tagValueFromHeader = JWSObject.parse(tagValue).getPayload().toString()
                    .replaceAll("\"", "")
                    .replaceAll(",", ", ")
                    .replaceAll(":", "=");
            String tagValueFromEntityToSign = entity.takePayload().toString();
            return tagValueFromEntityToSign.equals(tagValueFromHeader);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String calculateETag(EntityToSign entity) {
        try {
            JWSSigner signer = new MACSigner(SECRET);
            String jsonObject = new JSONObject(entity.takePayload()).toString();
            JWSObject jwsObject = new JWSObject(new JWSHeader(JWSAlgorithm.HS256), new Payload(jsonObject));
            jwsObject.sign(signer);

            return jwsObject.serialize();
        } catch (JOSEException e) {
            e.printStackTrace();
            return "ETag failure";
        }
    }
}
