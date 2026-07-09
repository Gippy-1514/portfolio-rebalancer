## Portfolio Rebalancer Framework:
This framework is designed to help investors manage and rebalance their investment portfolios effectively.

## Installation Steps:
1. Clone the repository to your local machine:
   git clone https://github.com/Gippy-1514/portfolio-rebalancer.git
   
2. Import the project into IntelliJ IDEA as a Maven project. 
3. Run via IDE: Right-click RebalancerEngineTest.java and select Run.
4. Alternatively, run via CLI: Execute mvn clean test in your terminal. 
5. View Results: Open target/extent-reports/Dashboard.html in your web browser.

## Tech Stack:
   Language Engine: Java 23
   Testing Framework: TestNG (v7.10.2)
   Build Architecture: Apache Maven (v3.9.x) & Maven Surefire Plugin (v3.2.5)
   Reporting Engine: Extent Reports (v5.1.2)
   CI/CD Platform: GitHub Actions Cloud Runners

## Project Structure:

📦 crd-technical-assessment
├─ .github/workflows/maven.yml # Cloud CI/CD pipeline script
├─ src/main/java/com/crd
│  ├─ analytics                # Portfolio drift algorithms
│  ├─ rebalancer               # Core rebalancing engine logic
│  ├─ util                     # Config reader utilities
│  └─ validation               # Data threshold safety guards
├─ src/main/resources
│  └─ config.properties        # Portfolio datasets & inputs
├─ src/test/java/com/crd
│  ├─ rebalance                # TestNG test cases
│  └─ reporting                # Extent HTML report setup
├─ Jenkinsfile                 # Alternative pipeline script
├─ pom.xml                     # Maven project dependencies
└─ README.md                   # Project documentation

## Automated Test Matrix:

1. **`testHappyPathAccountABC` (Positive Flow)**
    * Parses live inputs from `config.properties`, runs the calculation engine, and verifies that generated trade orders match expected targets down to a tolerance of `0.0001`.
2. **`testInvalidTargetAllocationSum` (Negative Flow)**
    * Inputs a flawed matrix where asset allocations do not equal 100%, verifying that the engine catches the error and throws an `IllegalArgumentException`.
3. **`testInvalidUnitPrice` (Edge Case Flow)**
    * Inputs a zero or negative asset price, confirming that defensive compliance bounds successfully trigger an exception.

## CI/CD Orchestration Layer:

This framework features fully automated Pipeline-as-Code using GitHub Actions (maven.yml).
Every push to GitHub triggers a cloud runner to isolate a clean Java 23 environment, execute the TestNG grid, and archive the Extent Report as a downloadable artifact.