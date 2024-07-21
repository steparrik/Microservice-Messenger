package steparrik.manager;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import steparrik.utils.token.JwtTokenUtil;
import steparrik.utils.validate.token.JwtValidate;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class CheckJwtManager {
    private final JwtValidate jwtValidate;

    public ResponseEntity<?> checkJwt(String authHeader){
        String token = authHeader.substring(7);
        return ResponseEntity.ok().body(jwtValidate.checkJwt(token));
    }

}
