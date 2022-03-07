package leesh.backend.controller;

import leesh.backend.dto.PostListResponse;
import leesh.backend.dto.PostWriteRequest;
import leesh.backend.dto.PostWriteResponse;
import leesh.backend.entity.Post;
import leesh.backend.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;

    @PostMapping(value = "/api/posts")
    public ResponseEntity<PostWriteResponse> write(@RequestBody PostWriteRequest requestDto) {

        Post savedPost = postService.write(requestDto);

        PostWriteResponse responseDto = PostWriteResponse.builder()
                .title(savedPost.getTitle())
                .body(savedPost.getBody())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping(value = "/api/posts")
    public ResponseEntity<PostListResponse> list() {



        return null;
    }


}
