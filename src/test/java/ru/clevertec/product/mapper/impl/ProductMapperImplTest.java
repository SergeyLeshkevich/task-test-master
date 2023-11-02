package ru.clevertec.product.mapper.impl;

import org.junit.jupiter.api.Test;
import ru.clevertec.product.data.InfoProductDto;
import ru.clevertec.product.data.ProductDto;
import ru.clevertec.product.entity.Product;
import ru.clevertec.product.mapper.ProductMapper;
import ru.clevertec.util.AppConstants;
import ru.clevertec.util.ProductTest;

import static org.assertj.core.api.Assertions.assertThat;

class ProductMapperImplTest {

    ProductTest productTest = new ProductTest();

    @Test
    void testToProduct() {
        // given
        ProductDto productDto = new ProductDto(AppConstants.NAME_PRODUCT,
                AppConstants.DESCRIPTION_PRODUCT, AppConstants.PRICE_PRODUCT);

        // when
        Product product = ProductMapper.INSTANCE.toProduct(productDto);

        // then
        assertThat(product.getUuid()).isNull();
        assertThat(product.getName()).isEqualTo(AppConstants.NAME_PRODUCT);
        assertThat(product.getDescription()).isEqualTo(AppConstants.DESCRIPTION_PRODUCT);
        assertThat(product.getPrice()).isEqualTo(AppConstants.PRICE_PRODUCT);
        assertThat(product.getCreated()).isNull();
    }

    @Test
    void testToInfoProductDto() {
        // given
        Product product = productTest.build();

        // when
        InfoProductDto infoProductDto=ProductMapper.INSTANCE.toInfoProductDto(product);

        // then
        assertThat(infoProductDto.uuid()).isEqualTo(AppConstants.UUID_PRODUCT);
        assertThat(infoProductDto.name()).isEqualTo(AppConstants.NAME_PRODUCT);
        assertThat(infoProductDto.description()).isEqualTo(AppConstants.DESCRIPTION_PRODUCT);
        assertThat(infoProductDto.price()).isEqualTo(AppConstants.PRICE_PRODUCT);
    }

    @Test
    void testMerge() {
        // given
        Product product = productTest
                .withNameProduct("")
                .withDescriptionProduct("")
                .withPriceProduct(null)
                .build();
        ProductDto productDto = new ProductDto(AppConstants.NAME_PRODUCT,
                AppConstants.DESCRIPTION_PRODUCT, AppConstants.PRICE_PRODUCT);

        // when
        Product actual=ProductMapper.INSTANCE.merge(product,productDto);

        // then
        assertThat(actual.getUuid()).isEqualTo(AppConstants.UUID_PRODUCT);
        assertThat(actual.getName()).isEqualTo(AppConstants.NAME_PRODUCT);
        assertThat(actual.getDescription()).isEqualTo(AppConstants.DESCRIPTION_PRODUCT);
        assertThat(actual.getPrice()).isEqualTo(AppConstants.PRICE_PRODUCT);
        assertThat(actual.getCreated()).isEqualTo(AppConstants.CREAT_PRODUCT);
    }

}