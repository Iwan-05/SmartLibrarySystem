package com.csc3402.smartlibrarysystem.BookAPI;

import com.csc3402.smartlibrarysystem.model.Book;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GoogleBooksService {


    private final String GOOGLE_API_KEY = "AIzaSyAf5jsslkQexUruVHkVh-s1iGPGdkYGjlM";

    // 2. Modified URL to accept the key at the end
    private final String GOOGLE_BOOKS_URL = "https://www.googleapis.com/books/v1/volumes?q=:";

    public Book fetchBookByIsbn(String searchQuery) {
        RestTemplate restTemplate = new RestTemplate();

        String safeQuery = searchQuery.trim().replace(" ", "+");

        String url = GOOGLE_BOOKS_URL + safeQuery + "&key=" + GOOGLE_API_KEY;

        try {
            GoogleBookResponse response = restTemplate.getForObject(url, GoogleBookResponse.class);

            if (response != null && response.items != null && !response.items.isEmpty()) {
                GoogleBookResponse.VolumeInfo info = response.items.get(0).volumeInfo;

                Book book = new Book();
                book.setTitle(info.title);

                if (info.authors != null && !info.authors.isEmpty()) {
                    book.setAuthor(String.join(", ", info.authors));
                }

                if (info.categories != null && !info.categories.isEmpty()) {
                    book.setGenre(String.join(", ", info.categories));
                }

                if (info.description != null) {
                    String cleanSynopsis = info.description;
                    if (cleanSynopsis.length() > 3900) {
                        cleanSynopsis = cleanSynopsis.substring(0, 3900) + "...";
                    }
                    book.setSynopsis(cleanSynopsis);
                }

                if (info.imageLinks != null && info.imageLinks.thumbnail != null) {
                    book.setCover_url(info.imageLinks.thumbnail.replace("http://", "https://"));
                }

                book.setAvg_rating(info.averageRating != null ? info.averageRating : 0.0);
                book.setStatus("Available");

                return book;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}