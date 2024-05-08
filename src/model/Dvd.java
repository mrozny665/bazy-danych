package model;

public class Dvd {
    public long id;
    public boolean availability;
    public String title;
    public long filmSerialid;
    public boolean isFilm;
    public int seasonNumber;

    public Dvd(long id, boolean availability, String title, long filmSerialid, boolean isFilm, int seasonNumber) {
        this.id = id;
        this.availability = availability;
        this.title = title;
        this.filmSerialid = filmSerialid;
        this.isFilm = isFilm;
        this.seasonNumber = seasonNumber;
    }
}
