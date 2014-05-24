package at.schett.githubexample.githubapp.restapi;

import java.util.List;

import at.schett.githubexample.githubapp.model.Repository;
import at.schett.githubexample.githubapp.model.User;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by Schett on 23.05.2014.
 */
public interface GitHubApiService {

    // Gett all users from github
    @GET("/users")
    void getUsers(Callback<List<User>> userList);

    // Get the repositories for a user
    @GET("/users/{user}/repos")
    void getRepositoriesForUser(@Path("user") String userName, Callback<List<Repository>> callback);

}
