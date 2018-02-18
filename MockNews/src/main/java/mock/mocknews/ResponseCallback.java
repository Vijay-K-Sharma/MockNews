package mock.mocknews;

public interface ResponseCallback {
    void onSuccess(String response);
    void onUpdate(int state, int index);
    void onFailure(String errorMessage);
}
