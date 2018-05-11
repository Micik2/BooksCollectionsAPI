package com.example.index;

import com.google.gson.GsonBuilder;
import com.google.gson.Gson;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;

import spark.Spark;
import spark.servlet.SparkApplication;
import java.io.*;
import java.util.*;
import static spark.Spark.*;

public class Main {
    
    public static Book getBookByISBN(ArrayList<Book> b, String i) {
        Book singleBook = new Book();
        int sizeOfBooks = b.size();
        for (int k = 0; k < sizeOfBooks; k++) {
            singleBook = b.get(k);
            if (i.equals(singleBook.getIsbn()))
                return singleBook;
            else if (i.equals(singleBook.getId()))
                return singleBook;
        }
        return null;
    }
    
    public static ArrayList<Book> getBooksByCategory(ArrayList<Book> b, String c) {
        ArrayList<Book> categoryBooks = new ArrayList<Book>();
        int sizeOfBooks = b.size();
        for (int m = 0; m < sizeOfBooks; m++) {
            Book singleBook = b.get(m);
            int numberCategories = singleBook.getCategories().size();
            for (int n = 0; n < numberCategories; n++) {
                String category = singleBook.getCategories().get(n);
                if (c.equals(category))
                    categoryBooks.add(singleBook);
            }
        }
        return categoryBooks;
    }

    public static Map<String, Double> getRatings(ArrayList<Book> b) {
        Map<String, Double> authorsRating = new HashMap<String, Double>();
        int sizeOfBooks = b.size();
        for (int o = 0; o < sizeOfBooks; o++) {
            Book singleBook = b.get(o);
            double averageRating = singleBook.getAverageRating();
            ArrayList<String> authorsSingleBook = new ArrayList<String>();
            if (averageRating != 0.0) {
                authorsSingleBook = singleBook.getAuthors();
                String a = "";
                for (String authorSingleBook : authorsSingleBook) {
                    a = authorSingleBook;
                    if (!a.equals(""))
                        authorsRating.put(a, averageRating);
                }
            }
        }
        return authorsRating;
    }
    
    public static void main(String[] args) {
        String id;
        String isbn = "";
        String title;
        String subtitle = "";
        String publisher = "";
        String publishedDate = "";
        String description = "";
        int pageCount = 0;
        String thumbnailUrl;
        String language;
        String previewLink;
        double averageRating = 0.0;
        ArrayList<String> authors;
        ArrayList<String> categories;
        ArrayList<Book> books = new ArrayList<Book>();
        Book book = new Book();

        port(8080);
        String filePath = args[0];
        
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));
            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = (JsonObject) jsonParser.parse(bufferedReader);
            JsonArray items = jsonObject.getAsJsonArray("items");

            for (JsonElement item : items) {
                authors = new ArrayList<String>();
                categories = new ArrayList<String>();
                
                JsonObject JObook = item.getAsJsonObject();
                id = JObook.get("id").getAsString();
                JsonObject volumeInfo = JObook.getAsJsonObject("volumeInfo");
                title = volumeInfo.get("title").getAsString();
                
                if (volumeInfo.has("subtitle"))
                    subtitle = volumeInfo.get("subtitle").getAsString();
                if (volumeInfo.has("publisher"))
                    publisher = volumeInfo.get("publisher").getAsString();
                if (volumeInfo.has("publishedDate"))
                    publishedDate = volumeInfo.get("publishedDate").getAsString();
                if (volumeInfo.has("description"))
                    description = volumeInfo.get("description").getAsString();
                if (volumeInfo.has("pageCount"))
                    pageCount = volumeInfo.get("pageCount").getAsInt();

                JsonArray industryIdentifiers = volumeInfo.getAsJsonArray("industryIdentifiers");
                for (JsonElement industryIdentifier : industryIdentifiers) {
                    JsonObject iI = industryIdentifier.getAsJsonObject();
                    if (iI.get("type").getAsString().equals("ISBN_13")) {
                        isbn = iI.get("identifier").getAsString();
                        break;
                    }
                    else
                        isbn = null;
                }

                JsonObject imageLinks = volumeInfo.getAsJsonObject("imageLinks");
                thumbnailUrl = imageLinks.get("thumbnail").getAsString();
                language = volumeInfo.get("language").getAsString();
                previewLink = volumeInfo.get("previewLink").getAsString();
                if (volumeInfo.has("averageRating"))
                    averageRating = volumeInfo.get("averageRating").getAsDouble();

                JsonArray JAauthors = volumeInfo.getAsJsonArray("authors");
                if (JAauthors != null) {
                    for (JsonElement JAauthor : JAauthors)
                        authors.add(JAauthor.getAsString());
                }

                JsonArray JAcategories = volumeInfo.getAsJsonArray("categories");
                if (JAcategories != null) {
                    for (JsonElement JAcategory : JAcategories)
                        categories.add(JAcategory.getAsString());
                }
                
                book = new Book(id, isbn, title, subtitle, publisher, publishedDate, description, pageCount, thumbnailUrl, language, previewLink, averageRating, authors, categories);
                books.add(book);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        
        get("/api/book/:isbn", (req, res) -> {
            Book b = new Book();
            String isbnId = req.params(":isbn");
            Type type = new TypeToken<Book>() {}.getType();
            b = getBookByISBN(books, isbnId);
            String json = gson.toJson(b, type);
            if (b != null)
                return json;
            res.status(404);
            return "No results found";
        });
        
        get("/api/category/:category/books", (req, res) -> {
            String categoryName = req.params(":category");
            Type type = new TypeToken<ArrayList<Book>>() {}.getType();
            ArrayList<Book> cB = getBooksByCategory(books, categoryName);
            String json = gson.toJson(cB, type);
            if (cB != null)
                return json;
            else
                return new ArrayList<Book>();
        });
        
        get("/api/rating", (req, res) -> {
            Type type = new TypeToken<HashMap<String, Double>>() {}.getType();
            Map<String, Double> aR = getRatings(books);
            String json = gson.toJson(aR, type);
            if (aR != null)
                return json;
            else 
                return new ArrayList<Book>();
        });
        
    }
}
