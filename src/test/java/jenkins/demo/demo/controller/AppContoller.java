package jenkins.demo.demo.controller;

import jenkins.demo.demo.contoller.AppController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AppController.class)
class AppControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGreetings() throws Exception {
        mockMvc.perform(get("/jenkins/greet"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello Everyone"));
    }
}
