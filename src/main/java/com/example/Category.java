package com.example;

public enum Category {
    FICTION("Fiction", "Imaginative narratives and stories"),
    NON_FICTION("Non-Fiction", "Factual and informational books"),
    SCIENCE_FICTION("Science Fiction", "Futuristic and speculative fiction"),
    MYSTERY("Mystery", "Crime, detective, and suspenseful stories"),
    BIOGRAPHY("Biography", "Life stories and memoirs"),
    HISTORY("History", "Historical events and periods"),
    FANTASY("Fantasy", "Magical and supernatural worlds"),
    ROMANCE("Romance", "Love stories and relationships"),
    THRILLER("Thriller", "Suspenseful and exciting narratives"),
    CLASSIC("Classic", "Timeless literary works");

    private final String displayName;
    private final String description;

    Category(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
