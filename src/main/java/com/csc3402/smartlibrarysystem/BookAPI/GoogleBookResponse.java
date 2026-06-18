package com.csc3402.smartlibrarysystem.BookAPI;

import java.util.List;

public class GoogleBookResponse {
    public List<Item> items;

    public static class Item {
        public VolumeInfo volumeInfo;
    }

    public static class VolumeInfo {
        public String title;
        public List<String> authors;
        public String description; // Maps to SYNOPSIS
        public List<String> categories; // Maps to GENRE
        public Double averageRating; // Maps to AVG_RATING
        public ImageLinks imageLinks;
    }

    public static class ImageLinks {
        public String thumbnail; // Maps to COVER_URL
    }
}