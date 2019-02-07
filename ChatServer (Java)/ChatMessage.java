import java.io.Serializable;

final class ChatMessage implements Serializable {
    private static final long serialVersionUID = 6898543889087L;

    private int i;
    private String recipient;
    private String message;

    public ChatMessage(int i, String message, String recipient) {
        this.i = i;
        this.message = message;
        this.recipient = recipient;
    }

    public int getMessageType() {
        return i;
    }

    public String getMessage() {
        return message;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
