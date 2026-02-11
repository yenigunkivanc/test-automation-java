package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Random;

public class ProductDetailPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private static final int STEP_SLEEP_MS = 1000;

    // Locators
    private final By productName =
            By.xpath("/html/body/div[3]/div[1]/div[1]/div[2]/div[2]/h1/span");

    private final By productPrice =
            By.id("priceNew");

    private final By addToCartButton =
            By.id("addBasket");

    private final By inStockSizeButtons =
            By.xpath("//*[@id=\"sizes\"]/div");

    private final By goToCartFromPopup =
            By.xpath("/html/body/header/div/div/div[3]/div/a[3]");

    public ProductDetailPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    /* ===================== GETTERS ===================== */

    public String getProductName() {
        String name = wait.until(ExpectedConditions.visibilityOfElementLocated(productName))
                .getText().trim();
        sleepStep();
        return name;
    }

    public String getProductPrice() {
        WebElement el = wait.until(d -> d.findElement(productPrice));
        String price = el.getAttribute("innerText").trim();
        sleepStep();
        return price;
    }

    /* ===================== ACTIONS ===================== */

    public void addToCartSelectingRandomSize() {

        // 1) Sepete ekle (beden popup/list açılır)
        WebElement addBtn = wait.until(ExpectedConditions.presenceOfElementLocated(addToCartButton));
        scrollToCenter(addBtn);
        sleepStep();
        safeClick(addBtn);

        // 3) Stokta olan bedenlerden random seç
        List<WebElement> sizes = wait.until(d -> {
            List<WebElement> els = d.findElements(inStockSizeButtons);
            els.removeIf(e -> !e.isDisplayed());
            return els.isEmpty() ? null : els;
        });

        WebElement choice = sizes.get(new Random().nextInt(sizes.size()));
        scrollToCenter(choice);
        sleepStep();
        safeClick(choice);
        sleepStep();

        // 4) Bazı ürünlerde beden seçince otomatik ekler, bazılarında tekrar "Ekle" ister
        try {
            WebElement addBtn2 = driver.findElement(addToCartButton);
            if (addBtn2.isDisplayed() && addBtn2.isEnabled()) {
                scrollToCenter(addBtn2);
                sleepStep();
                safeClick(addBtn2);
                sleepStep();
            }
        } catch (WebDriverException ignored) {
        }
    }

    public void goToCartFromAddToCartPopup() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(goToCartFromPopup));
        sleepStep();
        safeClick(btn);
        sleepStep();
    }

    /* ===================== HELPERS ===================== */

    private void safeClick(WebElement el) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(el));
            el.click();
        } catch (WebDriverException ex) {
            jsClick(el);
        }
    }

    private void scrollToCenter(WebElement el) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});", el
        );
    }

    private void jsClick(WebElement el) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
    }

    private void sleepStep() {
        try {
            Thread.sleep(STEP_SLEEP_MS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread sleep interrupted", e);
        }
    }
}
