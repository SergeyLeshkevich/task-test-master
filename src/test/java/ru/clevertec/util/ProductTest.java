package ru.clevertec.util;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.product.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@With
public class ProductTest implements TestBuilder<Product> {
    private UUID uuidProduct = AppConstants.UUID_PRODUCT;
    private String nameProduct = AppConstants.NAME_PRODUCT;
    private String descriptionProduct = AppConstants.DESCRIPTION_PRODUCT;
    private BigDecimal priceProduct = AppConstants.PRICE_PRODUCT;
    private LocalDateTime createdProduct = AppConstants.CREAT_PRODUCT;
    @Override
    public Product build() {
        final var product = new Product();
        product.setUuid(uuidProduct);
        product.setName(nameProduct);
        product.setDescription(descriptionProduct);
        product.setPrice(priceProduct);
        product.setCreated(createdProduct);
        return product;
    }
}

