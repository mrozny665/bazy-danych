package model;

import java.time.LocalDate;

public class Rental {
    public long id;
    public LocalDate start;
    public LocalDate end;
    public long dvdId;
    public long clientId;

    public Rental(long id, LocalDate start, LocalDate end, long dvdId, long clientId) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.dvdId = dvdId;
        this.clientId = clientId;
    }
}
