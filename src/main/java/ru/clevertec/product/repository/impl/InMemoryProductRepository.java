package ru.clevertec.product.repository.impl;

import ru.clevertec.product.entity.Product;
import ru.clevertec.product.repository.ProductRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class InMemoryProductRepository implements ProductRepository {

    private final Map<UUID, Product> productMap = new HashMap<>();

    @Override
    public Optional<Product> findById(UUID uuid) {
        return Optional.ofNullable(productMap.get(uuid));
    }

    @Override
    public List<Product> findAll() {
        return productMap.values().stream().toList();
    }

    @Override
    public Product save(Product product) {
        if (product.getUuid() == null) {
            UUID uuid = UUID.randomUUID();
            product.setCreated(LocalDateTime.now());
            product.setUuid(uuid);
            productMap.put(uuid, product);
        } else {
            productMap.replace(product.getUuid(), product);
        }
        return productMap.get(product.getUuid());
    }

    @Override
    public void delete(UUID uuid) {
        productMap.remove(uuid);
    }
}

