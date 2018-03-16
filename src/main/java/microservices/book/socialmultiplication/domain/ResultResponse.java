package microservices.book.socialmultiplication.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Getter
public class ResultResponse  implements Serializable {
    private final boolean correct;
}
