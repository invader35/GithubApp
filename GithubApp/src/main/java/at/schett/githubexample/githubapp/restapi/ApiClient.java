package at.schett.githubexample.githubapp.restapi;

import at.schett.githubexample.githubapp.constants.Constants;
import retrofit.RestAdapter;

/**
 * Created by Schett on 23.05.2014.
 */
public class ApiClient {
    private GitHubApiService apiService;
    private static ApiClient apiClient;

    public GitHubApiService getApiClient() {
        if (apiService == null) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(Constants.URLEndpoint)
                    .build();

            apiService = restAdapter.create(GitHubApiService.class);
        }
        return apiService;
    }

    /**
     * Returns a singleton instance of the api client
     *
     * @return a singleton instance of the api client
     */
    public static ApiClient getInstance() {
        if(apiClient == null) {
            apiClient = new ApiClient();
        }
        return apiClient;
    }
}
