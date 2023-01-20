package be;

public class CategoryToMovie {

    private int ID;

    private int categoryID;
    private int movieID;

    public int getCategoryToMovieID() {
        return ID;
    }

    public void setCategoryToMovieID(int ID) {
        this.ID = ID;
    }

    public CategoryToMovie(int Id, int categoryID, int movieID) {
        this.categoryID = categoryID;
        this.movieID = movieID;
        this.ID= Id;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public int getMovieID() {
        return movieID;
    }

    public void setMovieID(int movieID) {
        this.movieID = movieID;
    }
}
