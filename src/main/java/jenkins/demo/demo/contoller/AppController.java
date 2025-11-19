package jenkins.demo.demo.contoller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jenkins")
public class AppController {
    @GetMapping("/greet")
    public String greetings(){
        return "Hello Everyone";
    }
}
