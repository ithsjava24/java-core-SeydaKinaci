package org.example.warehouse;

import java.math.BigDecimal;
import java.util.*;

public class Warehouse {

    final String name;
    private static HashMap<String, Warehouse> warehouses = new HashMap<>();
    private List<ProductRecord> products = new ArrayList<>();
    private List<ProductRecord> changedProducts = new ArrayList<>();


    private Warehouse(String name) {
        this.name = name;
    }

    public static Warehouse getInstance() {
        warehouses.remove("MyStore");
        return getInstance("MyStore");
    }

    public static Warehouse getInstance(String name) {
        if (!warehouses.containsKey(name)) {
            Warehouse warehouse = new InnerClass(name).getWarehouse();
            warehouses.put(name, warehouse);
            return warehouse;
        } else {
            return warehouses.get(name);
        }
    }

    public List<ProductRecord> getProducts() {
        return Collections.unmodifiableList(products);
    }

    public ProductRecord addProduct(UUID uuid, String name, Category category, BigDecimal price) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Product name can't be null or empty.");
        }
        if (category == null) {
            throw new IllegalArgumentException("Category can't be null.");
        }
        if (uuid == null) {
            uuid = UUID.randomUUID();
        }

        ProductRecord product = new ProductRecord(uuid, name, category, price == null ? BigDecimal.ZERO : price);
        boolean flag = false;
        for (ProductRecord productRecord : products) {
            if (productRecord.uuid() == product.uuid()) {
                flag = true;
                break;
            }
        }
        if (flag) {
            throw new IllegalArgumentException("Product with that id already exists, use updateProduct for updates.");
        } else {
            products.add(product);
        }
        return product;

    }

    public boolean isEmpty() {
        return products.isEmpty();
    }

    public Optional<ProductRecord> getProductById(UUID uuid) {
        for (ProductRecord product : products) {
            if (product.uuid() == uuid) {
                return Optional.of(product);
            }
        }
        return Optional.empty();
    }

    public List<ProductRecord> getChangedProducts() {
        return changedProducts;
    }

    public Map<Category, List<ProductRecord>> getProductsGroupedByCategories() {
        Map<Category, List<ProductRecord>> productsGroupedByCategories = new HashMap<>();
        for (ProductRecord product : products) {
            if (productsGroupedByCategories.containsKey(product.category())) {
                productsGroupedByCategories.get(product.category()).add(product);
            } else {
                List<ProductRecord> products = new ArrayList<>();
                products.add(product);
                productsGroupedByCategories.put(product.category(), products);
            }
        }
        return productsGroupedByCategories;
    }

    public List<ProductRecord> getProductsBy(Category category) {
        List<ProductRecord> productsByCategory = new ArrayList<>();
        for (ProductRecord product : products) {
            if (product.category() == category) {
                productsByCategory.add(product);
            }
        }
        return productsByCategory;
    }

    public void updateProductPrice(UUID uuid, BigDecimal price) {
        List<ProductRecord> tempProducts = new ArrayList<>();
        boolean flag = true;
        for (ProductRecord product : products) {
            if (product.uuid() == uuid) {
                flag = false;
                changedProducts.add(product);
                tempProducts.add(new ProductRecord(uuid, product.name(), product.category(), price));
            } else {
                tempProducts.add(product);
            }
        }
        if (flag) {
            throw new IllegalArgumentException("Product with that id doesn't exist.");
        } else {
            products = tempProducts;
        }
    }

    private static class InnerClass {

        public Warehouse warehouse;

        public InnerClass(String name) {
            this.warehouse = new Warehouse(name);
        }

        public Warehouse getWarehouse() {
            return warehouse;
        }

    }
}
