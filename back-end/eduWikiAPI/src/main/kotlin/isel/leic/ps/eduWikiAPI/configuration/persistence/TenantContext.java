package isel.leic.ps.eduWikiAPI.configuration.persistence;

public class TenantContext {
    private static ThreadLocal<String> tenantSchema = new ThreadLocal<>();

    public static String getTenantSchema() {
        return tenantSchema.get();
    }

    public static void setTenantSchema(String uuid) {
        tenantSchema.set(uuid);
    }

    public static void resetTenantSchema() {
        tenantSchema.set(null);
    }
}