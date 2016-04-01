package pt.askflow.util;

/**
 * Created by PhucThanh on 1/20/2016.
 */
public class Contact {
    private int avatar;
    private String name;

    public Contact(int avatar, String name) {
        this.avatar = avatar;
        this.name = name;
    }

    public Contact() {
    }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
