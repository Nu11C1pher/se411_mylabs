public class SearchByCategory implements SearchStrategy<Item> {
    private final String category;

    public SearchByCategory(String category) {
        this.category = category;
    }

    @Override
    public boolean matches(Item item) {
        if (item instanceof ElectronicDevice) {
            ElectronicDevice device = (ElectronicDevice) item;
            return device.getCategory().equalsIgnoreCase(category);
        }
        return false;
    }
}