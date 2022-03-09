package leesh.backend.controller;

import leesh.backend.dto.*;
import leesh.backend.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;

    @PostMapping(value = "/api/posts")
    public ResponseEntity<PostWriteResponseDto> write(@RequestBody @Validated PostWriteRequestDto requestDto) {
        PostWriteResponseDto body = postService.write(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    // /api/posts?username={username}&tag={tag}&page={page}&size={size}
    @GetMapping(value = "/api/posts")
    public ResponseEntity<List<PostGetResponseDto>> list(@RequestParam(value = "username", required = false) String username,
                                                         @RequestParam(value = "tags", required = false) Set<String> tags,
                                                         @PageableDefault(page = 0, size = 10) Pageable pageable) {

        List<PostGetResponseDto> page = postService.list(username, tags, pageable);

//        List<PostGetResponseDto> body = page.getContent().stream()
//                .map(PostGetResponseDto::toDto)
//                .collect(Collectors.toList());
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Last-Page", String.valueOf(page.getTotalPages()));
//
        return ResponseEntity.status(HttpStatus.OK).body(page);
//                .headers()
//                .body(page);
    }

    @GetMapping(value = "/api/posts/{id}")
    public ResponseEntity<PostGetResponseDto> get(@PathVariable("id") long id) {
        PostGetResponseDto body = postService.get(id);
        return ResponseEntity.status(HttpStatus.OK).body(body);

    }

    @PatchMapping(value = "/api/posts/{id}")
    public ResponseEntity<PostUpdateResponseDto> edit(@RequestBody @Validated PostUpdateRequestDto requestDto, @PathVariable("id") long id) {
        PostUpdateResponseDto body = postService.edit(requestDto, id);
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @DeleteMapping(value = "/api/posts/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        postService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
