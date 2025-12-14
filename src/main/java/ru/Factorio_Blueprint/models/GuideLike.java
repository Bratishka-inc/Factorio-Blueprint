package ru.Factorio_Blueprint.models;

import jakarta.persistence.*;

@Entity
@Table(
        name = "guide_likes",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"guide_id", "user_id"})
        }
)
public class GuideLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Связь с гайдом
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guide_id", nullable = false)
    private Guide guide;

    // Кто именно лайкнул
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public GuideLike() {
    }

    public GuideLike(Guide guide, User user) {
        this.guide = guide;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public Guide getGuide() {
        return guide;
    }

    public void setGuide(Guide guide) {
        this.guide = guide;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
