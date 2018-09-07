package isel.leic.ps.eduWikiAPI.configuration.persistence;

/**
 * TheadLocal that stores the schema of the current request in the thread handling the request
 * and the threads delegated by it as well
 */
public class TenantContext {
    private static ThreadLocal<String> tenantSchema = new ThreadLocal<>();

    public static String getTenantSchema() {
        return tenantSchema.get();
    }

    public static void setTenantSchema(String schema) {
        tenantSchema.set(schema);
    }

    public static void resetTenantSchema() {
        tenantSchema.set(null);
    }
}