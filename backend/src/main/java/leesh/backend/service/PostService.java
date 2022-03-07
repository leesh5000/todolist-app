package leesh.backend.service;

import leesh.backend.dto.PostWriteRequest;
import leesh.backend.entity.Post;
import leesh.backend.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;

    public Post write(PostWriteRequest requestDto) {

        return Post.builder()
                .title(requestDto.getTitle())
                .body(requestDto.getBody())
                .build();
    }

}
