package model;

public class Client {
    public long id;
    public String firstName;
    public String lastName;
    public String phoneNumber;

    public Client(long id, String firstName, String lastName, String phoneNumber) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }
}
