package microservices.book.socialmultiplication.service;

import microservices.book.socialmultiplication.domain.Multiplication;
import microservices.book.socialmultiplication.domain.MultiplicationResultAttempt;
import microservices.book.socialmultiplication.domain.User;
import microservices.book.socialmultiplication.repository.MultiplicationResultAttemptRepository;
import microservices.book.socialmultiplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Service
public class MultiplicationServiceImpl implements MultiplicationService {

    private RandomGeneratorService randomGeneratorService;
    private MultiplicationResultAttemptRepository multiplicationResultAttemptRepository;
    private UserRepository userRepository;

    @Autowired
    public MultiplicationServiceImpl( final RandomGeneratorService randomGeneratorService,
                                      final MultiplicationResultAttemptRepository multiplicationResultAttemptRepository,
                                      final UserRepository userRepository ) {
        this.randomGeneratorService = randomGeneratorService;
        this.multiplicationResultAttemptRepository = multiplicationResultAttemptRepository;
        this.userRepository = userRepository;
    }
    @Override
    public Multiplication createRandomMultiplication() {
        int factorA = randomGeneratorService.generateRandomFactor();
        int factorB = randomGeneratorService.generateRandomFactor();

        return new Multiplication( factorA, factorB );
    }

    @Override
    public boolean checkAttempt( MultiplicationResultAttempt resultAttempt ) {
        Assert.isTrue( !resultAttempt.isCorrect(), "You can't send attempt marked as correct!!" );

        //check if the user already exists for that alias
        Optional<User > optionalUser = userRepository.findByAlias( resultAttempt.getUser().getAlias() );

        boolean correct = resultAttempt.getResultAttempt() ==
                resultAttempt.getMultiplication().getFactorA() *
                resultAttempt.getMultiplication().getFactorB();

        MultiplicationResultAttempt checkedAttempt = new MultiplicationResultAttempt( optionalUser.orElse( resultAttempt.getUser() ),
                                                                                      resultAttempt.getMultiplication(),
                                                                                      resultAttempt.getResultAttempt(), correct );

        return correct;
    }

    @Override
    public List< MultiplicationResultAttempt > findTop5ByUserAliasOrderByIdDesc( String userAlias ) {
        return multiplicationResultAttemptRepository.findTop5ByUserAliasOrderByIdDesc( userAlias );
    }
}
