package mate.academy.bookstoreapp.security;

public class UsernamePasswordAuthenticationToken implements Authentication {
    private String username;
    private String password;

    public UsernamePasswordAuthenticationToken(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public Object getPrincipal() {
        return username;
    }

    @Override
    public Object getCredentials() {
        return password;
    }
}
