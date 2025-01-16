# SDET Challenge

### Requirements

| tools    | required | Notes                                            |
| -------- | -------- | ------------------------------------------------ |
| Java 17  | yes      |                                                  |
| git      | yes      |                                                  |
| Chrome   | yes      |                                                  |
| maven    | no       | maven wrappers have been provided in the project |
| IntelliJ | optional | Run tests in IDE                                 |

### Setup

- Get access to the repository https://github.com/boston-homelab/testing-sdet-challenge
  -  If the repository is private, contact me at c.steve98@gmail.com

- Clone the repository

```
# using https
git clone https://github.com/boston-homelab/testing-sdet-challenge.git
# using ssh
git clone git@github.com:boston-homelab/testing-sdet-challenge.git
```

###  Local Testing

#### From the command line

Use the maven wrapper (included with the project) `mvnw` or `mvnw.cmd` to ensure the same maven version is used between development/build environments.

The tests will use Chrome in incognito mode and headless (Chrome will not open) to visually see the testing on the Chrome browser and modify the test code by commenting out the option setting the `--headless` argument in the method `testSetup()`.

##### Building

This step will compile and install (if needed).  Skipping the test is optional.

```
 ./mvnw install -DskipTests
```

##### Testing

This step will build if needed, then test

```
./mvnw test
```

#### Testing in Github

The repository is located at https://github.com/boston-homelab/testing-sdet-challenge.

GitHub Actions will run the Action `Test Wordle` when a Pull Request is created.  A schedule has been created to run the action every night around 9 pm.

To test or review previously run tests, click the "Actions" tab in the repository, Find the action `Test Wordle` under `Actions,` and click it.

To run the `Test Wordle` action, click on the `Run workflow` button.

To Review previous test runs, click on the name of the workflow run, then click on ` build` in the `test.yaml` section.

### Adding tests

Tests are written using Java,  JUnit 5, and Selenium.

#### Create a branch

Branches should begin with the issue number (if available)

```
git switch -c [issue number]-branch-name
```

#### Committing code 

Using keywords `closes, close, fix, fixes, resolve, resolves` etc, with the issue number will close on merge.

```
git add [files]
git commit -m "resolves #123.  Added new tests"
```

#### Pushing code to the remote repository

```
git push
```

#### Test on pull request

Create a Pull Request in GitHub, the GitHub action will automatically run.



## Improvements

Given more time, I would do the following:

1. Create a Base class that contains commonly used properties and methods for the Wordle tests; that way, when I create a new Test file, I can extend the Base Class.  I would make the methods more generic so I can reuse them.

   Examples:

   	- When in incognito mode, a dialog always pops up for the user to accept "Updated terms."
   	- We must Click the Play Button before we get to the Wordle screen.
   	- I already have skipHowToPlay(), enterWordWithScreenKeyboad(String theWord), pressEnterWithKeyboard().

2. Refactoring code.  I'd go back and find places where I could benefit by using Java Streams (for readability)

3. I would add to the Maven pom support to use the `Allure Framework` to provide informative test reports.

4. I would update my `test/resource/log4j.xml` file to log "Debug" information for code in my tests to see the debug output if needed.

5. I'd remove org.example tests (Used to create the initial project and test out my configuration), left in in there so I can use them as a sanity check

6. I would add to the GitHub Action "Test Wordle" a step to run code coverage (maven jacoco-maven-plugin)

7. If needed, I would add a step to the GitHub Action "Test Wordle" to run SonarQube, which scans code and vulnerabilities and performs code smells.

8. I would create a separate `GitHub Action` to run OWASP tests nightly or weekly to spot vulnerabilities earlier.  

9. I'm pretty sure I could have used Parameterized tests, but If I had the time, I'd go back and refactor my tests to use them if it makes sense.

10. I'd include actions/cache to speed up my builds instead of  "cache: 'maven'" in the java-setup action as the former is not tool/language dependent and if the project was in a different language I wouldn't need to figure out how to cache build artifacts since `actions/cache` will handle that for me.

11. There is nothing to Deliver in this case, but if I had an actual service, I'd look into automating the Build, test, and deployment in a GitHub action.

12. I'd go back and find any sensitive information and use GitHub actions to store it in Settings "Secrets and variables" and update my Actions to use them.

13. I'd re-write the entire project gradle and Kotlin as I'm fond of Kotlin :)

14. I'm currently tracking issues and tasks in GitHub `Issues`, but I'd prefer to move these to Jira.

