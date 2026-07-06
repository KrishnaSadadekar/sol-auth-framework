package sol.auth.common.enums;

public enum UserStatustus {
   
    PENDING("Pending Approval"),
    ACTIVE("Active"),
    INACTIVE("Suspended/Inactive"),
    DELETED("Archived/Deleted"),
    COMPLETED("Successfully Completed");

    private final String displayName;

    // Enum constructors are implicitly private
    UserStatustus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
