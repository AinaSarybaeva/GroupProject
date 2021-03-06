import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.By;
import org.testng.Assert;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class ProjectOne {
    public static void main(String[] args) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "/Users/apple/Desktop/sdet-java/Selenium/chromedriver");
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        driver.get("https://orangehrm-demo-6x.orangehrmlive.com/client/#/dashboard");

        // 1. Login as administrator
        driver.findElement(By.xpath("//input[@id='btnLogin']")).click();

        // 2. Open Admin -> Announcements -> News
        driver.findElement(By.xpath("//li[@id='menu_admin_viewAdminModule']")).click();
        driver.findElement(By.cssSelector("li[id='menu_news_Announcements']")).click();
        driver.findElement(By.xpath("//a[@id='menu_news_viewNewsList']")).click();

        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        driver.switchTo().frame("noncoreIframe");

        // 3. Store all date in Map<String, List <String>>

        List<WebElement> topicList = driver.findElements(By.xpath("//tr[@class='dataRaw']/td[2]"));
        List<WebElement> dateList = driver.findElements(By.xpath("//tr[@class='dataRaw']/td[3]"));
        List<WebElement> userRoles = driver.findElements(By.xpath("//tr[@class='dataRaw']/td[6]"));
        List<WebElement> attachment = driver.findElements(By.xpath("//tr[@class='dataRaw']/td[7]/i"));

        // handling attachments
        List<String> attachmentYN = new ArrayList<>();
        for (var i = 0; i < attachment.size(); i++) {
            if (attachment.get(i).getAttribute("class").contains("disabled")) {     //getting the value from attribute('class')
                attachmentYN.add("No");
            } else {
                attachmentYN.add("Yes");
            }
        }

        Map<String, List<String>> newsList = new HashMap<>();

        for (int i = 0; i < topicList.size(); i++) {
            List<String> temp = new LinkedList<>();
            temp.add(dateList.get(i).getText());
            temp.add(userRoles.get(i).getText());
            temp.add(attachmentYN.get(i));

            newsList.put(topicList.get(i).getText(), temp);
        }

        // a. Print out the count of news
        System.out.println("News List: " + newsList.size());

        // b. Print out the map
        for (String key : newsList.keySet()) {
            System.out.println(String.valueOf(key) + " | " + newsList.get(key).get(0) + " | " + newsList.get(key).get(1) + " | " + newsList.get(key).get(2));
        }

        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);


        // 4. Add new News item
        driver.findElement(By.cssSelector("i[class='large material-icons']")).click();
        driver.findElement(By.cssSelector("input[id='news_topic']")).sendKeys("Congratulations Anna");

        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

        driver.switchTo().frame("news_description_ifr");

        driver.findElement(By.cssSelector("body[id='tinymce']")).click();
        driver.findElement(By.cssSelector("body[id='tinymce']")).sendKeys("Promotion was awarded to Anna on 1/7/2020");

        // 5. Next button
        driver.switchTo().parentFrame();
        driver.findElement(By.xpath("//button[@class='modal-action waves-effect action-btn btn right cancel-btn']")).click();

        // 6. Check "Publish to all other Roles"
        driver.findElement(By.xpath("//label[contains(text(), 'Publish To - All User Roles')]")).click();

        // 7.Publish
        driver.findElement(By.xpath("//button[@class='modal-action waves-effect action-btn btn right cancel-btn publish-btn']")).click();
        Thread.sleep(4000);


        // 8. Verify count of news are more now than it was before
        List<WebElement> newsList1 = driver.findElements(By.cssSelector(".dataRaw"));
        Assert.assertEquals(newsList.size() + 1, newsList1.size()); // + 1 cause the new list is 1 more
        // Assert.assertTrue(newsList.size()+1==newsList1.size());

        // 9. Verify your news is displayed on the current table
        String actualTopicName = driver.findElement(By.xpath("//tr[@class='dataRaw'][1]/td[2]")).getText();
        Assert.assertEquals(actualTopicName, "Congratulations Anna");

        driver.findElement(By.xpath("//i[@class='material-icons']")).click();
        driver.findElement(By.id("logoutLink")).click();

        // 10. Log in as "1st level supervisor"
        driver.findElement(By.id("loginAsButtonGroup")).click();
        driver.findElement(By.xpath("//a[@data-password='kevin.mathews']")).click();

        //  Navigate to news section
        driver.findElement(By.xpath("//li[@id='menu_news_More']")).click();
        driver.findElement(By.xpath("//li[@id='menu_news_viewAnnouncementModule']")).click();
        driver.findElement(By.id("menu_news_viewNewsArticles")).click();

        // 11. In News section verify your newly added item is present
        List<WebElement> newItem = driver.findElements(By.xpath("//div[@id='header']"));
        for (WebElement item : newItem) {
            if (item.getText().equals("Congratulations Anna")) {
                System.out.println("Newly present item is present");

            }

            // 12. Verify Topic and Description values are same

            Assert.assertEquals(driver.findElement(By.xpath("//div[@id='header']")).getText(), "Congratulations Anna");
            Assert.assertEquals(driver.findElement(By.xpath("[//div[@class='html-content']//p)[1]")).getText(), "Promotion was awarded to Anna on 1/7/2020");


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

            Thread.sleep(3000);
            driver.close();

        }
    }
}
