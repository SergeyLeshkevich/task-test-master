package ru.clevertec.product.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.product.data.InfoProductDto;
import ru.clevertec.product.data.ProductDto;
import ru.clevertec.product.entity.Product;
import ru.clevertec.product.exception.ProductNotFoundException;
import ru.clevertec.product.mapper.impl.ProductMapperImpl;
import ru.clevertec.product.repository.impl.InMemoryProductRepository;
import ru.clevertec.util.AppConstants;
import ru.clevertec.util.ProductTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    InMemoryProductRepository productRepository;

    @Mock
    ProductMapperImpl productMapper;

    @InjectMocks
    ProductServiceImpl productService;

    @Captor
    ArgumentCaptor<Product> productCaptor;

    @Captor
    ArgumentCaptor<UUID> productUUIDCaptor;
    private final ProductTest productTest = new ProductTest();

    @Test
    void shouldThrowProductNotFoundException() {
        // given
        UUID uuid = UUID.fromString("0f39a05f-b9ea-46f6-b38a-f189052b63e8");
        // when,then
        assertThatThrownBy(() -> {
            productService.get(uuid);
        })
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessage("Product with uuid: 0f39a05f-b9ea-46f6-b38a-f189052b63e8 not found");
    }

    @Test
    void shouldGetProductById() {
        // given
        InfoProductDto expected = new InfoProductDto(AppConstants.UUID_PRODUCT, AppConstants.NAME_PRODUCT,
                AppConstants.DESCRIPTION_PRODUCT, AppConstants.PRICE_PRODUCT);
        UUID uuid = AppConstants.UUID_PRODUCT;

        when(productMapper.toInfoProductDto(productTest.build())).thenReturn(expected);
        when(productRepository.findById(uuid)).thenReturn(Optional.of(productTest.build()));

        // when
        InfoProductDto actual = productService.get(uuid);

        // then
        assertThat(actual)
                .isEqualTo(expected);
    }

    @Test
    void shouldSizeListTwo() {
        // given
        InfoProductDto infoProductDto1 = new InfoProductDto(AppConstants.UUID_PRODUCT, AppConstants.NAME_PRODUCT,
                AppConstants.DESCRIPTION_PRODUCT, AppConstants.PRICE_PRODUCT);

        InfoProductDto infoProductDto2 = new InfoProductDto(UUID.fromString("e57cbac6-51db-4757-829a-cd306df76f59"),
                AppConstants.NAME_PRODUCT, AppConstants.DESCRIPTION_PRODUCT, AppConstants.PRICE_PRODUCT);

        Product product = productTest
                .withUuidProduct(UUID.fromString("e57cbac6-51db-4757-829a-cd306df76f59"))
                .build();

        List<InfoProductDto> expected = List.of(infoProductDto1, infoProductDto2);

        when(productRepository.findAll()).thenReturn(List.of(productTest.build(), product));
        when(productMapper.toInfoProductDto(productTest.build())).thenReturn(infoProductDto1);
        when(productMapper.toInfoProductDto(product)).thenReturn(infoProductDto2);

        // when
        List<InfoProductDto> actual = productService.getAll();

        // then
        assertThat(actual).isNotNull()
                .hasSize(2)
                .isEqualTo(expected);
    }

    @Test
    void shouldActualUuidProductEqualToExpectedUuidProduct() {
        // given
        ProductDto productDto = new ProductDto(AppConstants.NAME_PRODUCT,
                AppConstants.DESCRIPTION_PRODUCT,
                AppConstants.PRICE_PRODUCT);

        when(productMapper.toProduct(productDto)).thenReturn(productTest.build());
        when(productRepository.save(productTest.build())).thenReturn(productTest.build());

        // when
        UUID actual = productService.create(productDto);

        // then
        assertThat(actual)
                .isNotNull()
                .isEqualTo(AppConstants.UUID_PRODUCT);
    }

    @ParameterizedTest
    @MethodSource("getArgumentsForUpdateTest")
    void shouldActualProductEqualToExpectedProduct(UUID uuid, String name, String description, BigDecimal price,
                                                   LocalDateTime date) {
        // given
        ProductDto productDto = new ProductDto(name, description, price);
        Product product = Product.builder()
                .uuid(uuid)
                .name(name)
                .description(description)
                .price(price)
                .created(date)
                .build();

        when(productMapper.toProduct(productDto)).thenReturn(product);
        when(productRepository.findById(uuid)).thenReturn(Optional.of(product));
        when(productRepository.save(productTest.build())).thenReturn(product);

        productService.create(productDto);

        // when
        productService.update(uuid, productDto);

        // then
        verify(productRepository, times(2)).save(productCaptor.capture());
        Product actual = productCaptor.getValue();

        assertThat(actual)
                .isNotNull()
                .isEqualTo(product);
    }

    static Stream<Arguments> getArgumentsForUpdateTest() {
        return Stream.of(
                Arguments.of(UUID.fromString("555817ab-d5ce-4f1b-93a8-603fef29a38a"),
                        "Test Product",
                        "description product",
                        BigDecimal.valueOf(1000.1),
                        LocalDateTime.of(2023, 10, 30, 13, 00))
        );
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/product-data.csv", delimiter = ',', numLinesToSkip = 1)
    void shouldActualUuidProductEqualToExpectedUuidProduct(String uuid, String name, String description, String price,
                                                           String year, String month, String day, String hour, String minute) {
        // given
        UUID id = UUID.fromString(uuid);
        BigDecimal priceProduct = BigDecimal.valueOf(Double.parseDouble(price));
        LocalDateTime dateTime = LocalDateTime.of(Integer.parseInt(year), Integer.parseInt(month),
                Integer.parseInt(day), Integer.parseInt(hour), Integer.parseInt(minute));

        ProductDto productDto = new ProductDto(name, description, priceProduct);

        Product product = Product.builder()
                .uuid(id)
                .name(name)
                .description(description)
                .price(priceProduct)
                .created(dateTime)
                .build();

        when(productMapper.toProduct(productDto)).thenReturn(product);
        when(productRepository.findById(AppConstants.UUID_PRODUCT)).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);

        productService.create(productDto);

        // when
        productService.delete(id);

        //then
        verify(productRepository).delete(productUUIDCaptor.capture());
        UUID actual = productUUIDCaptor.getValue();

        assertThat(actual)
                .isNotNull()
                .isEqualTo(id);
    }
}