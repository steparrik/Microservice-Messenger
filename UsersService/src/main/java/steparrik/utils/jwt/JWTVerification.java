package steparrik.utils.jwt;

import com.auth0.jwt.JWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JWTVerification {
//    @Value("${jwt.secret}")
    private String jwtSecret;

    public String getUsernameFromJwt(String authData){
        String token = authData.substring(7);
//        String username = JWT.require(Algorithm.HMAC256(jwtSecret))
//                .build()
//                .verify(token)
//                .getClaim("sub")
//                .asString();
        String username = JWT.decode(token).getClaim("sub").asString();

        return username;
    }

}
