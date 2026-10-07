public class SearchByAuthor implements SearchStrategy<Item> {
    private final String authorName;

    public SearchByAuthor(String authorName) {
        this.authorName = authorName;
    }

    @Override
    public boolean matches(Item item) {
        if (item instanceof Book) {
            Book book = (Book) item;
            return book.getAuthorName().equalsIgnoreCase(authorName);
        }
        return false;
    }
}