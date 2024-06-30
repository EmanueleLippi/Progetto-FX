package Login;

import java.io.File;
import java.util.Scanner;

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

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
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

    public void loadFile(String user,String email, String password) {
        try {
            Scanner scan = new Scanner(new File("Learn - program/src/Data/users.csv")); // Apre il file users.csv per la lettura
            while (scan.hasNextLine()){
                String[] line = scan.nextLine().split(",");
                if (line[0].equals(user) && line[1].equals(password)) {
                    this.email = line[2];
                    this.score[0] = Integer.parseInt(line[3]);
                    this.score[1] = Integer.parseInt(line[4]);
                    this.score[2] = Integer.parseInt(line[5]);
                    this.score[3] = Integer.parseInt(line[6]);
                    this.score[4] = Integer.parseInt(line[7]);
                    this.score[5] = Integer.parseInt(line[8]);
                    this.score[6] = Integer.parseInt(line[9]);
                    this.score[7] = Integer.parseInt(line[10]);
                    this.score[8] = Integer.parseInt(line[11]);
                    break;
                }
            }
            scan.close();
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Errorex: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
