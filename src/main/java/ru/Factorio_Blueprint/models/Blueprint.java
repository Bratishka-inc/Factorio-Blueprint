package ru.Factorio_Blueprint.models;

import jakarta.persistence.*;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Pattern;
import ru.Factorio_Blueprint.models.Category;
import ru.Factorio_Blueprint.models.User;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "blueprints")
public class Blueprint implements Serializable {
    private static final long serialVersionUID = 1L;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 2000)
    private String description;

    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "Код чертежа может содержать только латинские буквы и цифры без пробелов")
    @Column(name = "blueprint", length = 8000)
    private String blueprintString;

    private String imageUrl;

    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Blueprint() {}

    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getBlueprintString() { return blueprintString; }
    public void setBlueprintString(String blueprintString) { this.blueprintString = blueprintString; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

}
