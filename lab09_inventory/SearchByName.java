public class SearchByName implements SearchStrategy<Item> {
    private final String name;

    public SearchByName(String name) {
        this.name = name;
    }

    @Override
    public boolean matches(Item item) {
        return item.getName().equalsIgnoreCase(name);
    }
}