package mate.academy.bookstoreapp.security;

public interface Authentication {
    Object getPrincipal();

    Object getCredentials();
}
