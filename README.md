# ProjectLens Test Automation Framework

A clean, standard **Selenium WebDriver + TestNG Page Object Model (POM)** test automation framework with **Excel Data-Driven Testing (DDT)** and **ExtentReports**.

---

## 📁 Project Architecture (Page Object Model)

```
testing-pl/
│
├── src/main/java/com/cts/
│   ├── pages/                       # 1. PAGE OBJECTS (Locators + Actions)
│   │   ├── BasePage.java            # Common wrapper methods (click, enterText, getText, isDisplayed)
│   │   ├── LoginPage.java           # Login page locators and login actions
│   │   ├── DashboardPage.java       # Navigation, sidebar, and logout actions
│   │   ├── ProjectSubmissionPage.java # Project idea submission form
│   │   ├── SubmissionsPage.java     # Pod submissions list and evaluation cards
│   │   ├── ReviewsPage.java         # Trainer review queue and decision panel
│   │   ├── NotificationsPage.java   # System notifications and alert verification
│   │   └── ReviseSubmissionPage.java# Revision workflow and resubmission
│   │
│   └── utils/                       # 2. HELPER UTILITIES
│       ├── ConfigReader.java        # Loads config.properties (baseUrl, browser, timeouts)
│       ├── DriverManager.java       # ThreadLocal WebDriver manager (Chrome, Edge, Firefox)
│       ├── ExcelReader.java         # Reads test data rows from TestData.xlsx using Apache POI
│       ├── DataProviders.java       # TestNG @DataProvider methods feeding data to test cases
│       ├── WaitUtils.java           # Explicit waits (WebDriverWait) for element visibility/clickable
│       └── ReportManager.java       # Configures and writes the Extent HTML report
│
├── src/test/java/com/cts/
│   ├── base/
│   │   └── BaseTest.java            # Setup (@BeforeMethod) and Teardown (@AfterMethod)
│   │
│   ├── listeners/
│   │   └── TestListener.java        # TestNG Listener: creates Extent tests & attaches screenshots on failure
│   │
│   └── tests/                       # 3. TEST SCRIPTS (Assertions only)
│       ├── LoginTest.java           # Module 1: Login & authentication scenarios
│       ├── TrainerReviewTest.java   # Module 2: Trainer review, approve, revision, and reject
│       ├── ReviseSubmissionTest.java# Module 3: Pod Lead revision workflow
│       ├── FormValidationTest.java  # Module 4: Form submission validation & cohort criteria
│       ├── NotificationTest.java    # Module 5: Notifications & alerts
│       └── FormDataUpdationTest.java# Module 6: End-to-end form data persistence across roles
│
├── src/test/resources/
│   ├── config/
│   │   └── config.properties        # App URL, browser, and timeout settings
│   └── testdata/
│       └── TestData.xlsx            # Excel workbook containing test data sheets for all modules
│
├── test-output/
│   └── ExecutionReport.html         # The single, rich interactive Extent Report
│
├── testng.xml                       # Master TestNG suite runner
├── run-all.bat                      # 1-Click Windows execution script
└── pom.xml                          # Maven build file (Selenium, TestNG, POI, ExtentReports)
```

---

## 🚀 How to Run the Tests

### Option 1: 1-Click Execution (Windows)
Double-click `run-all.bat` in the project root. It executes the entire test suite and reports the final status.

### Option 2: Command Line (Maven)
- **Run all modules together:**
  ```bash
  mvn test
  ```
- **Run a single test module:**
  ```bash
  mvn test -Dtest=LoginTest
  mvn test -Dtest=TrainerReviewTest
  mvn test -Dtest=FormValidationTest
  mvn test -Dtest=NotificationTest
  mvn test -Dtest=ReviseSubmissionTest
  mvn test -Dtest=FormDataUpdationTest
  ```
- **Run a single specific test case:**
  ```bash
  mvn test -Dtest=LoginTest#testTC02_VerifyLoginWithValidCredentials
  ```

---

## 📊 Viewing the Test Report

After running tests, open the single generated Extent HTML report in your web browser:
- **File path:** `test-output/ExecutionReport.html`
- **Features:**
  - Executive dashboard with Pass / Fail / Skip charts
  - Categorized tabs for each test module
  - Execution duration and timestamps
  - Embedded Base64 screenshots captured automatically on test failure

---

## 💡 Quick Interview Cheat-Sheet (Theory & Framework Explanation)

### Q1: What is Page Object Model (POM)?
> **Answer:** POM is a design pattern where each web page is represented as a separate Java class. The class contains:
> 1. Private element locators (`By` variables).
> 2. Public action methods (e.g. `login()`, `clickSignIn()`).
> **Benefit:** If UI locators change, we only update the Page class without touching test scripts, reducing test maintenance.

### Q2: How is Data-Driven Testing (DDT) implemented?
> **Answer:** We use **Apache POI** (`ExcelReader.java`) to read test scenarios and credentials from `TestData.xlsx`. The data is converted into `Object[][]` by TestNG `@DataProvider` in `DataProviders.java`. Each test method receives its input parameters dynamically from the Excel sheet.

### Q3: How do you handle browser initialization and teardown?
> **Answer:** We have `BaseTest.java` with `@BeforeMethod` and `@AfterMethod`:
> - `@BeforeMethod`: Calls `DriverManager.initDriver()` which launches Chrome in maximized mode and navigates to `baseUrl`.
> - `@AfterMethod`: Calls `DriverManager.quitDriver()` which closes the browser session after every test.
> - `DriverManager` uses `ThreadLocal<WebDriver>` to ensure thread safety during parallel execution.

### Q4: How is reporting handled?
> **Answer:** We use **ExtentReports 5**. We implemented TestNG's `ITestListener` in `TestListener.java`. 
> - When a test passes, it logs `PASS` with execution time.
> - When a test fails, it captures a screenshot using `TakesScreenshot` and attaches the Base64 image directly to the Extent Report at `test-output/ExecutionReport.html`.

### Q5: How do you handle dynamic waits?
> **Answer:** We avoid hardcoded `Thread.sleep()`. Instead, we use `WebDriverWait` (explicit wait) in `WaitUtils.java` with `ExpectedConditions` like `visibilityOfElementLocated` and `elementToBeClickable`.
