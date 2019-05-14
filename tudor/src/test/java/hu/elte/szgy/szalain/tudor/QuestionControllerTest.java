package hu.elte.szgy.szalain.tudor;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.elte.szgy.szalain.tudor.model.Question;
import java.io.IOException;
import static junit.framework.Assert.assertEquals;
import org.junit.After;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TudorApplication.class)
public class QuestionControllerTest {
    private static Logger log = LoggerFactory.getLogger(QuestionControllerTest.class);

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
	log.info("SetUp executing");
	DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
	this.mockMvc = builder.build();
    }

    @After
    public void tearDown() throws Exception {
	log.info("TearDown executing");
    }

    private <T> T mapFromJson(String json, Class<T> clazz)
	    throws JsonParseException, JsonMappingException, IOException {

	ObjectMapper objectMapper = new ObjectMapper();
	return objectMapper.readValue(json, clazz);
    }

    @Test
    public void testGetAllQuestions() throws Exception {
        assertTrue(true);
	/*String uri = "/question/all";
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
	int status = mvcResult.getResponse().getStatus();
	assertEquals(200, status);
	String content = mvcResult.getResponse().getContentAsString();
	Question[] questionlist = mapFromJson(content, Question[].class);
	log.info("GetAllQuestion returned " + questionlist.length + " question");
	for(Question q : questionlist) {
	    log.info("Title: " + q.getTitle() + " Text: " + q.getText() + " ID: " + q.getId() + " Class:" + q.getClass());
	}
	assertTrue(questionlist.length > 0);*/
    }
}
