package steparrik.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import steparrik.config.feign.FeignConfig;
import steparrik.dto.user.ProfileUserDto;

@FeignClient(name = "users", url = "http://users:8084", configuration = FeignConfig.class)
public interface UserClient {

    @GetMapping("/profile/{id}")
    ProfileUserDto getUserById(@PathVariable("id") Long id);

    @GetMapping("/profile")
    ProfileUserDto getUserByUsername(@RequestHeader(HttpHeaders.AUTHORIZATION)String authData);

    @GetMapping("/profile")
    ProfileUserDto getUserByUsernameOrPhoneNumber(@RequestHeader(HttpHeaders.AUTHORIZATION)String authData,
                                                  @RequestParam("username")String username,
                                                  @RequestParam("phoneNumber")String phoneNumber);
}
