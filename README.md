
# Web Browser Automation BDD Framework

[![Java](https://img.shields.io/badge/Java-21_LTS-orange.svg)](https://openjdk.java.net/)
[![Selenium](https://img.shields.io/badge/Selenium-4.38.0-green.svg)](https://www.selenium.dev/)
[![Cucumber](https://img.shields.io/badge/Cucumber-7.31.0-brightgreen.svg)](https://cucumber.io/)
[![TestNG](https://img.shields.io/badge/TestNG-7.11.0-red.svg)](https://testng.org/)
[![Maven](https://img.shields.io/badge/Maven-3.13.0-blue.svg)](https://maven.apache.org/)

A modern, enterprise-grade BDD test automation framework built with Java 21, Selenium WebDriver, Cucumber, and TestNG. Features parallel execution, multi-environment support, comprehensive reporting, and data-driven testing capabilities.

---


## 📋 Table of Contents
- [Features](#-features)
- [Prerequisites](#-prerequisites)
- [Quick Start](#-quick-start)
- [Project Structure](#-project-structure)
- [Configuration](#-configuration)
- [Running Tests](#-running-tests)
- [Data-Driven Testing](#-data-driven-testing)
- [Reporting](#-reporting)
- [CI/CD Architecture & Jenkins Pipeline](#-cicd-architecture--jenkins-pipeline)
- [Best Practices](#-best-practices)
- [Troubleshooting](#-troubleshooting)
---

## 🏗️ CI/CD Architecture & Jenkins Pipeline
This framework is built to be "Infrastructure as Code" (IaC) ready and leverages a robust Jenkins pipeline for scalable, reproducible automation.

### 1. The Controller-Agent Model
Instead of a monolithic installation, the pipeline uses **Dynamic Ephemeral Agents**:
- **Jenkins Controller:** Orchestrates the workflow and hosts the reporting dashboard.
- **Docker Agent (`markhobson/maven-chrome`):** A sterile, short-lived container containing the exact JDK 21, Maven, and Chrome versions needed. It is created at the start of the build and destroyed immediately after, ensuring no "environment drift."

### 2. The "Magic Bridge" (DooD)
To allow the Jenkins container to manage sibling containers, we mount the host's Docker socket:

```bash
-v /var/run/docker.sock:/var/run/docker.sock
```

---

## 💻 Setup for Interviewers / Developers
To run this framework in a sterile Jenkins environment:
1. **Pull Jenkins:**
    ```bash
    docker pull jenkins/jenkins:lts
    ```
2. **Launch with Socket:**
    ```bash
    docker run -d -p 8080:8080 -v /var/run/docker.sock:/var/run/docker.sock jenkins/jenkins:lts
    ```

---

## 🚀 Features

### Core Capabilities
- ✅ **BDD with Cucumber** - Gherkin syntax for readable test scenarios
- ✅ **Page Object Model** - Maintainable and reusable page components
- ✅ **Parallel Execution** - Run up to 10 tests simultaneously
- ✅ **Multi-Browser Support** - Chrome, Firefox, Safari (local & remote)
- ✅ **Multi-Environment** - PROD, TEST, DEV configurations
- ✅ **Data-Driven Testing** - 4 flexible approaches (Properties, Cucumber Examples, Java Records, Inline)
- ✅ **Triple Reporting** - Extent, Cucumber, and Allure reports
- ✅ **Screenshot on Failure** - Automatic capture for debugging
- ✅ **Headless Mode** - CI/CD pipeline ready
- ✅ **Responsive Testing** - Configurable window sizes (desktop, tablet, mobile)

### Modern Java 21 Features
- 🔹 **Records** - Immutable data carriers (BrowserConfig, WindowSize, ParcelTestData)
- 🔹 **Enhanced Switch** - Pattern matching with yield expressions
- 🔹 **Text Blocks** - Multi-line string literals
- 🔹 **Modern APIs** - URI.create(), Duration-based timeouts

---

## 📦 Prerequisites

### Required
- **Java 21 LTS** or higher ([Download](https://adoptium.net/))
- **Maven 3.6+** ([Download](https://maven.apache.org/download.cgi))
- **Git** ([Download](https://git-scm.com/downloads))

### Optional (for Allure reports)
- **Allure CLI** ([Installation Guide](https://docs.qameta.io/allure/#_installing_a_commandline))

### Verify Installation
```bash
java -version    # Should show Java 21+
mvn -version     # Should show Maven 3.6+
git --version    # Should show Git 2.0+
```

---

## ⚡ Quick Start

### 1. Clone Repository
```bash
git clone https://github.com/bastheboss7/WebBrowserAutomation_BDD_Framework.git
cd WebBrowserAutomation_BDD_Framework
```

### 2. Install Dependencies
```bash
mvn clean install
```

### 3. Run Sample Test
```bash
# Run single test with tag
mvn clean verify -Dcucumber.filter.tags="@ParcelShopFilter"

# Run all tests in parallel
mvn clean verify -Dsurefire.suiteXmlFiles=testngParallel.xml
```

### 4. View Reports
- **Extent Report**: `target/reports/extent-report/index.html`
- **Cucumber Report**: `target/reports/cucumber-report/cucumber-html-reports/overview-features.html`
- **Allure Report**: Run `allure serve target/reports/allure-report`

---

## 📁 Project Structure

```
WebBrowserAutomation_BDD_Framework/
├── src/main/java/
│   ├── features/              # Cucumber feature files (Gherkin)
│   │   └── evriProhibitedItem.feature
│   ├── pages/                 # Page Object Model classes
│   │   ├── EvriHomePage.java
│   │   ├── EvriDeliveryOptionsPage.java
│   │   ├── EvriParcelDetailsPage.java
│   │   ├── EvriParcelShopFinderPage.java
│   │   └── Hooks.java
│   ├── wrappers/              # Selenium wrapper methods
│   │   ├── BasePageObject.java
│   │   ├── GenericWrappers.java
│   │   └── Wrappers.java
│   ├── runner/                # TestNG runner configuration
│   │   └── TestNgRunner.java
│   ├── utils/                 # Utilities (Reporter)
│   │   └── Reporter.java
│   └── resources/
│       ├── config.properties  # Environment & browser config
│       ├── object.properties  # Element locators
│       └── extent-config.xml  # Extent report styling
├── target/reports/            # Generated test reports (auto-generated)
│   ├── extent-report/         # Extent HTML report
│   ├── cucumber-report/       # Cucumber HTML report
│   └── allure-report/         # Allure report data
├── testngParallel.xml         # Parallel execution config (10 threads)
├── pom.xml                    # Maven dependencies & plugins
└── README.md                  # This file
```

---

## ⚙️ Configuration

### Environment Selection

The framework supports **three environments**:

| Environment | URL | Use Case |
|------------|-----|----------|
| **PROD** (default) | https://www.evri.com/ | Production testing |
| **TEST** | https://www.test.evri.com/ | QA/Staging testing |
| **DEV** | https://www.dev.evri.com/ | Development testing |

**Switch Environments:**

```bash
# Method 1: Command line (recommended)
mvn clean verify -Denv=TEST

# Method 2: Edit config.properties
# src/main/resources/config.properties
Environment=TEST
```

**Priority**: `-Denv` > `config.properties` > Default (PROD)

---

### Browser Configuration

#### Headless Mode (CI/CD)
```bash
# Run without visible browser
mvn verify -Dheadless=true

# Combined with environment
mvn verify -Denv=TEST -Dheadless=true
```

#### Window Sizes (Responsive Testing)
```bash
# Predefined sizes
mvn verify -DwindowSize=MAXIMIZED           # Default
mvn verify -DwindowSize=HD_1920x1080        # Full HD
mvn verify -DwindowSize=HD_1366x768         # Laptop HD
mvn verify -DwindowSize=TABLET_768x1024     # iPad
mvn verify -DwindowSize=MOBILE_375x667      # iPhone

# Custom size
mvn verify -DwindowSize=1440x900
```

#### Advanced Options
```properties
# config.properties
Browser=chrome
Browser.Headless=false
Browser.WindowSize=MAXIMIZED
Browser.PageLoadTimeout=60
Browser.ScriptTimeout=30
Browser.UserAgent=Mozilla/5.0...
Browser.DownloadDir=/path/to/downloads
```

#### Combined Examples
```bash
# Headless + Mobile viewport
mvn verify -Dheadless=true -DwindowSize=MOBILE_375x667

# Test environment + Tablet size
mvn verify -Denv=TEST -DwindowSize=TABLET_768x1024

# Full customization
mvn verify -Denv=PROD -Dheadless=true -DwindowSize=HD_1920x1080 -DpageLoadTimeout=90
```

---

## 🧪 Running Tests

### Command Line Execution

#### Run All Tests (Parallel)
```bash
mvn clean verify -Dsurefire.suiteXmlFiles=testngParallel.xml
```

#### Run with Specific Tags
```bash
# Single tag
mvn clean verify -Dcucumber.filter.tags="@ParcelShopFilter"

# Multiple tags (AND)
mvn clean verify -Dcucumber.filter.tags="@Smoke and @Regression"

# Multiple tags (OR)
mvn clean verify -Dcucumber.filter.tags="@Smoke or @Regression"

# Exclude tags
mvn clean verify -Dcucumber.filter.tags="not @Skip"
```

#### Run Specific Feature File
```bash
mvn test -Dcucumber.features=src/main/java/features/evriProhibitedItem.feature
```

### IDE Execution (IntelliJ IDEA)

1. **Run TestNG Suite**: Right-click `testngParallel.xml` → Run as TestNG Suite
2. **Run Runner Class**: Right-click `TestNgRunner.java` → Run 'TestNgRunner'
3. **Run Feature File**: Right-click any `.feature` file → Run Feature
4. **Run Scenario**: Click green arrow next to scenario in feature file

### Parallel Execution Configuration

**Enable/Disable Parallel Execution:**

Edit `src/main/java/runner/TestNgRunner.java`:
```java
@DataProvider(name = "scenarios", parallel = true)  // Set to false to disable
public Object[][] features() {
    return testNGCucumberRunner.provideScenarios();
}
```

**Configure Thread Count:**

Edit `testngParallel.xml`:
```xml
<suite name="ParallelSuite" thread-count="10" parallel="methods">
```

---

## 📊 Data-Driven Testing

This framework showcases **4 different approaches** for storing and using test data, demonstrating flexibility for various testing scenarios.

---

### Approach 1: Inline Test Data (Hardcoded)

**Use Case**: Simple, one-off tests with fixed values

**Example**: `EvriParcelShopFinderPage.java`
```java
@When("I select the {string} link from the {string} menu")
public void iSelectTheLinkFromTheMenu(String linkText, String menuName) {
    // Direct URL navigation with hardcoded value
    getDriver().get("https://www.evri.com/find-a-parcelshop");
}
```

**Feature File**:
```gherkin
Scenario: User can filter ParcelShops by location
  When I search for ParcelShops in "Edinburgh"
  Then I should see only ParcelShops with postcodes starting with "EH"
```

**Pros**: Simple, fast, no external dependencies  
**Cons**: Not reusable, hard to maintain

---

### Approach 2: Properties Files (Configuration)

**Use Case**: Environment configs, locators, system settings

**Example**: `config.properties`
```properties
Environment=PROD
Browser=chrome
Browser.Headless=false
Browser.PageLoadTimeout=60
```

**Example**: `object.properties`
```properties
homepage.searchButton=css:button[data-test-id='search']
homepage.postcodeInput=id:postcode
```

**Usage**:
```java
String browser = getProperties().getProperty("Browser");
String locator = prop.getProperty("homepage.searchButton");
```

**Pros**: Centralized config, easy to update, version controlled  
**Cons**: Not suitable for large datasets

---

### Approach 3: Cucumber Examples (Data Tables)

**Use Case**: Multiple test iterations with small to medium datasets

**Feature File**:
```gherkin
Scenario Outline: User searches on Google
  Given I navigate to Google homepage
  When I search for "<SearchTerm>"
  Then I should see results containing "<ExpectedResult>"

  Examples:
    | SearchTerm         | ExpectedResult |
    | Selenium WebDriver | selenium.dev   |
    | GitHub Copilot     | github.com     |
    | TestNG Framework   | testng.org     |
```

**Execution**: Automatically runs 3 times with different data

**Pros**: Readable, integrated with BDD, version controlled  
**Cons**: Limited to small datasets, not reusable across features

---

### Approach 4: Java Records (Type-Safe Data)

**Use Case**: Complex test data with validation, immutable data carriers

**Implementation**: `ParcelTestData.java` (Java 21 Record)
```java
public record ParcelTestData(
    String fromPostcode,
    String toPostcode,
    String weight,
    String parcelContents,
    String value
) {
    // Validation in compact constructor
    public ParcelTestData {
        if (fromPostcode == null || fromPostcode.isBlank()) {
            throw new IllegalArgumentException("From postcode required");
        }
    }
    
    // Factory methods for common scenarios
    public static ParcelTestData validParcel() {
        return new ParcelTestData("BD2 3FG", "LS2 7EP", "1kg - 2kg", "Books", "50");
    }
    
    public static ParcelTestData prohibitedItem() {
        return new ParcelTestData("BD2 3FG", "LS2 7EP", "1kg - 2kg", "gun", "100");
    }
}
```

**Usage in Tests**:
```java
@When("I enter valid parcel details")
public void enterValidParcelDetails() {
    ParcelTestData data = ParcelTestData.validParcel();
    enterByElement(contentsField, data.parcelContents());
    enterByElement(valueField, data.value());
}

@When("I enter prohibited item details")
public void enterProhibitedItemDetails() {
    ParcelTestData data = ParcelTestData.prohibitedItem();
    enterByElement(contentsField, data.parcelContents());
}
```

**Pros**: Type-safe, compile-time validation, immutable, reusable  
**Cons**: Requires code changes for new data


---

### Current Implementation in Framework

**Live Examples in Codebase**:

| Approach | Location | Description |
|----------|----------|-------------|
| **Inline Data** | `evriProhibitedItem.feature` | Hardcoded values in Gherkin scenarios |
| **Properties** | `config.properties`<br>`object.properties` | Environment config & element locators |
| **Cucumber Examples** | Ready for any `.feature` file | Data tables with scenario outlines |
| **Java Records** | `GenericWrappers.java` | `WindowSize` & `BrowserConfig` records |

---

### Comparison Matrix

| Approach | Maintainability | Scalability | Business User | Type Safety | Best For |
|----------|----------------|-------------|---------------|-------------|----------|
| **Inline Data** | Low | Low | ❌ | ✅ | Quick tests |
| **Properties** | High | Medium | ❌ | ❌ | Configuration |
| **Cucumber Examples** | High | Medium | ✅ | ❌ | BDD scenarios |
| **Java Records** | High | Medium | ❌ | ✅ | Complex data |

This demonstrates **comprehensive test data management** suitable for enterprise frameworks

---

## 📈 Reporting

The framework generates **three comprehensive reports** automatically after each test run.

### 1. Extent Reports (Primary)

**Location**: `target/reports/extent-report/index.html`

**Features**:
- ✅ Dashboard with pass/fail statistics
- ✅ Screenshot for each step
- ✅ Test execution timeline
- ✅ Environment details
- ✅ Exception stack traces

**View Report**:
```bash
open target/reports/extent-report/index.html
```

### 2. Cucumber HTML Reports

**Location**: `target/reports/cucumber-report/cucumber-html-reports/overview-features.html`

**Features**:
- ✅ Feature-wise breakdown
- ✅ Step-by-step execution details
- ✅ Tag-based filtering
- ✅ Pass/fail/skip statistics

**View Report**:
```bash
open target/reports/cucumber-report/cucumber-html-reports/overview-features.html
```

### 3. Allure Reports (Interactive)

**Location**: `target/reports/allure-report/`

**Installation** (one-time):
```bash
# macOS
brew install allure

# Windows (Scoop)
scoop install allure

# Manual: https://github.com/allure-framework/allure2/releases
```

**Generate & View**:
```bash
# Method 1: Auto-open in browser
allure serve target/reports/allure-report

# Method 2: Generate static HTML
allure generate target/reports/allure-report -o allure-html-report --clean
open allure-html-report/index.html
```

**Features**:
- ✅ Interactive charts & graphs
- ✅ Test case history
- ✅ Categorized failures
- ✅ Environment & executor info
- ✅ Timeline & retry tracking

---

## 🔧 CI/CD Integration

### Jenkins Pipeline Example

```groovy
pipeline {
    agent any
    
    tools {
        maven 'Maven 3.9'
        jdk 'JDK 21'
    }
    
    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/bastheboss7/WebBrowserAutomation_BDD_Framework.git'
            }
        }
        
        stage('Run Tests') {
            steps {
                sh '''
                    mvn clean verify \
                    -Denv=TEST \
                    -Dheadless=true \
                    -DwindowSize=HD_1920x1080 \
                    -Dsurefire.suiteXmlFiles=testngParallel.xml
                '''
            }
        }
        
        stage('Publish Reports') {
            steps {
                // Extent Report
                publishHTML([
                    allowMissing: false,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: 'target/reports/extent-report',
                    reportFiles: 'index.html',
                    reportName: 'Extent Report'
                ])
                
                // Allure Report
                allure([
                    includeProperties: false,
                    jdk: '',
                    properties: [],
                    reportBuildPolicy: 'ALWAYS',
                    results: [[path: 'target/reports/allure-report']]
                ])
            }
        }
    }
    
    post {
        always {
            junit 'target/surefire-reports/*.xml'
        }
    }
}
```

### GitHub Actions Example

```yaml
name: BDD Tests

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main ]

jobs:
  test:
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v3
    
    - name: Set up JDK 21
      uses: actions/setup-java@v3
      with:
        java-version: '21'
        distribution: 'temurin'
    
    - name: Cache Maven packages
      uses: actions/cache@v3
      with:
        path: ~/.m2
        key: ${{ runner.os }}-m2-${{ hashFiles('**/pom.xml') }}
    
    - name: Run Tests
      run: |
        mvn clean verify \
        -Denv=TEST \
        -Dheadless=true \
        -DwindowSize=HD_1920x1080 \
        -Dsurefire.suiteXmlFiles=testngParallel.xml
    
    - name: Upload Extent Report
      if: always()
      uses: actions/upload-artifact@v3
      with:
        name: extent-report
        path: target/reports/extent-report/
    
    - name: Upload Allure Results
      if: always()
      uses: actions/upload-artifact@v3
      with:
        name: allure-results
        path: target/reports/allure-report/
```

---

## 💡 Best Practices

### 1. Feature File Organization
```gherkin
@Smoke @Regression
Feature: ParcelShop Finder
  As a customer
  I want to find ParcelShops near me
  So that I can drop off my parcels

  Background:
    Given I am on the evri.com homepage

  @ParcelShopFilter @Priority1
  Scenario: Filter ParcelShops by location
    When I select the "Find a ParcelShop" link from the "ParcelShops" menu
    And I search for ParcelShops in "Edinburgh"
    Then I should see only ParcelShops with postcodes starting with "EH"
```

### 2. Page Object Best Practices
```java
public class EvriHomePage extends BasePageObject {
    
    // Element locators using @FindBy
    @FindBy(css = "input[data-test-id='search']")
    private WebElement searchInput;
    
    // Reusable methods
    public void searchLocation(String location) {
        enterByElement(searchInput, location);
        reportStep("Searched for: " + location, "PASS");
    }
    
    // Use framework wrapper methods
    // - clickByElement()
    // - enterByElement()
    // - waitForElementToBeClickable()
}
```

### 3. Test Data Management
- Use Java Records for type-safe, validated test data
- Use Cucumber Examples for small to medium datasets
- Use Properties files for configuration and locators
- Keep inline data for simple, one-off tests

### 4. Error Handling
```java
try {
    clickByElement(element);
    reportStep("Action successful", "PASS");
} catch (Exception e) {
    reportStep("Action failed: " + e.getMessage(), "FAIL");
    throw new RuntimeException("Test failed", e);
}
```

### 5. Logging & Reporting
- Use `reportStep()` for every action
- Take screenshots on failure (automatic)
- Add meaningful messages to assertions
- Use appropriate log levels (PASS/FAIL/WARN/INFO)

---

## 🐛 Troubleshooting

### Common Issues

#### 1. Tests Not Running
```bash
# Issue: mvn run not working
# Solution: Use correct Maven goal
mvn clean verify  # NOT mvn run
```

#### 2. Browser Not Launching
```bash
# Issue: Browser not launching
# Solution: WebDriverManager handles drivers automatically
# If issues persist, clear cache and retry:
rm -rf ~/.cache/selenium/
mvn clean verify
```

#### 3. Element Not Found
- Verify element locator in `object.properties`
- Check if page has loaded completely
- Add explicit waits: `waitForElementToBeClickable()`

#### 4. Parallel Execution Failures
- Reduce thread count in `testngParallel.xml`
- Ensure proper ThreadLocal driver management
- Check for shared state between tests

#### 5. Report Generation Failed
```bash
# Clean target and rebuild
mvn clean install
mvn verify -Dsurefire.suiteXmlFiles=testngParallel.xml
```

### Debug Mode
```bash
# Run with debug logging
mvn clean verify -X -Dsurefire.suiteXmlFiles=testngParallel.xml
```

---

## 📚 Additional Resources

### Documentation
- [Selenium WebDriver](https://www.selenium.dev/documentation/)
- [Cucumber](https://cucumber.io/docs/cucumber/)
- [TestNG](https://testng.org/doc/)
- [Java 21 Features](https://openjdk.org/projects/jdk/21/)

### Framework-Specific
- **Java 21 Features**: Modern records, switch expressions, text blocks
- **Page Object Model**: Reusable page components in `src/main/java/pages/`
- **Wrapper Methods**: Selenium utilities in `GenericWrappers.java`
- **Test Data**: 4 flexible approaches (Inline, Properties, Cucumber Examples, Java Records)

---

## 🤝 Contributing

1. Fork the repository
2. Create feature branch: `git checkout -b feature/your-feature`
3. Commit changes: `git commit -am 'Add new feature'`
4. Push to branch: `git push origin feature/your-feature`
5. Create Pull Request

---

## 📝 License

This project is open source and available under the MIT License.

---

## 👤 Author

**Baskar P**
- GitHub: [@bastheboss7](https://github.com/bastheboss7)
- Repository: [WebBrowserAutomation_BDD_Framework](https://github.com/bastheboss7/WebBrowserAutomation_BDD_Framework)

---

## 🎯 Roadmap

### Future Enhancements
- [ ] Mobile automation (Appium integration)
- [ ] API testing integration (RestAssured)
- [ ] Visual regression testing (Applitools/Percy)
- [ ] Docker containerization
- [ ] Kubernetes support
- [ ] Performance testing integration (JMeter)
- [ ] Database validation support
- [ ] Cross-browser cloud testing (BrowserStack/Sauce Labs)

---

**Last Updated**: November 2025  
**Framework Version**: 2.0  
**Java Version**: 21 LTS 
