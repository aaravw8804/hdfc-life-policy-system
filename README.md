HDFC Life Policy Claims Console
A robust, pure Java console application designed to manage insurance policies, calculate dynamic premiums using the Strategy pattern, file insurance claims with a fluent Builder pattern, and notify registered channels on status changes via the Observer pattern—all built following strict SOLID principles and Object-Oriented best practices.

Features & Architecture
Domain Models & Polymorphism: Abstract base Policy class extended by TermLifePolicy, UlipPolicy, and EndowmentPolicy.

Collections Store (PolicyStore):

ArrayList for storing all policies.

HashSet for tracking unique customer names.

HashMap for efficient O(1) policy lookup by policy number.

TreeMap for maintaining policies sorted by policy number keys.

PriorityQueue for ordering claims by urgency (HIGH before MEDIUM before LOW).

SOLID Design Principles:

SRP: Responsibilities cleanly split across PolicyStore, PremiumCalculator, ClaimService, and AuditLogger.

OCP: New premium types can be added seamlessly without modifying existing calculator logic.

LSP: Any PremiumStrategy implementation is fully interchangeable.

ISP: Observers implement a lightweight ClaimObserver interface with a single method.

DIP: Services depend on abstractions rather than concrete classes.

Design Patterns:

Singleton (AppConfig): Thread-safe enum singleton managing global configurations (companyName, maxClaimAmount).

Factory (PolicyFactory): Centralized instantiation handling known policy types and throwing custom exceptions for unknown types.

Builder (Claim): Fluent inner static builder pattern ensuring immutability of core claim fields post-creation.

Strategy: Modular percentage-based premium calculations for different policy categories.

Observer: Real-time notification dispatch to InAppNotifier and BranchLetterNotifier upon claim status updates.

Robust Exception Hierarchy: Custom runtime exceptions (PolicyServiceException, PolicyNotFoundException, InvalidClaimException, UnknownPolicyTypeException) preventing empty catch blocks.

Auto-Closeable Audit Logging: Secure file writing using Java's try-with-resources statement.

Project Structure
Plaintext
hdfc-life-policy-system/
├── src/
│   └── com/
│       └── hdfclife/
│           ├── Main.java
│           ├── config/
│           │   └── AppConfig.java
│           ├── exception/
│           │   ├── InvalidClaimException.java
│           │   ├── PolicyNotFoundException.java
│           │   ├── PolicyServiceException.java
│           │   └── UnknownPolicyTypeException.java
│           ├── factory/
│           │   └── PolicyFactory.java
│           ├── model/
│           │   ├── Claim.java
│           │   ├── EndowmentPolicy.java
│           │   ├── Policy.java
│           │   ├── TermLifePolicy.java
│           │   ├── UlipPolicy.java
│           │   └── Urgency.java
│           ├── observer/
│           │   ├── BranchLetterNotifier.java
│           │   ├── ClaimEventPublisher.java
│           │   ├── ClaimObserver.java
│           │   └── InAppNotifier.java
│           ├── service/
│           │   ├── AuditLogger.java
│           │   └── ClaimService.java
│           ├── store/
│           │   └── PolicyStore.java
│           └── strategy/
│               ├── EndowmentPremiumStrategy.java
│               ├── PremiumCalculator.java
│               ├── PremiumStrategy.java
│               ├── TermPremiumStrategy.java
│               └── UlipPremiumStrategy.java
├── .gitignore
└── README.md
Getting Started
Prerequisites
Java Development Kit (JDK) 8 or higher installed on your machine.

Compilation & Execution
Open your terminal or command prompt at the root directory (hdfc-life-policy-system/).

Create a target output directory:

Bash
mkdir -p bin
Compile all Java source files:

Bash
javac -d bin src/com/hdfclife/**/*.java
Run the application:

Bash
java -cp bin com.hdfclife.Main
