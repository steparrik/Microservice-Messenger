package steparrik.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@FeignClient(name = "auth", url = "http://auth-service.default.svc.cluster.local:8089")
public interface AuthClient {

    @GetMapping("/check")
    Map<String, String> checkJwt(@RequestHeader("Authorization") String token);
}
