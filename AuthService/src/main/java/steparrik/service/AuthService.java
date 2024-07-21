package steparrik.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import steparrik.dto.token.JwtResponseDto;
import steparrik.dto.user.AuthUserDto;
import steparrik.utils.token.JwtTokenUtil;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserDetailService userDetailService;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;


    @Transactional
    public JwtResponseDto createAuthToken(@RequestBody AuthUserDto authRequest) {
        try {
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(), authRequest.getPassword()));
        } catch (
                BadCredentialsException e) {
            return null;
        }
        UserDetails userDetails = userDetailService.loadUserByUsername(authRequest.getUsername());
        String token = jwtTokenUtil.generateToken(userDetails);
        return new JwtResponseDto(token);
    }
}
