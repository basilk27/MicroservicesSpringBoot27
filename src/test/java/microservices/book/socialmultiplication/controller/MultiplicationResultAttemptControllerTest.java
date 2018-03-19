package microservices.book.socialmultiplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import microservices.book.socialmultiplication.domain.Multiplication;
import microservices.book.socialmultiplication.domain.MultiplicationResultAttempt;
import microservices.book.socialmultiplication.domain.ResultResponse;
import microservices.book.socialmultiplication.domain.User;
import microservices.book.socialmultiplication.service.MultiplicationService;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.jws.soap.SOAPBinding;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith( SpringRunner.class )
@WebMvcTest(MultiplicationResultAttemptController.class)
@Slf4j
public class MultiplicationResultAttemptControllerTest {

    @MockBean
    private MultiplicationService multiplicationService;

    @Autowired
    private MockMvc mockMvc;

    private JacksonTester<MultiplicationResultAttempt> jsonResultAttempt;
    private JacksonTester<List<MultiplicationResultAttempt>> jsonResultAttemptList;

    @Before
    public void setUp() {
        JacksonTester.initFields( this, new ObjectMapper() );
    }

    @Test
    public void postResultReturnCorrect() throws Exception {
        genericParameterizedTest(true);
    }

    @Test
    public void postResultReturnNotCorrect() throws Exception {
        genericParameterizedTest(false);
    }

    private void genericParameterizedTest(final boolean correct) throws Exception {
        //given - note we are not testing the service itself that is why it's mocked
        given(multiplicationService.checkAttempt( any(MultiplicationResultAttempt.class))).willReturn( correct );

        User user = new User( "john_doe" );
        Multiplication multiplication = new Multiplication( 50, 70 );
        MultiplicationResultAttempt resultAttempt = new MultiplicationResultAttempt( user, multiplication, 3500, correct );

        //when
        MockHttpServletResponse response = mockMvc.perform( post( "/results" )
                                                .contentType( MediaType.APPLICATION_JSON )
                                                .content( jsonResultAttempt.write( resultAttempt ).getJson()))
                                            .andReturn().getResponse();

        //then
        assertThat( response.getStatus() ).isEqualTo( HttpStatus.OK.value() );
        assertThat( response.getContentAsString() ).isEqualTo( jsonResultAttempt.write(
                        new MultiplicationResultAttempt( resultAttempt.getUser(), resultAttempt.getMultiplication(),
                                                         resultAttempt.getResultAttempt(), resultAttempt.isCorrect() )).getJson() );
    }

    @Test
    public void getUserStats() {
        try {
            //given
            User user = new User( "john_doe" );
            Multiplication multiplication = new Multiplication( 50, 70 );
            MultiplicationResultAttempt attempt = new MultiplicationResultAttempt( user, multiplication, 350, true );
            List< MultiplicationResultAttempt > resultAttemptList = Lists.newArrayList( attempt, attempt );

            given( multiplicationService.findTop5ByUserAliasOrderByIdDesc( "john_doe" ) ).willReturn( resultAttemptList );

            // when
            MockHttpServletResponse response = mockMvc.perform( get( "/results" ).param( "alias", "john_doe" ) ).andReturn().getResponse();

            // then
            assertThat( response.getStatus() ).isEqualTo( HttpStatus.OK.value() );
            assertThat( response.getContentAsString() ).isEqualTo( jsonResultAttemptList.write( resultAttemptList ).getJson() );
        }
        catch ( Exception ex ) {
            log.error( "MultiplicationResultAttemptControllerTest:getUserStats:: " + ex.getMessage() );
        }
    }
}