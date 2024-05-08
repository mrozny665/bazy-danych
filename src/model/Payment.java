package model;

public class Payment {
    public long id;
    public long clientId;
    public double payment;

    public Payment(long id, long clientId, double payment) {
        this.id = id;
        this.clientId = clientId;
        this.payment = payment;
    }
}
