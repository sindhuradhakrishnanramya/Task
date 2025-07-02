package uk.co.prodapt.inventory.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.co.prodapt.inventory.model.Product;
import uk.co.prodapt.inventory.model.Supplier;

@Service
public class ProductService {

    private final List<Product> products = new ArrayList<>();
    private final SupplierService supplierService;

    @Autowired
    public ProductService(SupplierService supplierService) {
        this.supplierService = supplierService;

        // Initialize some sample products
        products.add(new Product(1, "Product A", true, 1, null));
        products.add(new Product(2, "Product B", false, 2, null));
        products.add(new Product(3, "Product C", true, 3, null));
    }

    public List<Product> getAll() {
        return enrichWithSupplierInfo(new ArrayList<>(products));
    }

    public Optional<Product> getById(Integer id) {
        return products.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst()
                .map(this::enrichWithSupplierInfo);
    }

    public Product create(Product product) {
        products.add(product);
        return enrichWithSupplierInfo(product);
    }

    public Product update(Integer id, Product updatedProduct) {
        Product existingProduct = getById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setAvailable(updatedProduct.isAvailable());
        existingProduct.setSupplierId(updatedProduct.getSupplierId());
        return enrichWithSupplierInfo(existingProduct);
    }

    public void delete(Integer id) {
        products.removeIf(product -> product.getId().equals(id));
    }

    private List<Product> enrichWithSupplierInfo(List<Product> products) {
        for (Product product : products) {
            enrichWithSupplierInfo(product);
        }
        return products;
    }

    private Product enrichWithSupplierInfo(Product product) {
        if (product.getSupplierId() != null) {
            try {
                Supplier supplier = supplierService.getSupplierById(product.getSupplierId());
                product.setSupplierName(supplier.getName());
            } catch (RuntimeException e) {
                product.setSupplierName("Unknown Supplier");
            }
        }
        return product;
    }
    
    //task1
    
    public List<Product> getProducts(boolean availableOnly) {
        Stream<Product> productStream = products.stream();

        if (availableOnly) {
            productStream = productStream.filter(product -> product.getId() > 0);
        }

       
        return productStream
            .filter(Objects::nonNull)
            .collect(Collectors.toMap(
                Product::getId,
                Function.identity(),
                (existing, duplicate) -> existing,
                LinkedHashMap::new
            ))
            .values()
            .stream()
            .collect(Collectors.toList());
    }
    
    //task  2
    
    public List<Product> getAvailableProducts(Optional<Boolean> availabilityFilter) {
        
        List<Product> snapshot;
        synchronized (this.products) {
            snapshot = new ArrayList<>(this.products);
        }

        
        Stream<Product> stream = snapshot.stream();
        if (availabilityFilter.isPresent()) {
            boolean filterValue = availabilityFilter.get();
            stream = stream.filter(product -> product.isAvailable() == filterValue);
        }

         return stream
                .collect(Collectors.collectingAndThen(
                        Collectors.toMap(
                                Product::getId,  
                                Function.identity(),
                                (existing, duplicate) -> existing,  
                                LinkedHashMap::new  
                        ),
                        map -> new ArrayList<>(map.values())
                ));
    }
}
