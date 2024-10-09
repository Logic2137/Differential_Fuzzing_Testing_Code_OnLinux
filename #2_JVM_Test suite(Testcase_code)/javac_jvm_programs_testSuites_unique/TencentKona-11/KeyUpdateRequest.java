

public enum KeyUpdateRequest {

    NOT_REQUESTED("update_not_requested"),
    REQUESTED("update_requested");

    public String name;

    KeyUpdateRequest(String name) {
        this.name = name;
    }
}
