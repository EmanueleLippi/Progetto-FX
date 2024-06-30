package Login;

public class Utente {
    private String username;
    private String email;
    private String password;
    private int[] score = {0,0,0,0,0,0,0,0,0};
    private boolean available = false;

    public Utente(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password; 
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password; 
    }

    public String toString() {
        return "Welcome " + this.username;
    }

    public int[] getScore() {
        return this.score;
    }

    public boolean isAvailable() {
        return this.available;
    }

    public void setScore(int indx) {
        this.score[indx] = 1;
    }

    public void setAvailable() {
        this.available = !available;
    }

    public String onFile(){
        return this.username + "," + this.email + "," + this.password+","+score[0]+","+score[1]+","+score[2]+","+score[3]+","+score[4]+","+score[5]+","+score[6]+","+score[7]+","+score[8];
    }
}
