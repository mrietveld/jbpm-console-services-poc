package org.jbpm.example.code;

public class FluentBuilderApi {

    private final String name;
    private final String connectionUrl;
    private final String driver;
    private final String username;
    private final String password;

    // Constructors ----------------------------------------------------------

    /**
     * Create a new DataSource.
     * 
     * @param dataSourceBuilder
     */
    private FluentBuilderApi(FluentObjectBuilder dataSourceBuilder) {
        this.name = dataSourceBuilder.name;
        this.connectionUrl = dataSourceBuilder.connectionUrl;
        this.driver = dataSourceBuilder.driver;
        this.username = dataSourceBuilder.username;
        this.password = dataSourceBuilder.password;
    }

    // Public methods --------------------------------------------------------

    /**
     * Get the name.
     * 
     * @return the name.
     */
    public String getName() {
        return name;
    }

    public String getJndiName() {
        return "java:jboss/datasources/" + name;
    }

    /**
     * Get the connectionUrl.
     * 
     * @return the connectionUrl.
     */
    public String getConnectionUrl() {
        return connectionUrl;
    }

    /**
     * Get the driver.
     * 
     * @return the driver.
     */
    public String getDriver() {
        return driver;
    }

    /**
     * Get the username.
     * 
     * @return the username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Get the password.
     * 
     * @return the password.
     */
    public String getPassword() {
        return password;
    }

    // Embedded classes ------------------------------------------------------

    public static class FluentObjectBuilder {
        private String name;
        private String connectionUrl;
        private String driver;
        private String username;
        private String password;

        public FluentObjectBuilder name(String name) {
            this.name = name;
            return this;
        }

        public FluentObjectBuilder connectionUrl(String connectionUrl) {
            this.connectionUrl = connectionUrl;
            return this;
        }

        public FluentObjectBuilder driver(String driver) {
            this.driver = driver;
            return this;
        }

        public FluentObjectBuilder username(String username) {
            this.username = username;
            return this;
        }

        public FluentObjectBuilder password(String password) {
            this.password = password;
            return this;
        }

        public FluentBuilderApi build() {
            return new FluentBuilderApi(this);
        }
    }

}