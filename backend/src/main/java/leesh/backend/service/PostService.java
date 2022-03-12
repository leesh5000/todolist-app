package leesh.backend.service;

import leesh.backend.dto.*;
import leesh.backend.entity.Post;
import leesh.backend.entity.PostTag;
import leesh.backend.entity.Tag;
import leesh.backend.entity.User;
import leesh.backend.exception.CustomException;
import leesh.backend.exception.ErrorCode;
import leesh.backend.repository.PostRepository;
import leesh.backend.repository.PostTagRepository;
import leesh.backend.repository.TagRepository;
import leesh.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
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
    private final TagRepository tagRepository;
    private final PostTagRepository postTagRepository;

    @Transactional(readOnly = false)
    public PostWriteResponseDto write(PostWriteRequestDto requestDto) {

        final String title = requestDto.getTitle();
        final String body = requestDto.getBody();

        // 현재 로그인 유저 조회 (글 작성자)
        final User loginUser = this.getLoginUser();

        // Tag 생성
        List<Tag> tags = requestDto.getTags().stream()
                .map(inputTagName -> Tag.createTag(inputTagName))
                .collect(Collectors.toList());
        tagRepository.saveAll(tags);

        // Post 생성
        Post newPost = Post.createPost(title, body, loginUser);
        postRepository.save(newPost);

        // PostTag 생성
        List<PostTag> postTags = tags.stream()
                .map(tag -> PostTag.createPostTag(newPost, tag))
                .collect(Collectors.toList());

        return PostWriteResponseDto.toDto(newPost);
    }

    @Transactional(readOnly = false)
    public PostUpdateResponseDto edit(PostUpdateRequestDto requestDto, long id) {

        Post editPost = postRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_RESOURCE));
        User loginUser = this.getLoginUser();
        checkOwnPost(editPost, loginUser);

        // 기존에 존재하지 않는 태그들은 새로 만들기
        List<Tag> newTags = requestDto.getTags().stream()
                .filter(inputTagName -> !tagRepository.existsTagByTagName(inputTagName))
                .map(notExistTagName -> Tag.createTag(notExistTagName))
                .collect(Collectors.toList());
        tagRepository.saveAll(newTags);

        // 수정할 태그들 조회
        List<Tag> editTags = requestDto.getTags().stream()
                .map(inputTagName -> tagRepository.findByTagName(inputTagName))
                .collect(Collectors.toList());

        // 기존에 존재하지 않는 PostTag 새로 생성
        List<PostTag> newPostTag = editTags.stream()
                .filter(editTag -> !postTagRepository.existsByPostAndTag(editPost, editTag))
                .map(editTag -> PostTag.createPostTag(editPost, editTag))
                .collect(Collectors.toList());

        editPost.edit(requestDto.getTitle(), requestDto.getBody(), newPostTag);

        // 기존에 없는 태그는 생성
        return PostUpdateResponseDto.toDto(editPost);
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
        List<Post> allByUser = postRepository.findByUsername(username);
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
