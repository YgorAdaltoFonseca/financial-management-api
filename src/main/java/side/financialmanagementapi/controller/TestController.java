package side.financialmanagementapi.controller;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

@RestController
@RequestMapping(path = "/test")
@RequiredArgsConstructor
public class TestController {

    /*Classe só para teste - Destruí-la futuramente, caso deseje*/

    @GetMapping
    public ResponseEntity<Test> test(@RequestParam String name) throws UnknownHostException {

        Test test = Test.builder()
                        .name(name)
                        .timestamp(new Date().toString())
                        .host(InetAddress.getLocalHost().getHostName())
                        .ip(InetAddress.getLocalHost().getHostAddress())
                        .build();

        return ResponseEntity.status(HttpStatus.OK).body(test);
    }

    @Builder
    @Getter
    static class Test {
        private String name;
        private String timestamp;
        private String host;
        private String ip;
    }
}
