package engineer.cafisodevs.todoapp.test;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    @GetMapping("/greetings")
    public String greet(HttpServletRequest req){
        return "Hello " + req.getUserPrincipal().getName();
    }
}
