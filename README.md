Selenium SauceDemo Automation Project
📌 Project Description

This project is an automation testing project using Selenium WebDriver with Java.
The automation verifies the Add to Cart functionality on SauceDemo website.

Website: https://www.saucedemo.com/

🚀 Tech Stack
Java
Selenium WebDriver
WebDriverManager
Apache POI (Excel Reporting)
Maven
📂 Project Structure
src
 └── main
     └── java
         └── com.juaracoding.evan
             └── AddToCartTest.java

pom.xml
README.md
🧪 Test Cases
Test Case ID	Test Scenario	Description
SIT-001	Login Test	Verify login success
SIT-002	Add To Cart	Verify add to cart functionality
SIT-003	Remove Item	Verify remove item from cart
▶️ How To Run
1. Clone Repository
git clone https://github.com/your-username/your-repo.git
2. Navigate to Project
cd your-repo
3. Run Project
mvn clean install

Then run:

AddToCartTest.java
📊 Test Result

After execution, the system will generate:

SIT_Report.xlsx

Example Output:

Test Case ID	Test Name	Result	Timestamp
SIT-001	Login Test	SUCCESS	2026-03-25
SIT-002	Add To Cart	SUCCESS	2026-03-25
SIT-003	Remove Item	SUCCESS	2026-03-25
📸 Features
Multiple test cases
Excel reporting
Timestamp logging
Clean code structure
Automation using Selenium
👨‍💻 Author

Evan
QA Automation Engineer (Learning Project)