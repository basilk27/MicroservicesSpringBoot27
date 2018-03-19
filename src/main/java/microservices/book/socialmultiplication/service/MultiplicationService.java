package microservices.book.socialmultiplication.service;

import microservices.book.socialmultiplication.domain.Multiplication;
import microservices.book.socialmultiplication.domain.MultiplicationResultAttempt;

import java.util.List;

public interface MultiplicationService {
    Multiplication createRandomMultiplication();
    boolean checkAttempt( MultiplicationResultAttempt resultAttempt );
    List<MultiplicationResultAttempt> findTop5ByUserAliasOrderByIdDesc(String userAlias);
}
