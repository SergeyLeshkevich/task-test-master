package ru.clevertec.product.repository.impl;

import org.junit.jupiter.api.Test;
import ru.clevertec.product.entity.Product;
import ru.clevertec.util.AppConstants;
import ru.clevertec.util.ProductTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryProductRepositoryTest {

    private final InMemoryProductRepository repository = new InMemoryProductRepository();
    private final ProductTest productTest = new ProductTest();

    @Test
    public void testCheckFindProductByIdFromRepository() {
        // given
        UUID uuid = AppConstants.UUID_PRODUCT;
        Product product = productTest.build();
        repository.save(product);

        // when
        Optional<Product> expected = repository.findById(uuid);

        // then
        assertAll(
                () -> assertTrue(expected.isPresent()),
                () -> assertEquals(product, expected.orElse(null))
        );

    }

    @Test
    public void testCheckFindAllProductsFromRepository() {
        // given
        Product product = productTest.build();
        List<Product> expectedProducts = Arrays.asList(
                product,
                new Product(UUID.fromString("555817ab-d5ce-4f1b-93a8-603fef29a38a"),
                        "Test Product2",
                        "description product2",
                        BigDecimal.valueOf(1000.1),
                        LocalDateTime.of(2023, 10, 30, 13, 00))
        );

        expectedProducts.forEach(repository::save);

        // when
        List<Product> actualProducts = repository.findAll();

        // then
        assertEquals(expectedProducts, actualProducts);
    }

    @Test
    public void testCheckSaveProductInRepository() {
        // given
        Product expected = productTest.build();

        // when
        Product actual = repository.save(expected);

        // then
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testCheckDeleteProductFromRepository() {
        // given
        UUID uuid = AppConstants.UUID_PRODUCT;
        Product product = productTest.build();
        repository.save(product);
        repository.delete(uuid);

        // when
        Optional<Product> actual = repository.findById(uuid);

        // then
        assertFalse(actual.isPresent());
    }
}
