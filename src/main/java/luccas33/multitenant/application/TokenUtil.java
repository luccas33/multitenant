package luccas33.multitenant.application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import luccas33.multitenant.model.Token;
import org.springframework.http.HttpStatus;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;

public class TokenUtil {


    public static final int TOKEN_VALIDITY_MIN = 24 * 60;
    private static final String SECRET_KEY = "luccas33multitenanttokensecretkeywithmin64charschangeitforsafety";
    private static final Key KEY = new SecretKeySpec(SECRET_KEY.getBytes(), SignatureAlgorithm.HS256.getJcaName());

    public static String generateToken(Token content) {
        Date now = new Date();
        Date validity = Utils.addDateField(now, Calendar.MINUTE, TOKEN_VALIDITY_MIN);

        return Jwts.builder()
                .setSubject(content.getUserId() + "")
                .setIssuer(content.getSchemaName())
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(KEY).compact();
    }

    public static Token readToken(String tokenStr) throws StatusException {
        if (tokenStr == null) {
            throw new StatusException("Missing token", HttpStatus.BAD_REQUEST);
        }
        tokenStr = tokenStr.trim();
        if (tokenStr.startsWith("bearer")) {
            tokenStr = tokenStr.substring(6).trim();
        }
        Claims claims;
        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(KEY)
                    .build()
                    .parseClaimsJws(tokenStr)
                    .getBody();
        } catch (Exception e) {
            throw new StatusException("Invalid token", e, HttpStatus.NOT_ACCEPTABLE);
        }
        Date validity = Utils.addDateField(new Date(), Calendar.MINUTE, -TOKEN_VALIDITY_MIN);
        if (validity.compareTo(claims.getIssuedAt()) > 0) {
            throw new StatusException("Expirated token", HttpStatus.NOT_ACCEPTABLE);
        }
        try {
            return new Token(Long.parseLong(claims.getSubject()), claims.getIssuer());
        } catch (Exception e) {
            throw new StatusException("Error reading token", e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
