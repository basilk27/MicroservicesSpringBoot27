package microservices.book.socialmultiplication.controller;

import microservices.book.socialmultiplication.domain.MultiplicationResultAttempt;
import microservices.book.socialmultiplication.domain.ResultResponse;
import microservices.book.socialmultiplication.service.MultiplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/results")
public class MultiplicationResultAttemptController {

    @Autowired
    private MultiplicationService multiplicationService;

    @PostMapping
    ResponseEntity<MultiplicationResultAttempt> postResult( @RequestBody MultiplicationResultAttempt resultAttempt ) {
        boolean isCorrect = multiplicationService.checkAttempt( resultAttempt );
        return ResponseEntity.ok( new MultiplicationResultAttempt( resultAttempt.getUser(), resultAttempt.getMultiplication(),
                                                                    resultAttempt.getResultAttempt(), isCorrect) );
    }
}
