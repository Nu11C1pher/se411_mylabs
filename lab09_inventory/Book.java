public class Book extends Item {
    private String authorName;

    public Book(int id, String name, String authorName) {
        super(id, name);
        this.authorName = authorName;
    }

    public String getAuthorName() {
        return authorName;
    }

    @Override
    public String toString() {
        return "Book{id=" + getId() + ", name='" + getName()
                + "', author='" + authorName + "'}";
    }
}