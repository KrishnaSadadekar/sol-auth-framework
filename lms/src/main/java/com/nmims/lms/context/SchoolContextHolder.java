package com.nmims.lms.context;

/**
 * Thread-local holder for SchoolContext.
 * Set by the interceptor at the start of each request,
 * cleared after the request completes.
 */
public final class SchoolContextHolder {

    private static final ThreadLocal<SchoolContext> CONTEXT = new ThreadLocal<>();

    private SchoolContextHolder() {}

    public static void set(SchoolContext ctx) {
        CONTEXT.set(ctx);
    }

    public static SchoolContext get() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }

    public static String getCampusCode() {
        SchoolContext ctx = get();
        return ctx != null ? ctx.getCampusCode() : null;
    }

    public static String getSchool() {
        SchoolContext ctx = get();
        return ctx != null ? ctx.getSchool() : null;
    }
}
