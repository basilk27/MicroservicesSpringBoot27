package microservices.book.socialmultiplication.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith( SpringRunner.class )
@SpringBootTest
public class RandomGeneratorServiceTest {

    private RandomGeneratorService randomGeneratorService;

    @Before
    public void setUp() {
        randomGeneratorService = new RandomGeneratorServiceImp();
    }

    @Test
    public void generateRandomFactor() {
        //when a good sample of randomly generated factors is generated
        List<Integer> randomFactors = IntStream.range( 0,1000 )
                .map( i -> randomGeneratorService.generateRandomFactor() )
                .boxed().collect( Collectors.toList());

        //then all of them should be between 11 and 100 for this user case
        assertThat( randomFactors ).containsOnlyElementsOf( IntStream.range( 11, 100 ).boxed().collect( Collectors.toList()) );
    }
}