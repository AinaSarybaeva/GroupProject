import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

public class LastThreeSteps {
    public static void main(String[] args) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "/Users/chi_town/Downloads/chromedriver");
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);


        // 15. Check the item and delete it
        String text = driver.findElement(By.xpath("//*[@id=\"resultTable\"]/tbody/tr[1]/td[2]/a")).getText();
        driver.findElement(By.xpath("(//tr[@class='dataRaw']//td//label)[1]")).click();
        driver.findElement(By.xpath("//*[@id=\"frmList_ohrmListComponent_Menu\"]/i")).click();
        driver.findElement(By.id("newsDelete")).click();
        driver.findElement(By.id("news-delete-button")).click();
// 16. Verify that item doesn"t exist in the table anymore
        if (driver.findElement(By.xpath("//*[@id=\"resultTable\"]/tbody/tr[1]/td[2]/a")).equals(text)) {
            System.out.println(text + " name still exists");
        } else {
            System.out.println(text + " name does not exist");
        }
        // 17. Verify row count is one less after delete
        String rowCountBefore = driver.findElement(By.id("frmList_ohrmListComponenttotal")).getText();
        int rowCountBeforeInt = Integer.parseInt(rowCountBefore);
        System.out.println(rowCountBeforeInt);
        Thread.sleep(5000);
        String rowCountAfter = driver.findElement(By.id("frmList_ohrmListComponenttotal")).getText();
        int rowCountAfterInt = Integer.parseInt(rowCountAfter);
        System.out.println(rowCountAfterInt);
        if ((rowCountBeforeInt - 1) == rowCountAfterInt) {
            System.out.println("Row count is one less!");
        } else {
            System.out.println("Row count is the same!");
        }
    }
}