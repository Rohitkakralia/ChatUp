package com.chat.chattingApp.entities;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user")
@Data
@NoArgsConstructor // Required for MongoDB to recreate the object
public class Users {

    @Id
    private ObjectId id;

    private String name;

    @NonNull
    @Indexed(unique = true) // Ensures no two people use the same email
    private String email;

    @NonNull
    private String password;

    @DBRef(lazy = true) // 'lazy = true' prevents loading all friends unless you actually call .getFriends()
    private List<Users> friends = new ArrayList<>(); // Initialize to avoid NullPointerException



    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName( String name) {
        this.name = name;
    }

    public @NonNull String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    public @NonNull String getPassword() {
        return password;
    }

    public void setPassword(@NonNull String password) {
        this.password = password;
    }

    public List<Users> getFriends() {
        return friends;
    }

    public void addFriend(Users friend) {
        if (this.friends == null) {
            this.friends = new ArrayList<>();
        }
        this.friends.add(friend);
    }

}
