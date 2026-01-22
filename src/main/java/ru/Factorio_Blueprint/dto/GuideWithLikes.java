package ru.Factorio_Blueprint.dto;

public class GuideWithLikes {

    private final Long id;
    private final String title;
    private final String category;
    private final long likeCount;

    public GuideWithLikes(Long id, String title, String category, long likeCount) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.likeCount = likeCount;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getCategory() { return category; }
    public long getLikeCount() { return likeCount; }
}
