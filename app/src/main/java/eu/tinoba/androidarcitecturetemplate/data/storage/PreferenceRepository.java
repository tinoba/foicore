package eu.tinoba.androidarcitecturetemplate.data.storage;

public interface PreferenceRepository {

    void setUserId(long userId);

    long getUserId();

    void setAccessToken(String token);

    String getAccessToken();
}
