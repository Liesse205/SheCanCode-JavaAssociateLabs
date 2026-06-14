# Lab 1: Advanced Java Core Foundations

## Problem Statement

This project simulates three real-world backend systems to practice:

- Type-safe data modeling with generics
- Robust error handling in financial transactions
- Concurrent order matching for trading systems
- Performance-aware collection choices
- Production-grade logging and exception chaining
  
This project implements type-safe inventory tracking, robust banking error handling, and concurrent order matching.

---

## Technology Stack

| Component | Version |
|-----------|---------|
| Java | OpenJDK 25.0.3 LTS |
| Build Tool | Maven 3.9.16 |
| Testing | JUnit 5.10.0 |
| Logging | SLF4J 2.0.9 + Logback 1.4.11 |

---

## Scenario 1: Inventory Management System

### Problem

A retail company needs a warehouse inventory tracker that can store different product types without runtime casting errors, support fast category lookups, and sort products by multiple fields.

### Exercise 1.1 - Generic Warehouse Store

**Implementation:**
- `WarehouseStore<T extends Product>` generic class
- Compile-time type safety prevents `ClassCastException`
- UUID for unique product identification

**Why bounded type parameter:**
```java
// Compiles - Electronics extends Product
WarehouseStore<Electronics> techStore = new WarehouseStore<>();

// Compile error - String does not extend Product
// WarehouseStore<String> stringStore = new WarehouseStore<>();
```

**Category as enum, not String:**
```java
public enum Category {
    ELECTRONICS, COMPUTERS, MOBILE, AUDIO, CLOTHING, GROCERY;
}
```
Benefits: compile-time validation, IDE autocomplete, refactoring safety.

### Exercise 1.2 - Collections Benchmark**
**Observational comparison (not formal JMH benchmark):**

Collection	Insert Performance	Lookup Performance	Iteration	Best Use Case
ArrayList	Fast (contiguous memory)	O(n) scan	Fastest	Read-heavy, indexed access
LinkedList	Moderate (node allocation)	O(n) scan	Slower	Frequent head/tail operations
HashSet	Fast (hash-based)	O(1) average	Moderate	Uniqueness + fast lookup
TreeSet	Slower (log n)	O(log n)	Sorted	Sorted unique elements

**Recommended for "Recently Viewed Products":** ```LinkedHashSet```

- Preserves insertion order (chronological view)

- O(1) lookup time

- Automatic duplicate elimination

### Exercise 1.3 - Custom Comparator Chain**
```java
public static final Comparator<Product> BY_CATEGORY_THEN_PRICE_DESC =
    Comparator.comparing(Product::getCategory)
              .thenComparing(Comparator.comparing(Product::getPrice, 
                            Comparator.nullsLast(Double::compareTo)).reversed());
```
Handles null prices by placing them at the end of the sorted list.

## Scenario 2: Banking Transaction Processor
### Problem
A banking system needs to process money transfers with proper error handling, preserve root causes when wrapping exceptions, and log transaction context for debugging. Failed CSV transactions should not abort the entire batch.

### Exercise 1.4 - Custom Exception Hierarchy**
Exception Structure:
```
TransactionException (checked)
├── InsufficientFundsException (errorCode: INSF_001)
└── FraudDetectedException (errorCode: FRD_001, FRD_002)
```
**Error codes appear in messages:**
```java
throw new InsufficientFundsException(
    "Insufficient funds in account: " + accountNumber,
    "INSF_001"
);
// Message becomes: "[INSF_001] Insufficient funds..."
```
### Exercise 1.5 - Exception Chaining with MDC**
```java
String transactionId = UUID.randomUUID().toString();
MDC.put("transactionId", transactionId);
try {
    BigDecimal balance = databaseService.getBalance(accountId);
} catch (SQLException e) {
    throw new DataAccessException("Failed to fetch balance", e);
} finally {
    MDC.clear();
}
```
**Test that verifies cause chain:**
```java
DataAccessException exception = assertThrows(DataAccessException.class, () -> {
    databaseService.getBalanceFromDatabase("");
});

assertNotNull(exception.getCause());
assertTrue(exception.getCause() instanceof SQLException);

SQLException cause = (SQLException) exception.getCause();
assertEquals("Invalid account identifier", cause.getMessage());
```
### Exercise 1.6 - CSV Processing with Try-With-Resources**
**Design:**

- ```try-with-resources``` ensures BufferedReader is closed

- Malformed lines do NOT abort processing

- Failed transactions written to separate file for manual review

**Output example:**
```text
Processed: 5, Failed: 3
Wrote 3 errors to: logs/failed_transactions.csv
```
## Scenario 3: Real-Time Order Matching Engine
### Problem
A stock exchange needs a concurrent order book where multiple buyers and sellers can submit orders simultaneously without race conditions or lost orders. The system must measure matching throughput.

### Exercise 1.7 - Thread-Safe Order Book**
- Thread safety implementation:

- ```ReentrantLock``` locks both ``addOrder()``` and ```matchOrders()```

- Mutable ```remainingQuantity``` preserves order identity

- 10 buyer threads + 10 seller threads

- No order loss verified

**Critical improvement over simple approach:**
```java
// Wrong: removes order then recreates it (loses identity)
queue.poll();
Order remaining = new Order(...);

// Correct: reduces quantity in place
order.reduceQuantity(matchedQty);
if (order.isFilled()) {
    queue.poll();
}

// Wrong: removes order then recreates it (loses identity)
queue.poll();
Order remaining = new Order(...);

// Correct: reduces quantity in place
order.reduceQuantity(matchedQty);
if (order.isFilled()) {
    queue.poll();
}
```
### Exercise 1.8 - ExecutorService Orchestration**
**Thread pool size:**
```java
int availableProcessors = Runtime.getRuntime().availableProcessors();
ExecutorService executor = Executors.newFixedThreadPool(availableProcessors);
```
**Throughput measurement with warm-up:**
```java
// Warm-up run (ignored to prevent JIT skew)
for (int i = 0; i < warmupRuns; i++) {
    submitMatchingTask();
}

// Measured runs
long startTime = System.nanoTime();
for (int i = 0; i < matchingRuns; i++) {
    submitMatchingTask();
}
double throughput = matchingRuns / durationSeconds;
```
**Future.get() with error handling:**
```java
Future<Integer> future = executor.submit(matchingTask);
try {
    int matches = future.get();
} catch (ExecutionException e) {
    log.error("Matching failed", e.getCause());
}
```
**How to Run**
**Prerequisites**
- Java 17 or higher (tested on OpenJDK 25.0.3 LTS)
- Maven 3.9 or higher

**Compile**
```bash
mvn clean compile
```
**Run All Tests**
```bash
mvn test
```
**Run Individual Demos**
**Inventory Demo (Lab 1.1):**
```bash
mvn exec:java "-Dexec.mainClass=com.iro.labs.inventory.demo.MainDemo"
```
**Banking Demo (Lab 1.2):**
```bash
mvn exec:java "-Dexec.mainClass=com.iro.labs.banking.BankingDemo"
```
**Matching Demo (Lab 1.3):**
```bash
mvn exec:java "-Dexec.mainClass=com.iro.labs.matching.MatchingDemo"
```
**Project Structure**
```text
Lab1-advanced-java-core-foundations/
├── README.md
├── pom.xml
├── src/
│   ├── main/java/com/iro/labs/
│   │   ├── inventory/          # Scenario 1
│   │   │   ├── model/          # Product, Electronics, Category
│   │   │   ├── store/          # WarehouseStore
│   │   │   ├── comparator/     # ProductComparator
│   │   │   └── demo/           # MainDemo
│   │   ├── banking/            # Scenario 2
│   │   │   ├── model/          # Account, ParseError
│   │   │   ├── exception/      # TransactionException, InsufficientFundsException, FraudDetectedException, DataAccessException
│   │   │   ├── service/        # TransactionService, DatabaseService
│   │   │   ├── processor/      # CsvTransactionProcessor
│   │   │   └── BankingDemo
│   │   ├── matching/           # Scenario 3
│   │   │   ├── model/          # Order, OrderType, MatchResult
│   │   │   ├── engine/         # OrderBook
│   │   │   ├── executor/       # BuyerTask, SellerTask, MatchingTask, OrderMatchingEngine
│   │   │   └── MatchingDemo
│   │   └── resources/
│   │       └── logback.xml
│   └── test/java/com/iro/labs/
│       ├── inventory/store/    # WarehouseStoreTest
│       ├── banking/service/    # TransactionServiceTest, DatabaseServiceTest
│       └── matching/engine/    # OrderBookTest
├── data/
│   └── transactions.csv
└── logs/
```
**Lessons Learned**

- Bounded type parameters catch type errors at compile time, not runtime

- Enum over String eliminates entire classes of bugs

- Exception chaining preserves root cause for debugging distributed systems

- MDC transaction IDs enable log correlation across concurrent requests

- Full locking is simpler and safer than partial synchronization

- BigDecimal is non-negotiable for financial applications

- Warm-up runs prevent JIT compilation from skewing benchmark results

- Try-with-resources guarantees resource cleanup even when exceptions occur

