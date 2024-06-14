package Login;

public class Utente {
    private String username;
    private String email;

    public Utente(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String toString(){
        return "Welcome "+this.username;
    }
}
