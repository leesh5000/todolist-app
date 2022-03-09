package leesh.backend;

import leesh.backend.entity.Post;
import leesh.backend.entity.User;
import leesh.backend.repository.PostRepository;
import leesh.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class initDb {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner loadInitData() {
        return args -> {
            log.info("init db start...");

            List<User> users = new ArrayList<>();
            List<Post> posts = new ArrayList<>();

            for (int i=0; i<10; i++) {

                User user = User.builder()
                        .username("test" + i)
                        .password(passwordEncoder.encode("1111"))
                        .build();

                users.add(user);

                for (int j=0; j<50; j++) {
                    Set<String> tag = new HashSet<>();
                    tag.add(String.valueOf((int) (Math.random() * 100)));
                    tag.add(String.valueOf((int) (Math.random() * 100)));
                    tag.add(String.valueOf((int) (Math.random() * 100)));
                    Post post = Post.write("title" + j, "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut" +
                            " labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris" +
                            " nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit" +
                            " esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in" +
                            " culpa qui officia deserunt mollit anim id est laborum." + j, tag, user);

                    posts.add(post);
                }
            }

            userRepository.saveAll(users);
            postRepository.saveAll(posts);

            log.info("init db end...");
        };
    }
}
