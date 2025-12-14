package ru.Factorio_Blueprint.dev;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.Factorio_Blueprint.models.Guide;
import ru.Factorio_Blueprint.models.GuideLike;
import ru.Factorio_Blueprint.models.User;
import ru.Factorio_Blueprint.repositories.GuideLikeRepository;
import ru.Factorio_Blueprint.repositories.GuideRepository;
import ru.Factorio_Blueprint.repositories.UserRepository;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

//ИМЕННО ТАК РАБОТАЕТ НАКРУТКА ПРОСМОТРОВ В ФКонтакте

@Component
@Profile("dev")
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final GuideRepository guideRepository;
    private final GuideLikeRepository guideLikeRepository;

    public DataSeeder(UserRepository userRepository,
                      GuideRepository guideRepository,
                      GuideLikeRepository guideLikeRepository) {
        this.userRepository = userRepository;
        this.guideRepository = guideRepository;
        this.guideLikeRepository = guideLikeRepository;
    }

    @Override
    public void run(String... args) {

        Random random = new Random();

        if (userRepository.count() > 10) {
            System.out.println("Seeder: данные присутствуют, не заполняем");
            return;
        }

        System.out.println("Seeder: создание пользователей");
        List<User> users = userRepository.saveAll(
                IntStream.rangeClosed(1, 100)
                        .mapToObj(i -> {
                            User u = new User();
                            u.setUsername("user" + i);
                            u.setEmail("user" + i + "@test.com");
                            u.setPassword("{noop}password");
                            return u;
                        })
                        .toList()
        );

        System.out.println("Seeder: создание гайдов");
        List<Guide> guides = IntStream.rangeClosed(1, 100)
                .mapToObj(i -> {
                    Guide g = new Guide();
                    g.setTitle("Гайд #" + i);
                    g.setCategory("Category " + (i % 5));
                    g.setDescription("Описание гайда #" + i);
                    g.setAuthor(users.get(i % users.size()));
                    return g;
                })
                .map(guideRepository::save)
                .toList();

        System.out.println("Seeder: накрутка лайков");
        for (Guide guide : guides) {
            int likesCount = random.nextInt(50); // 0–49 лайков

            for (int i = 0; i < likesCount; i++) {
                User user = users.get(random.nextInt(users.size()));

                if (!guideLikeRepository.existsByGuide_IdAndUser_Id(
                        guide.getId(), user.getId())) {

                    guideLikeRepository.save(
                            new GuideLike(guide, user)
                    );
                }
            }
        }

        System.out.println("Seeder: готово ");
    }
}
