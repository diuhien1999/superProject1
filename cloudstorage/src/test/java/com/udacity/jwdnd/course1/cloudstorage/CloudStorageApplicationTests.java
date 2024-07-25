package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import java.io.File;
import java.time.Duration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doMockSignUp(String firstName, String lastName, String userName, String password){
		// Create a dummy account for logging in later.

		// Visit the sign-up page.
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		driver.get("http://localhost:" + this.port + "/signup");
		webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));

		// Fill out credentials
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("firstName")));
		WebElement inputFirstName = driver.findElement(By.id("firstName"));
		inputFirstName.click();
		inputFirstName.sendKeys(firstName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("lastName")));
		WebElement inputLastName = driver.findElement(By.id("lastName"));
		inputLastName.click();
		inputLastName.sendKeys(lastName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
		WebElement inputUsername = driver.findElement(By.id("username"));
		inputUsername.click();
		inputUsername.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
		WebElement inputPassword = driver.findElement(By.id("password"));
		inputPassword.click();
		inputPassword.sendKeys(password);

		// Attempt to sign up.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
		WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
		buttonSignUp.click();

		/* Check that the sign up was successful.
		// You may have to modify the element "success-msg" and the sign-up
		// success message below depening on the rest of your code.
		*/
		Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up! Please login to continue"));
	}



	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doLogIn(String userName, String password)
	{
		// Log in to our dummy account.
		driver.get("http://localhost:" + this.port + "/login");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
		WebElement loginUserName = driver.findElement(By.id("username"));
		loginUserName.click();
		loginUserName.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
		WebElement loginPassword = driver.findElement(By.id("password"));
		loginPassword.click();
		loginPassword.sendKeys(password);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
		WebElement loginButton = driver.findElement(By.id("login-button"));
		loginButton.click();

		webDriverWait.until(ExpectedConditions.titleContains("Home"));

	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
	 * rest of your code.
	 * This test is provided by Udacity to perform some basic sanity testing of
	 * your code to ensure that it meets certain rubric criteria.
	 *
	 * If this test is failing, please ensure that you are handling redirecting users
	 * back to the login page after a succesful sign up.
	 * Read more about the requirement in the rubric:
	 * https://review.udacity.com/#!/rubrics/2724/view
	 */
	@Test
	public void testRedirection() {

		// Create a test account
		doMockSignUp("Redirection","Test","RT","123");

		// Check if we have been redirected to the log in page.
		Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
	 * rest of your code.
	 * This test is provided by Udacity to perform some basic sanity testing of
	 * your code to ensure that it meets certain rubric criteria.
	 *
	 * If this test is failing, please ensure that you are handling bad URLs
	 * gracefully, for example with a custom error page.
	 *
	 * Read more about custom error pages at:
	 * https://attacomsian.com/blog/spring-boot-custom-error-page#displaying-custom-error-page
	 */
	@Test
	public void testBadUrl() {
		// Create a test account
		doMockSignUp("URL","Test","UT","123");
		doLogIn("UT", "123");

		// Try to access a random made-up URL.
		driver.get("http://localhost:" + this.port + "/abc");
		Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));
	}


	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
	 * rest of your code.
	 * This test is provided by Udacity to perform some basic sanity testing of
	 * your code to ensure that it meets certain rubric criteria.
	 *
	 * If this test is failing, please ensure that you are handling uploading large files (>1MB),
	 * gracefully in your code.
	 *
	 * Read more about file size limits here:
	 * https://spring.io/guides/gs/uploading-files/ under the "Tuning File Upload Limits" section.
	 */
	@Test
	public void testLargeUpload() {
		// Create a test account
		doMockSignUp("Large File","Test","LFT","123");
		doLogIn("LFT", "123");

		// Try to upload an arbitrary large file
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		String fileName = "upload5m.zip";

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
		WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
		fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

		WebElement uploadButton = driver.findElement(By.id("uploadButton"));
		uploadButton.click();
		try {
			webDriverWait.until(ExpectedConditions.titleContains("Home"));
		} catch (TimeoutException e) {
			System.out.println("Large File upload failed");
		}
		Assertions.assertTrue(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));
	}

	/**
	 * View uploaded file
	 */
	@Test
	public void testViewUploadedFile() {
		// Create a test count
		doMockSignUp("hien", "tran", "viewFile", "123");
		doLogIn("viewFile", "123");

		// Upload a valid file
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		webDriverWait.until(ExpectedConditions.titleContains("Home"));
		String fileName = "fileTestUpload.xlsx";

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
		WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
		fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

		WebElement uploadButton = driver.findElement(By.id("uploadButton"));
		uploadButton.click();

		webDriverWait.until(ExpectedConditions.titleContains("Home"));
		Assertions.assertTrue(driver.getPageSource().contains("fileTestUpload.xlsx"));
	}

	/**
	 * Delete file
	 */
	@Test
	public void testDeleteFile() {
		// Create a test count
		doMockSignUp("Hien", "Tran", "deleteFile", "123");
		doLogIn("deleteFile", "123");

		// Upload a valid file
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		String fileName = "fileTestDelete.xlsx";

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
		WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
		fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("uploadButton")));
		WebElement uploadButton = driver.findElement(By.id("uploadButton"));
		uploadButton.click();

		driver.get("http://localhost:" + this.port + "/home");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("deleteFile")));
		WebElement deleteFile = driver.findElement(By.id("deleteFile"));
		deleteFile.click();

		Assertions.assertEquals("http://localhost:" + this.port + "/home", driver.getCurrentUrl());

	}

	/**
	 * Add new note
	 */
	@Test
	public void testAddNote() {
		// Create a test count
		doMockSignUp("Hien", "Tran", "addNote", "123");
		doLogIn("addNote", "123");

		// Upload a note
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		WebElement noteTab = driver.findElement(By.id("nav-notes-tab"));
		noteTab.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add-new-note")));
		WebElement addNewNoteBtn = driver.findElement(By.id("add-new-note"));
		addNewNoteBtn.click();

		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("noteModal")));
		WebElement noteTitleTxt = driver.findElement(By.id("note-title"));
		noteTitleTxt.sendKeys("Test add note title");

		WebElement noteDescriptionTxt = driver.findElement(By.id("note-description"));
		noteDescriptionTxt.sendKeys("Test add note description");

		WebElement saveChangesBtn = driver.findElement(By.id("save-note-button"));
		saveChangesBtn.click();

		Assertions.assertEquals("http://localhost:" + this.port + "/home", driver.getCurrentUrl());
	}

	/**
	 * Edit note
	 */
	@Test
	public void testEditNote() {
		// Create a test count
		doMockSignUp("Hien", "Tran", "editNote", "123");
		doLogIn("editNote", "123");

		// Upload a note
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		WebElement noteTab = driver.findElement(By.id("nav-notes-tab"));
		noteTab.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add-new-note")));
		WebElement addNewNoteBtn = driver.findElement(By.id("add-new-note"));
		addNewNoteBtn.click();

		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("noteModal")));
		WebElement noteTitleTxt = driver.findElement(By.id("note-title"));
		noteTitleTxt.sendKeys("Test add note title");

		WebElement noteDescriptionTxt = driver.findElement(By.id("note-description"));
		noteDescriptionTxt.sendKeys("Test add note description");

		WebElement saveChangesBtn = driver.findElement(By.id("save-note-button"));
		saveChangesBtn.click();

		WebElement noteTab1 = driver.findElement(By.id("nav-notes-tab"));
		noteTab1.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("viewNoteButton")));
		WebElement viewNoteBtn = driver.findElement(By.id("viewNoteButton"));
		viewNoteBtn.click();

		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("noteModal")));
		noteTitleTxt = driver.findElement(By.id("note-title"));
		noteTitleTxt.clear();
		noteTitleTxt.sendKeys("Test edit note title");

		noteDescriptionTxt = driver.findElement(By.id("note-description"));
		noteDescriptionTxt.clear();
		noteDescriptionTxt.sendKeys("Test add note description Editted");

		saveChangesBtn = driver.findElement(By.id("save-note-button"));
		saveChangesBtn.click();

		Assertions.assertEquals("http://localhost:" + this.port + "/home", driver.getCurrentUrl());
	}

	/**
	 * Delete note
	 */
	@Test
	public void testDeleteNote() {
		// Create a test count
		doMockSignUp("Hien", "Tran", "deleteNote", "123");
		doLogIn("deleteNote", "123");

		// Upload a note
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		WebElement noteTab = driver.findElement(By.id("nav-notes-tab"));
		noteTab.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add-new-note")));
		WebElement addNewNoteBtn = driver.findElement(By.id("add-new-note"));
		addNewNoteBtn.click();

		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("noteModal")));
		WebElement noteTitleTxt = driver.findElement(By.id("note-title"));
		noteTitleTxt.sendKeys("Test add note title");

		WebElement noteDescriptionTxt = driver.findElement(By.id("note-description"));
		noteDescriptionTxt.sendKeys("Test add note description");

		WebElement saveChangesBtn = driver.findElement(By.id("save-note-button"));
		saveChangesBtn.click();

		WebElement noteTab1 = driver.findElement(By.id("nav-notes-tab"));
		noteTab1.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("viewNoteButton")));
		WebElement deleteBtn = driver.findElement(By.id("delete-note-button"));
		deleteBtn.click();

		webDriverWait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("viewNoteButton")));
		Assertions.assertFalse(driver.getPageSource().contains("Test add note title"));
	}

	/**
	 * Add new credential
	 */
	@Test
	public void testAddCredential() {
		// Create a test count
		doMockSignUp("Hien", "Tran", "addCre", "123");
		doLogIn("addCre", "123");

		// Upload a note
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		WebElement credentialsTab = driver.findElement(By.id("nav-credentials-tab"));
		credentialsTab.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add-credentials-button")));
		WebElement addNewCredentialBtn = driver.findElement(By.id("add-credentials-button"));
		addNewCredentialBtn.click();

		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("credentialModal")));
		WebElement credentialUrl = driver.findElement(By.id("credential-url"));
		credentialUrl.sendKeys("add url");
		WebElement credentialUsername = driver.findElement(By.id("credential-username"));
		credentialUsername.sendKeys("add username");
		WebElement credentialPassword = driver.findElement(By.id("credential-password"));
		credentialPassword.sendKeys("addPassword");
		WebElement saveChangesBtn = driver.findElement(By.id("save-credential-button"));
		saveChangesBtn.click();

		WebElement credentialsTab1 = driver.findElement(By.id("nav-credentials-tab"));
		credentialsTab1.click();

		Assertions.assertTrue(driver.getPageSource().contains("add url"));
	}

	/**
	 * Edit credential
	 */
	@Test
	public void testEditCredential() {
		// Create a test count
		doMockSignUp("Hien", "Tran", "editCre", "123");
		doLogIn("editCre", "123");

		// Upload a note
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		WebElement credentialsTab = driver.findElement(By.id("nav-credentials-tab"));
		credentialsTab.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add-credentials-button")));
		WebElement addNewCredentialBtn = driver.findElement(By.id("add-credentials-button"));
		addNewCredentialBtn.click();

		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("credentialModal")));
		WebElement credentialUrl = driver.findElement(By.id("credential-url"));
		credentialUrl.sendKeys("add url");
		WebElement credentialUsername = driver.findElement(By.id("credential-username"));
		credentialUsername.sendKeys("add username");
		WebElement credentialPassword = driver.findElement(By.id("credential-password"));
		credentialPassword.sendKeys("addPassword");
		WebElement saveChangesBtn = driver.findElement(By.id("save-credential-button"));
		saveChangesBtn.click();

		WebElement credentialsTab1 = driver.findElement(By.id("nav-credentials-tab"));
		credentialsTab1.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("viewCredentialButton")));
		WebElement viewCredentialBtn = driver.findElement(By.id("viewCredentialButton"));
		viewCredentialBtn.click();

		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("credentialModal")));
		credentialUrl = driver.findElement(By.id("credential-url"));
		credentialUrl.clear();
		credentialUrl.sendKeys("edit url");
		credentialUsername = driver.findElement(By.id("credential-username"));
		credentialUsername.clear();
		credentialUsername.sendKeys("edit username");
		credentialPassword = driver.findElement(By.id("credential-password"));
		credentialPassword.clear();
		credentialPassword.sendKeys("editPassword");
		saveChangesBtn = driver.findElement(By.id("save-credential-button"));
		saveChangesBtn.click();

		Assertions.assertTrue(driver.getPageSource().contains("edit url"));
	}

	/**
	 * Delete credential
	 */
	@Test
	public void testDeleteCredential() {
		// Create a test count
		doMockSignUp("Hien", "Tran", "deleteCre", "123");
		doLogIn("deleteCre", "123");

		// Upload a note
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		WebElement credentialsTab = driver.findElement(By.id("nav-credentials-tab"));
		credentialsTab.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add-credentials-button")));
		WebElement addNewCredentialBtn = driver.findElement(By.id("add-credentials-button"));
		addNewCredentialBtn.click();

		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("credentialModal")));
		WebElement credentialUrl = driver.findElement(By.id("credential-url"));
		credentialUrl.sendKeys("add url");
		WebElement credentialUsername = driver.findElement(By.id("credential-username"));
		credentialUsername.sendKeys("add username");
		WebElement credentialPassword = driver.findElement(By.id("credential-password"));
		credentialPassword.sendKeys("addPassword");
		WebElement saveChangesBtn = driver.findElement(By.id("save-credential-button"));
		saveChangesBtn.click();

		WebElement credentialsTab1 = driver.findElement(By.id("nav-credentials-tab"));
		credentialsTab1.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("viewCredentialButton")));
		WebElement deleteCredentialBtn = driver.findElement(By.id("delete-credential-button"));
		deleteCredentialBtn.click();

		webDriverWait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("viewCredentialButton")));
		Assertions.assertFalse(driver.getPageSource().contains("add url"));
	}
}
