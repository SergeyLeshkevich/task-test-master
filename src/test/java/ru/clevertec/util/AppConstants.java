package ru.clevertec.util;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class AppConstants {
    public static final UUID UUID_PRODUCT = UUID.fromString("555817ab-d5ce-4f1b-93a8-603fef29a38a");
    public static final String NAME_PRODUCT = "Test Product";
    public static final String DESCRIPTION_PRODUCT = "description product";
    public static final BigDecimal PRICE_PRODUCT = BigDecimal.valueOf(1000.1);
    public static final LocalDateTime CREAT_PRODUCT = LocalDateTime.of(2023, 10, 30, 13, 00);
}
