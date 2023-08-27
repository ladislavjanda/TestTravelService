package cz.istep.javatest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.istep.javatest.data.JavaScriptFramework;
import cz.istep.javatest.repository.JavaScriptFrameworkRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class JavaScriptFrameworkTests {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private JavaScriptFrameworkRepository repository;

    @Before
    public void prepareData() throws Exception {
        repository.deleteAll();

        JavaScriptFramework react = new JavaScriptFramework("React", "1.0.0", 6);
        JavaScriptFramework vue = new JavaScriptFramework("Vue.js", "1", 3);

        repository.save(react);
        repository.save(vue);
    }

    @Test
    public void frameworksTest() throws Exception {
        prepareData();

        mockMvc.perform(get("/frameworks")).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("React")))
                .andExpect(jsonPath("$[0].versionNumber", is("1.0.0")))
                .andExpect(jsonPath("$[0].hypeLevel", is(6)))
                .andExpect(jsonPath("$[1].name", is("Vue.js")));
    }

    @Test
    public void addFrameworkInvalid() throws Exception {
        JavaScriptFramework framework = new JavaScriptFramework();
        mockMvc.perform(post("/frameworks").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(framework)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].field", is("name")))
                .andExpect(jsonPath("$.errors[0].message", is("NotEmpty")));

        framework.setName("verylongnameofthejavascriptframeworkjavaisthebest");
        mockMvc.perform(post("/frameworks").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(framework)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].field", is("name")))
                .andExpect(jsonPath("$.errors[0].message", is("Size")));
    }

    @Test
    public void frameworkAddEdit() throws Exception {
        repository.deleteAll();

        JavaScriptFramework react = new JavaScriptFramework("React", "1.0.0", 6);

        mockMvc.perform(post("/frameworks").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(react)))
                .andExpect(status().isOk());


        Long idReact = repository.findAll().iterator().next().getId();
        react.setHypeLevel(7);
        react.setId(idReact);


        mockMvc.perform(post("/frameworks/edit").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(react)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/frameworks")).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("React")))
                .andExpect(jsonPath("$[0].versionNumber", is("1.0.0")))
                .andExpect(jsonPath("$[0].hypeLevel", is(7)));

        mockMvc.perform(post("/frameworks/delete").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(idReact)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/frameworks")).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

}
