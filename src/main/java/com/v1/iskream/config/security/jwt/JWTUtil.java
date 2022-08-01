package com.v1.iskream.config.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.v1.iskream.config.security.userDtail.UserDetailImpl;

import java.time.Instant;

public class JWTUtil {

    private static final String SECRET_KEY = "carrykim";
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET_KEY);
    private static final long AUTH_TIME = 60 * 60 * 60* 24 * 365;

    public static String getToken(UserDetailImpl userDetail){
        return JWT.create()
                .withSubject(userDetail.getUser().getId())
                .withClaim("exp", Instant.now().getEpochSecond() + AUTH_TIME)
                .sign(ALGORITHM);
    }

    public static String verify(String token){
        try {
            DecodedJWT verify = JWT.require(ALGORITHM).build().verify(token);
            return verify.getSubject();
        }catch(Exception ex){
            return "";
        }
    }
}
