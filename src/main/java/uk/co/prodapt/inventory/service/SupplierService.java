package uk.co.prodapt.inventory.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import uk.co.prodapt.inventory.model.Supplier;

@Component
public class SupplierService {

    private static final Logger logger = LoggerFactory.getLogger(SupplierService.class);

    private final ObjectMapper objectMapper;
    private final Random random = new Random();
    private List<Supplier> suppliers;

    public SupplierService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Read a list of Suppliers from the file suppliers.json
     * Simulates a slow API call and potential errors.
     *
     * @return List of {@link Supplier}. Not @Null
     */
    public List<Supplier> getAll() throws IOException, InterruptedException {
        // Intentional error simulation
        final int randomInt = random.nextInt(9);
        if (randomInt == 1) {
            throw new IOException("Simulated IOException");
        }

        Thread t = new Thread(() -> {
            try {
                // Simulate slow API call
                Thread.sleep(2000);
                suppliers = loadFile();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        t.start();
        t.join();

        return suppliers;
    }

    /**
     * Load supplier data from suppliers.json file.
     *
     * @return List of {@link Supplier} objects.
     */
    private List<Supplier> loadFile() {
        Resource resource = new ClassPathResource("suppliers.json");
        try {
            if (resource.exists()) {
                TypeReference<List<Supplier>> mapType = new TypeReference<>() {};
                File file = resource.getFile();
                return objectMapper.readValue(file, mapType);
            } else {
                logger.error("File does not exist");
            }
        } catch (IOException e) {
            logger.error("Exception reading resource: " + e.getMessage());
        }
        return List.of();
    }

    /**
     * Get a supplier by its ID.
     *
     * @param id The ID of the supplier.
     * @return The supplier with the given ID, or null if not found.
     */
    public Supplier getSupplierById(int id) {
        if (suppliers == null || suppliers.isEmpty()) {
            throw new IllegalStateException("Suppliers list is not loaded.");
        }

        Optional<Supplier> supplier = suppliers.stream()
                .filter(s -> s.getId() == id)
                .findFirst();

        return supplier.orElse(null);
    }
}