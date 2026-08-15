# 🛡️ HDFC Life Policy Claims Console

> **A robust, pure Java console application for managing insurance policies, calculating dynamic premiums, processing claims, and delivering real-time claim notifications — built with SOLID principles and classic design patterns.**

---

## ✨ Overview

**HDFC Life Policy Claims Console** is a modular, object-oriented Java application designed to demonstrate how real-world insurance operations can be implemented using clean architecture and proven software design principles.

The application provides:

* 📋 Policy management
* 💰 Dynamic premium calculation
* 📝 Fluent claim creation
* 🚨 Priority-based claim processing
* 🔔 Real-time claim status notifications
* 🗃️ Multiple Java Collections for efficient data management
* 🧩 SOLID-compliant service architecture
* 🏭 Factory, Strategy, Builder, Observer, and Singleton patterns
* 📝 Auditable file-based logging
* 🛡️ Robust custom exception handling

---

## 🏗️ Architecture

```text
                        ┌─────────────────────┐
                        │      Main.java      │
                        │   Console Interface │
                        └──────────┬──────────┘
                                   │
                  ┌────────────────┼────────────────┐
                  │                │                │
                  ▼                ▼                ▼
          ┌──────────────┐ ┌──────────────┐ ┌──────────────┐
          │ PolicyStore  │ │ ClaimService │ │ AuditLogger  │
          └──────┬───────┘ └──────┬───────┘ └──────────────┘
                 │                │
        ┌────────┼────────┐       │
        ▼        ▼        ▼       ▼
    ArrayList HashSet HashMap  Claim
                         │        │
                         ▼        ▼
                      TreeMap  Observer
                                  │
                         ┌────────┴────────┐
                         ▼                 ▼
                 InAppNotifier     BranchLetterNotifier
```

---

# 🚀 Key Features

## 📋 Policy Management

The system supports multiple insurance policy types through an abstract `Policy` base class.

```text
                    Policy
                      │
          ┌───────────┼───────────┐
          │           │           │
          ▼           ▼           ▼
      Term Life     ULIP      Endowment
```

Supported policy types:

* 🛡️ `TermLifePolicy`
* 📈 `UlipPolicy`
* 💰 `EndowmentPolicy`

Polymorphism allows each policy type to define its own behavior while remaining interchangeable through the common `Policy` abstraction.

---

## 🗃️ PolicyStore & Collections

`PolicyStore` demonstrates the practical use of several Java Collection types.

| Collection      | Purpose                                      |
| --------------- | -------------------------------------------- |
| `ArrayList`     | Stores all policies                          |
| `HashSet`       | Tracks unique customer names                 |
| `HashMap`       | Provides fast policy lookup by policy number |
| `TreeMap`       | Maintains policies sorted by policy number   |
| `PriorityQueue` | Processes claims according to urgency        |

### ⚡ Efficient Lookup

```java
HashMap<String, Policy>
```

Policy numbers act as keys, allowing efficient lookup without scanning the complete policy list.

### 🚨 Claim Prioritization

Claims are processed according to:

```text
HIGH
  ↓
MEDIUM
  ↓
LOW
```

This is implemented using Java's `PriorityQueue`.

---

# 🧠 SOLID Principles

The application is intentionally designed around the **SOLID** principles.

### 🟢 S — Single Responsibility Principle

Each component has a focused responsibility.

```text
PolicyStore     → Policy data management
PremiumCalculator → Premium calculation
ClaimService    → Claim processing
AuditLogger     → Audit logging
```

---

### 🔵 O — Open/Closed Principle

The premium calculation system is extensible without modifying existing calculator logic.

New premium strategies can be introduced independently.

```text
PremiumStrategy
      │
 ┌────┼──────────────┐
 ▼    ▼              ▼
Term  ULIP       Endowment
```

---

### 🟣 L — Liskov Substitution Principle

Every implementation of `PremiumStrategy` can be substituted wherever the `PremiumStrategy` abstraction is expected.

```java
PremiumStrategy strategy;
```

The caller does not need to know which concrete strategy is being used.

---

### 🟠 I — Interface Segregation Principle

Observers implement a lightweight interface containing only the behavior they require.

```java
ClaimObserver
      │
      ├── InAppNotifier
      │
      └── BranchLetterNotifier
```

---

### 🔴 D — Dependency Inversion Principle

Services depend on abstractions rather than tightly coupling themselves to concrete implementations.

This makes components easier to:

* Test
* Replace
* Extend
* Maintain

---

# 🎨 Design Patterns

## 🧵 Singleton — `AppConfig`

A thread-safe enum singleton manages global configuration.

```text
AppConfig
   │
   ├── companyName
   └── maxClaimAmount
```

Using an enum singleton avoids common issues associated with traditional singleton implementations.

---

## 🏭 Factory — `PolicyFactory`

Centralizes policy creation.

```java
PolicyFactory.createPolicy(...)
```

The factory:

* Creates known policy types
* Hides construction details
* Keeps client code independent of concrete policy classes
* Throws `UnknownPolicyTypeException` for unsupported types

```text
                    PolicyFactory
                         │
          ┌──────────────┼──────────────┐
          ▼              ▼              ▼
     TermLifePolicy   UlipPolicy   EndowmentPolicy
```

---

## 🧱 Builder — `Claim`

Claims use a fluent static inner Builder.

```java
Claim claim = new Claim.Builder()
        .claimId("CLM001")
        .policyNumber("POL001")
        .amount(500000)
        .urgency(Urgency.HIGH)
        .build();
```

### Benefits

* Fluent API
* Readable object creation
* Validation during construction
* Immutable core claim fields
* Avoids large constructors

---

## 🔀 Strategy — Premium Calculation

Premium calculation is separated into interchangeable strategies.

```text
                 PremiumStrategy
                       │
          ┌────────────┼────────────┐
          ▼            ▼            ▼
       Term         ULIP       Endowment
      Strategy     Strategy      Strategy
```

This allows new premium calculation rules to be added without changing the existing calculator.

---

## 👀 Observer — Claim Notifications

Claim status changes are automatically published to registered observers.

```text
                    ClaimEventPublisher
                            │
                 ┌──────────┴──────────┐
                 ▼                     ▼
          InAppNotifier        BranchLetterNotifier
```

For example:

```text
Claim Status
     │
     ▼
APPROVED
     │
     ├──────────────► 📱 In-App Notification
     │
     └──────────────► ✉️ Branch Letter Notification
```

New notification channels can be added without modifying the claim service.

---

# 💰 Premium Calculation

Premium calculations are delegated to the appropriate strategy.

```text
Policy
  │
  ▼
PremiumCalculator
  │
  ▼
PremiumStrategy
  │
  ├── TermPremiumStrategy
  ├── UlipPremiumStrategy
  └── EndowmentPremiumStrategy
```

This keeps premium logic modular and makes the application easy to extend with future policy categories.

---

# 🚨 Claim Processing

Claims are created using the Builder pattern and processed through `ClaimService`.

```text
Create Claim
     │
     ▼
Validate Claim
     │
     ▼
Add to PriorityQueue
     │
     ▼
Process by Urgency
     │
     ▼
Update Status
     │
     ▼
Notify Observers
     │
     ▼
Audit Log
```

### Priority

```text
🔥 HIGH
   ↓
⚠️ MEDIUM
   ↓
ℹ️ LOW
```

---

# 🛡️ Exception Handling

The application uses a custom runtime exception hierarchy.

```text
              PolicyServiceException
                       │
        ┌──────────────┼──────────────┐
        ▼              ▼              ▼
PolicyNotFound   InvalidClaim   UnknownPolicyType
  Exception        Exception       Exception
```

### Available Exceptions

| Exception                    | Purpose                      |
| ---------------------------- | ---------------------------- |
| `PolicyServiceException`     | Base service-level exception |
| `PolicyNotFoundException`    | Policy lookup failure        |
| `InvalidClaimException`      | Invalid claim data           |
| `UnknownPolicyTypeException` | Unsupported policy type      |

This avoids silent failures and empty `catch` blocks.

---

# 📝 Audit Logging

`AuditLogger` implements secure file-based logging using Java's **try-with-resources**.

```java
try (FileWriter writer = new FileWriter(file, true)) {
    writer.write(message);
}
```

Benefits:

* Automatic resource management
* Proper file closure
* Cleaner exception handling
* Reduced resource-leak risk

---

# 📁 Project Structure

```text
hdfc-life-policy-system/
│
├── src/
│   └── com/
│       └── hdfclife/
│           │
│           ├── Main.java
│           │
│           ├── config/
│           │   └── AppConfig.java
│           │
│           ├── exception/
│           │   ├── InvalidClaimException.java
│           │   ├── PolicyNotFoundException.java
│           │   ├── PolicyServiceException.java
│           │   └── UnknownPolicyTypeException.java
│           │
│           ├── factory/
│           │   └── PolicyFactory.java
│           │
│           ├── model/
│           │   ├── Claim.java
│           │   ├── EndowmentPolicy.java
│           │   ├── Policy.java
│           │   ├── TermLifePolicy.java
│           │   ├── UlipPolicy.java
│           │   └── Urgency.java
│           │
│           ├── observer/
│           │   ├── BranchLetterNotifier.java
│           │   ├── ClaimEventPublisher.java
│           │   ├── ClaimObserver.java
│           │   └── InAppNotifier.java
│           │
│           ├── service/
│           │   ├── AuditLogger.java
│           │   └── ClaimService.java
│           │
│           ├── store/
│           │   └── PolicyStore.java
│           │
│           └── strategy/
│               ├── EndowmentPremiumStrategy.java
│               ├── PremiumCalculator.java
│               ├── PremiumStrategy.java
│               ├── TermPremiumStrategy.java
│               └── UlipPremiumStrategy.java
│
├── .gitignore
└── README.md
```

---

# ⚙️ Getting Started

## Prerequisites

Make sure you have:

* ☕ **JDK 8 or higher**
* 💻 Terminal / Command Prompt
* 📦 Standard Java compiler (`javac`)

Verify your Java installation:

```bash
java -version
javac -version
```

---

# 🔨 Compilation

Navigate to the project root:

```bash
cd hdfc-life-policy-system
```

Create the compilation output directory:

```bash
mkdir -p bin
```

Compile the project:

```bash
javac -d bin src/com/hdfclife/**/*.java
```

---

# ▶️ Run the Application

After successful compilation:

```bash
java -cp bin com.hdfclife.Main
```

---

# 🖥️ Application Flow

The application follows a straightforward console-driven workflow:

```text
┌─────────────────────────────┐
│       HDFC LIFE SYSTEM      │
└──────────────┬──────────────┘
               │
               ▼
        Manage Policies
               │
               ▼
       Calculate Premium
               │
               ▼
          File Claim
               │
               ▼
       Set Claim Status
               │
               ▼
     Notify Registered Channels
               │
               ▼
          Audit Event
```

---

# 🧩 Technology Stack

| Technology           | Usage                       |
| -------------------- | --------------------------- |
| ☕ Java               | Core programming language   |
| 📦 Java Collections  | Data storage and processing |
| 🧱 OOP               | Domain modelling            |
| 🏭 Factory Pattern   | Policy creation             |
| 🧠 Strategy Pattern  | Premium calculation         |
| 👀 Observer Pattern  | Claim notifications         |
| 🧱 Builder Pattern   | Claim construction          |
| 🔒 Singleton Pattern | Application configuration   |
| 📄 File I/O          | Audit logging               |
| ⚠️ Custom Exceptions | Error handling              |

---

# 🌟 Design Highlights

### Clean Architecture

Responsibilities are separated into focused packages:

```text
model       → Domain objects
store       → Data management
service     → Business operations
strategy    → Premium algorithms
factory     → Object creation
observer    → Notifications
exception   → Error handling
config      → Global configuration
```

### Extensibility

Adding a new policy type can follow this approach:

```text
1. Create new Policy subclass
          ↓
2. Create corresponding PremiumStrategy
          ↓
3. Register it in PolicyFactory
          ↓
4. Use it through existing abstractions
```

Existing services remain largely untouched.

---

# 🧪 Example Claim Creation

```java
Claim claim = new Claim.Builder()
        .claimId("CLM1001")
        .policyNumber("POL1001")
        .customerName("John Doe")
        .amount(250000)
        .urgency(Urgency.HIGH)
        .build();
```

The fluent API makes claim creation expressive and easy to read.

---

# 📌 Why This Project?

This project demonstrates practical application of:

* Object-Oriented Programming
* SOLID design principles
* Java Collections Framework
* Design Patterns
* Exception handling
* File I/O
* Immutability
* Polymorphism
* Abstraction
* Dependency Inversion
* Extensible software architecture

It is particularly useful as a **Java OOP / Design Patterns portfolio project** and as a reference for designing maintainable console-based business applications.

---

# 🔮 Future Enhancements

Potential improvements include:

* 🗄️ Database persistence using JDBC/JPA
* 🌐 REST API using Spring Boot
* 🔐 Authentication and authorization
* 📊 Claim analytics dashboard
* 🧪 Unit and integration tests with JUnit
* 📝 JSON-based configuration
* 📧 Email notification observer
* 📱 SMS notification observer
* 🧵 Asynchronous claim processing
* 📈 Premium calculation based on additional risk factors

---

# 👨‍💻 Engineering Principles

> **"Code should be open for extension, closed for unnecessary modification, and designed around clear responsibilities."**

This project prioritizes:

**Clean Code → SOLID → Abstraction → Extensibility → Maintainability**

---

## ⭐ Project Summary

```text
HDFC Life Policy Claims Console
│
├── 🛡️ Policy Management
├── 💰 Dynamic Premium Calculation
├── 📝 Claim Builder
├── 🚨 Priority Claim Processing
├── 🔔 Observer Notifications
├── 🗃️ Collection-based Storage
├── 🏭 Factory-based Policy Creation
├── 🔀 Strategy-based Premium Logic
├── 🔒 Singleton Configuration
├── ⚠️ Custom Exception Hierarchy
└── 📝 Auto-Closeable Audit Logging
```

**Built with ❤️ using Core Java and Object-Oriented Design.**
