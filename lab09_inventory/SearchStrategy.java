public interface SearchStrategy<T> {
    boolean matches(T item);
}