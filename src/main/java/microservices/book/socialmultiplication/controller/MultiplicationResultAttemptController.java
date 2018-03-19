package microservices.book.socialmultiplication.controller;

import microservices.book.socialmultiplication.domain.MultiplicationResultAttempt;
import microservices.book.socialmultiplication.domain.ResultResponse;
import microservices.book.socialmultiplication.service.MultiplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    ResponseEntity<List<MultiplicationResultAttempt>> getStatistics(@RequestParam("alias") String alias) {
        return ResponseEntity.ok(multiplicationService.findTop5ByUserAliasOrderByIdDesc( alias ));
    }
}
