package microservices.book.socialmultiplication.service;

import microservices.book.socialmultiplication.domain.Multiplication;
import microservices.book.socialmultiplication.domain.MultiplicationResultAttempt;
import microservices.book.socialmultiplication.domain.User;
import microservices.book.socialmultiplication.repository.MultiplicationResultAttemptRepository;
import microservices.book.socialmultiplication.repository.UserRepository;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

public class MultiplicationServiceImplTest {

    private MultiplicationServiceImpl multiplicationService;

    @Mock
    private RandomGeneratorService randomGeneratorService;

    @Mock
    private MultiplicationResultAttemptRepository multiplicationResultAttemptRepository;

    @Mock
    private UserRepository userRepository;

    @Before
    public void setUp() {
        //with this call to initMocks we tell Mockito to process the annotations
        MockitoAnnotations.initMocks( this );

        multiplicationService = new MultiplicationServiceImpl( randomGeneratorService,
                                                               multiplicationResultAttemptRepository,
                                                               userRepository );
    }

    @Test
    public void createRandomMultiplication() {
        //given Random generator service will return first 50, then 30
        given(randomGeneratorService.generateRandomFactor()).willReturn(50, 30);

        //when
        Multiplication multiplication = multiplicationService.createRandomMultiplication();

        //assert
        assertThat( multiplication.getFactorA() ).isEqualTo( 50 );
        assertThat( multiplication.getFactorB() ).isEqualTo( 30 );
    }

    @Test
    public void checkCorrectAttemptTest() {
        //given
        Multiplication multiplication = new Multiplication( 50, 60 );
        User user = new User( "john_doe" );
        MultiplicationResultAttempt resultAttempt = new MultiplicationResultAttempt( user, multiplication, 3000, false );

        MultiplicationResultAttempt verrifiedAttempt = new MultiplicationResultAttempt( user, multiplication, 3000, true );

        given( userRepository.findByAlias( "john_doe" ) ).willReturn( Optional.empty() );

        //when
        boolean attemptResult = multiplicationService.checkAttempt( resultAttempt );

        //assert
        assertThat( attemptResult ).isTrue();
        verify( multiplicationResultAttemptRepository ).save( verrifiedAttempt );

    }

    @Test
    public void checkWrongAttemptTest() {
        //given
        Multiplication multiplication = new Multiplication( 50, 60 );
        User user = new User( "john_doe" );
        MultiplicationResultAttempt resultAttempt = new MultiplicationResultAttempt( user, multiplication, 3010, false );

        given( userRepository.findByAlias( "john_doe" ) ).willReturn( Optional.empty() );

        //when
        boolean attemptResult = multiplicationService.checkAttempt( resultAttempt );

        //assert
        assertThat( attemptResult ).isFalse();

        verify( multiplicationResultAttemptRepository ).save( resultAttempt );
    }

    @Test
    public void findTop5ByUserAliasOrderByIdDescTest() {
        //given
        Multiplication multiplication = new Multiplication( 50, 60 );
        User user = new User( "john_doe" );
        MultiplicationResultAttempt resultAttempt1 = new MultiplicationResultAttempt( user, multiplication, 3010, false );
        MultiplicationResultAttempt resultAttempt2 = new MultiplicationResultAttempt( user, multiplication, 3051, false );
        List<MultiplicationResultAttempt> resultAttemptList = Lists.newArrayList(resultAttempt1, resultAttempt2);

        given( userRepository.findByAlias( "john_doe" )).willReturn( Optional.empty() );
        given( multiplicationResultAttemptRepository.findTop5ByUserAliasOrderByIdDesc( "john_doe" ) ).willReturn( resultAttemptList );

        // when
        List<MultiplicationResultAttempt> resultAttemptList2 = multiplicationService.findTop5ByUserAliasOrderByIdDesc( "john_doe" );

        // then
        assertThat( resultAttemptList2 ).isEqualTo( resultAttemptList );
    }
}