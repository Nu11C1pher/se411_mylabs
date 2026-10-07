public class SearchById implements SearchStrategy<Item> {
    private final int id;

    public SearchById(int id) {
        this.id = id;
    }

    @Override
    public boolean matches(Item item) {
        return item.getId() == id;
    }
}