package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CartPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private static final int STEP_SLEEP_MS = 1000;

    // 1) Sepette ürün birim fiyatı
    private final By cartItemUnitPrice =
            By.xpath("//*[@id=\"app\"]/div[1]/div/div/div[1]/div/div[1]/div[2]/div/div[2]/div/div/div[2]/div/div[1]/div/div/span");

    // 2) Adet liste
    private final By listButton =
            By.id("quantitySelect0-key-0");

    // 3) Adet seç
    private final By listSelect =
            By.xpath("//*[@id=\"quantitySelect0-key-0\"]/option[2]");

    // 3) Adet kontrol
    private final By quantityInput =
            By.xpath("//*[@id=\"quantitySelect0-key-0\"]");

    // 4) Sepeti sil
    private final By decreaseButton =
            By.id("removeCartItemBtn0-key-0");

    // 5) Sepet boş mesajı
    private final By emptyCartText =
            By.id("emtyCart");

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(25));
    }

    public String getCartItemUnitPrice() {
        WebElement priceEl = wait.until(
                ExpectedConditions.visibilityOfElementLocated(cartItemUnitPrice)
        );
        sleepStep();

        String price = priceEl.getText().trim();
        System.out.println("🧾 CART UNIT PRICE: " + price);
        return price;
    }

    // Adedi 2 yap ve doğrula
    public void increaseQuantityTo2() {

        WebElement list = wait.until(ExpectedConditions.presenceOfElementLocated(listButton));
        safeClick(list);
        sleepStep();
        WebElement selectList = wait.until(ExpectedConditions.presenceOfElementLocated(listSelect));
        safeClick(selectList);
        sleepStep();

       /* wait.until(d -> "2 adet".equals(d.findElement(quantityInput).getAttribute("value")));
        sleepStep();

        System.out.println("Adet 2 doğrulandı");
        */
    }

    // Ürünü tamamen sil
    public void removeItemCompletely() {

        WebElement minus = wait.until(ExpectedConditions.presenceOfElementLocated(decreaseButton));
        scrollToCenter(minus);
        safeClick(minus);
        sleepStep();

        wait.until(ExpectedConditions.visibilityOfElementLocated(emptyCartText));
        sleepStep();

        System.out.println("🗑 Ürün silindi, sepet boş");
    }

    public static String normalizePrice(String priceText) {
        if (priceText == null) return "";

        return priceText
                .replace("\u00A0", " ")
                .replace("TL", "")
                .replace(".", "")
                .replace(",", ".")
                .replaceAll("[^0-9.]", "")
                .trim();
    }

    /* ----------------- HELPERS ----------------- */

    private void safeClick(WebElement el) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(el));
            el.click();
        } catch (WebDriverException ex) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
        }
    }

    private void scrollToCenter(WebElement el) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});", el
        );
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
