package edu.bu.schin8.test.sdet;

import org.apache.log4j.LogManager;
import org.example.Main;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.TestInfo;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import static org.junit.jupiter.api.Assertions.assertEquals;

public class WordleTest {
    private static final Logger LOG = LoggerFactory.getLogger(WordleTest.class);

    public static final String BASEURL = "https://www.nytimes.com/games/wordle/index.html";
    public static final String HOW_TO_PLAY = "How To Play";
    public static final String GUESS_IN_6_TRIES = "Guess the Wordle in 6 tries.";
    public static final String INSTRUCTION_1 = "Each guess must be a valid 5-letter word.";
    public static final String INSTRUCTION_2 = "The color of the tiles will change to show how close your guess was to the word.";
    public static final String EXAMPLE_START = "Examples";
    public static final String W_EXAMPLE = "W is in the word and in the correct spot.";
    public static final String I_EXAMPLE = "I is in the word but in the wrong spot.";
    public static final String U_EXAMPLE = "U is not in the word in any spot.";
    public static final String STATS_MSG = "Log in or create a free NYT account to link your stats.";
    public static final String SIGNUP_1 = "A new puzzle is released daily at midnight. If you haven’t already, you can sign up";
    public static final String SIGNUP_2 = "for our daily reminder email.";

    private WebDriver driver;
    private WebElement howToPlayDialog;

    @BeforeEach
    public void testSetup(TestInfo testInfo) throws Exception {
        try {
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--headless");
            options.addArguments("--incognito");
            driver = new ChromeDriver(options);

            LOG.info(String.format("Starting %s",testInfo.getDisplayName()));

            driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
            driver.manage().window().maximize();
            driver.get(BASEURL);

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            /*
                When running in incognito mode, we'll get a "Updated terms" dialog.  hit We’ve updated our terms to quit
            */
            WebElement updatedTermsbutton = wait.until(ExpectedConditions.elementToBeClickable(By.className("purr-blocker-card__button")));

            // Click the button
            updatedTermsbutton.click();

            // Looks like the developer provided data-testid, I'll take advantage of it to find the play button
            WebElement playButton = driver.findElement(By.cssSelector("[data-testid='Play']"));
            playButton.click();

            howToPlayDialog = driver.findElement(By.id("help-dialog"));

        } catch (Exception e) {
            throw new Exception("testSetup problem detected");
        }
    }

    @AfterEach
    public void testCleanup(TestInfo testInfo) {
        if (driver != null) {
            driver.close();
        }
        LOG.info(String.format("Completed %s",testInfo.getDisplayName()));
    }

    /***
     * Test the contents of the dialog
     */
    @Test
    void readHowToPlay() {
        // WebElement howToPlayHeading = howToPlayDialig.findElement(By.tagName("h2"));
        // assertEquals(HOW_TO_PLAY, howToPlayHeading.getText());

        // Instead of pulling out each element, just extract the Entire dialog text.
        String dialogText = howToPlayDialog.getText();

        // Check to see if the Heading text is there
        assertThat(dialogText, containsString(HOW_TO_PLAY));
        assertThat(dialogText, containsString(GUESS_IN_6_TRIES));
        assertThat(dialogText, containsString(INSTRUCTION_1));
        assertThat(dialogText, containsString(INSTRUCTION_2));
        assertThat(dialogText, containsString(EXAMPLE_START));
        assertThat(dialogText, containsString(W_EXAMPLE));
        assertThat(dialogText, containsString(I_EXAMPLE));
        assertThat(dialogText, containsString(U_EXAMPLE));
        assertThat(dialogText, containsString(STATS_MSG));
        assertThat(dialogText, containsString(SIGNUP_1));
        assertThat(dialogText, containsString(SIGNUP_2));

        // Find the close button within the dialog by its aria-label attribute (I didn't have luck using data-testid='icon-close'`)
        WebElement closeButton = howToPlayDialog.findElement(By.cssSelector("button[aria-label='Close']"));
        closeButton.click();

        skipHowToPlay();
    }

    @Test
    public void verifyTitleOnPageIsWordle() {
        // SDET Challenge says the title should be just "Wordle", but it's not.  Check with developer
        // String expectedTitle="Worlde";
        String expectedTitle = "Wordle — The New York Times";
        skipHowToPlay();

        // Get the page title
        String title = driver.getTitle();
        assertThat(String.format("The title should be %s", expectedTitle), title, is(expectedTitle));
    }

    @Test
    public void verifyBoardExists(){
        skipHowToPlay();
        int expectedRows = 6;
        int expectedCols = 5;

        // It would be nice if there was an aria-label, but there isn't!  Use XPath to filter on the "constant part" of
        // the element name.
        WebElement boardElement = driver.findElement(By.xpath("//*[contains(@class, 'Board-module_board')]"));
        assertThat("Board element should be present", boardElement, is(notNullValue()));

        // Get all rows
        List<WebElement> rows = boardElement.findElements(By.xpath(".//*[contains(@class, 'Row-module_row')]"));
        assertThat(String.format("Number of Rows is %s",rows.size()), rows.size(), is(expectedRows));

        // Loop through each row, and check that the columns found is expected
        for (int i = 0; i < rows.size(); i++) {
            WebElement row = rows.get(i);

            // Get all columns within the current row
            List<WebElement> columns = row.findElements(By.xpath(".//*[contains(@class, 'Tile-module_tile')]"));
            assertThat(String.format("Number of Columns in row %d is %d", i + 1, columns.size()), columns.size(), is(expectedCols));
        }
    }

    // Helper
    private void skipHowToPlay() {
        // Find the close button within the dialog by its aria-label attribute (I didn't have luck using data-testid='icon-close'`)
        WebElement closeButton = howToPlayDialog.findElement(By.cssSelector("button[aria-label='Close']"));
        closeButton.click();
    }
}
