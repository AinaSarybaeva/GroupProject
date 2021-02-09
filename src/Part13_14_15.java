import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

public class Part13_14_15 {

    public static void main(String[] args) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\F\\Desktop\\Projects\\Selenium\\libs\\Browers\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        //logout

        driver.findElement(By.id("account-job")).click();
        driver.findElement(By.id("logoutLink")).click();

        // 13 Log in as Administrator

        driver.findElement(By.xpath("//input[@type='submit']")).click();

        //14 Open Admin -> Announcements -> News

        driver.findElement(By.id("menu_admin_viewAdminModule")).click();
        driver.findElement(By.id("menu_news_Announcements")).click();
        driver.findElement(By.id("menu_news_viewNewsList")).click();

        // 15 Check the item and delete it

        String text = driver.findElement(By.xpath("//*[@id=\"resultTable\"]/tbody/tr[1]/td[2]/a")).getText();
        driver.findElement(By.xpath("(//tr[@class='dataRaw']//td//label)[1]")).click();
        driver.findElement(By.xpath("//*[@id=\"frmList_ohrmListComponent_Menu\"]/i")).click();
        driver.findElement(By.id("newsDelete")).click();
        driver.findElement(By.id("news-delete-button")).click();

    }
}
