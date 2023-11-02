package ru.clevertec.product.service.impl;

import lombok.RequiredArgsConstructor;
import ru.clevertec.product.data.InfoProductDto;
import ru.clevertec.product.data.ProductDto;
import ru.clevertec.product.entity.Product;
import ru.clevertec.product.exception.ProductNotFoundException;
import ru.clevertec.product.mapper.ProductMapper;
import ru.clevertec.product.repository.ProductRepository;
import ru.clevertec.product.service.ProductService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductMapper mapper;
    private final ProductRepository productRepository;

    @Override
    public InfoProductDto get(UUID uuid) throws ProductNotFoundException {
        if (productRepository.findById(uuid).isPresent()) {
            Product product = productRepository.findById(uuid).get();
            return mapper.toInfoProductDto(product);
        }
        throw new ProductNotFoundException(uuid);
    }

    @Override
    public List<InfoProductDto> getAll() {
        List<Product> productList = productRepository.findAll();

        return productList.stream().map(mapper::toInfoProductDto).toList();
    }

    @Override
    public UUID create(ProductDto productDto) {
        Product product = mapper.toProduct(productDto);
        product = productRepository.save(product);
        return product.getUuid();
    }

    @Override
    public void update(UUID uuid, ProductDto productDto) {
        Optional<Product> optionalProduct = productRepository.findById(uuid);

        if (optionalProduct.isEmpty()) {
            throw new ProductNotFoundException(uuid);
        }

        Product product = mapper.toProduct(productDto);
        product.setUuid(uuid);
        productRepository.save(product);

    }

    @Override
    public void delete(UUID uuid) {
        Optional<Product> optionalProduct = productRepository.findById(uuid);

        if (optionalProduct.isEmpty()) {
            throw new ProductNotFoundException(uuid);
        }
        productRepository.delete(uuid);
    }
}