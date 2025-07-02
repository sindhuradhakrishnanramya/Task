# **Inventory Management Technical Test Instructions**

The purpose of this assessment is to evaluate your ability to implement features, resolve issues, and suggest improvements in a Java-based inventory management application. The application uses Swagger for API documentation and includes a SupplierService that simulates an external API. The SupplierService should be treated as a black box and must not be modified.

Familiarize yourself with the code, read the tasks, then complete them in order. When complete, provide us with read access to your Git repository.

---

#### **Task 1**

A new feature has been requested for the inventory management application. The consumer would like to add a way to filter products based on their availability status. If a product is marked as "out of stock," it should not appear in the results. If no availability filter is specified, all products should be returned.

Ensure that this filtering is optional and configurable for each request. Update the API to support this feature and ensure it is reflected in the Swagger documentation.

---

#### **Task 2**

An issue has been raised by a customer, and investigation has determined that the inventory management application is the source. When filtering products by availability (implemented in Task 1), the application occasionally returns incorrect results due to a race condition in the `ProductService`.

To simulate this issue, assume that the `ProductService` may occasionally return duplicate or incomplete results. Implement a solution to ensure that the results returned by the API are always accurate and free of duplicates. Follow best practices and existing project conventions where appropriate.

---

#### **Task 3**

Having completed the tasks above, consider what improvements you might like to see in the code. These could include refactors, optimizations, test improvements, general code housekeeping, etc. Prepare some suggestions for discussion in the next interview.