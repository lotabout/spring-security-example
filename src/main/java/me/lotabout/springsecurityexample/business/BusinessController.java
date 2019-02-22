package me.lotabout.springsecurityexample.business;

import me.lotabout.springsecurityexample.common.struct.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BusinessController {

    @PostMapping("/api/v1/square/{value}")
    public Response<Long> square(@PathVariable long value) {
        return Response.ok(value * value);
    }

    @PostMapping("/api/v1/triple/{user}/{hash}/{value}")
    public Response<Long> triple(@PathVariable long value) {
        return Response.ok(3 * value);
    }

    @GetMapping("/api/v1/hello")
    public Response<String> hello() {
        return Response.ok("hello world");
    }

    @PostMapping("/api/v1/admin")
    public Response<String> admin() {
        return Response.ok("DONE!");
    }

    @PostMapping("/api/v1/ignore")
    public Response<String> ignore() {
        return Response.ok("Security Ignored!");
    }
}
