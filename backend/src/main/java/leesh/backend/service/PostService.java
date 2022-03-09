package leesh.backend.service;

import leesh.backend.dto.*;
import leesh.backend.entity.Post;
import leesh.backend.entity.User;
import leesh.backend.exception.CustomException;
import leesh.backend.exception.ErrorCode;
import leesh.backend.repository.PostRepository;
import leesh.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static leesh.backend.exception.ErrorCode.NO_PERMISSION;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = false)
    public PostWriteResponseDto write(PostWriteRequestDto requestDto) {

        User loginUser = this.getLoginUser();

        Post newPost = postRepository.save(Post.write(requestDto.getTitle(), requestDto.getBody(), requestDto.getTags(), loginUser));

        return PostWriteResponseDto.builder()
                .id(newPost.getId())
                .title(newPost.getTitle())
                .body(newPost.getBody())
                .author(newPost.getUser().getUsername())
                .tag(newPost.getTag())
                .publishedDate(newPost.getCreatedDate())
                .build();
    }

    @Transactional(readOnly = false)
    public PostUpdateResponseDto edit(PostUpdateRequestDto requestDto, long id) {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_RESOURCE));
        User loginUser = this.getLoginUser();
        checkOwnPost(post, loginUser);

        post.edit(requestDto.getTitle(), requestDto.getBody(), requestDto.getTags());

        return PostUpdateResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .body(post.getBody())
                .author(post.getUser().getUsername())
                .tag(post.getTag())
                .publishedDate(post.getCreatedDate())
                .lastModifiedDate(post.getModifiedDate())
                .build();
    }

    private User getLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginUsername = (String) authentication.getPrincipal();
        return userRepository.findByUsername(loginUsername)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_RESOURCE));
    }

    private void checkOwnPost(Post post, User user) {
        if (!post.getUser().getId().equals(user.getId())) {
            throw new CustomException(NO_PERMISSION);
        }
    }

    public void delete(long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_RESOURCE));
        User loginUser = this.getLoginUser();
        checkOwnPost(post, loginUser); // 글 작성자만 게시글 수정 삭제 가능
        postRepository.delete(post);
    }

    public List<PostGetResponseDto> list(String username, Set<String> tags, @NotNull Pageable pageable) {

        log.info("list query start");
        List<Post> allByUser = postRepository.findByUsername(username, pageable);
        log.info("list query end");

        log.info("convert query start");
        List<PostGetResponseDto> body = allByUser.stream()
                .map(PostGetResponseDto::toDto)
                .collect(Collectors.toList());
        log.info("convert query end");

        return body;
    }

    public PostGetResponseDto get(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_RESOURCE));

        return PostGetResponseDto.toDto(post);
    }
}
