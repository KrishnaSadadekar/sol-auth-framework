package com.nmims.lms.utilities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

public class Utils {

    public static String getString(Map<String, Object> map, String key) {
        Object val = map.get(key);
        return val != null ? val.toString() : null;
    }

    public static Long getLong(Map<String, Object> map, String key) {
        Object val = map.get(key);
        if (val == null) return null;
        if (val instanceof Number) return ((Number) val).longValue();
        return Long.valueOf(val.toString());
    }

    public static Double getDouble(Map<String, Object> map, String key) {
        Object val = map.get(key);
        if (val == null) return null;
        if (val instanceof Number) return ((Number) val).doubleValue();
        return Double.valueOf(val.toString());
    }

    public static Integer getInteger(Map<String, Object> map, String key) {
        Object val = map.get(key);
        if (val == null) return null;
        if (val instanceof Number) return ((Number) val).intValue();
        return Integer.valueOf(val.toString());
    }

    public static Boolean getBoolean(Map<String, Object> map, String key) {
        Object val = map.get(key);
        if (val == null) return null;
        if (val instanceof Boolean) return (Boolean) val;
        return Boolean.valueOf(val.toString());
    }

    private LocalDate getLocalDate(Map<String, Object> map, String key) {
        Object v = map.get(key);
        if (v == null) return null;

        if (v instanceof LocalDate) {
            return (LocalDate) v;
        }

        if (v instanceof LocalDateTime) {
            return ((LocalDateTime) v).toLocalDate();
        }

        if (v instanceof java.sql.Date) {
            return ((java.sql.Date) v).toLocalDate();
        }

        if (v instanceof java.sql.Timestamp) {
            return ((java.sql.Timestamp) v)
                    .toLocalDateTime()
                    .toLocalDate();
        }

        if (v instanceof String) {
            String value = v.toString();

            // ISO date-time (2025-10-21T14:30)
            if (value.contains("T")) {
                return LocalDateTime.parse(value).toLocalDate();
            }

            // ISO date (2025-10-21)
            return LocalDate.parse(value);
        }

        throw new IllegalArgumentException(
                "Unsupported date type for key: " + key + ", value: " + v
        );
    }


    public static Date getDate(Map<String, Object> map, String key) {
        Object val = map.get(key);
        if (val == null) return null;

        if (val instanceof Date) {
            return (Date) val;
        }

        if (val instanceof java.sql.Timestamp) {
            return new Date(((java.sql.Timestamp) val).getTime());
        }

        if (val instanceof java.sql.Date) {
            return new Date(((java.sql.Date) val).getTime());
        }

        if (val instanceof LocalDateTime) {
            return Date.from(
                    ((LocalDateTime) val)
                            .atZone(ZoneId.systemDefault())
                            .toInstant()
            );
        }

        if (val instanceof LocalDate) {
            return Date.from(
                    ((LocalDate) val)
                            .atStartOfDay(ZoneId.systemDefault())
                            .toInstant()
            );
        }

        if (val instanceof String) {
            String s = val.toString().trim();

            // Handles: 2025-10-21T14:30
            if (s.contains("T")) {
                return Date.from(
                        LocalDateTime.parse(s)
                                .atZone(ZoneId.systemDefault())
                                .toInstant()
                );
            }

            // Handles: 2025-10-21
            return Date.from(
                    LocalDate.parse(s)
                            .atStartOfDay(ZoneId.systemDefault())
                            .toInstant()
            );
        }

        throw new IllegalArgumentException(
                "Unsupported date type for key: " + key + ", value: " + val
        );
    }



}
