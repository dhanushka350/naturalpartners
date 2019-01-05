package com.akvasoft.natural_partners.config;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.bind.annotation.RestController;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;

@RestController
public class Scrape implements InitializingBean {
    private FirefoxDriver driver = null;
    private FirefoxDriver driver2 = null;

    @Override
    public void afterPropertiesSet() throws InterruptedException {
        System.setProperty("webdriver.gecko.driver", "/var/lib/tomcat8/geckodriver");
        FirefoxOptions options = new FirefoxOptions();
        options.setHeadless(false);
        driver = new FirefoxDriver(options);
        driver2 = new FirefoxDriver(options);
        JavascriptExecutor executor = (JavascriptExecutor) driver;

        List<String> list = new ArrayList<>();
        String nutritional_supplements = "https://www.naturalpartners.com/us/browse/nutritional-supplements/_/N-102458";
        String vitamins_and_minerals = "https://www.naturalpartners.com/us/browse/vitamins-minerals/_/N-102591";
        String herbs = "https://www.naturalpartners.com/us/browse/herbs/_/N-102428";
        String personal_care = "https://www.naturalpartners.com/us/browse/personal-care-specialty/_/N-102554";
        String acupuncture_oriental_medicine = "https://www.naturalpartners.com/us/browse/acupuncture-oriental-medicine/_/N-102719";
        String prescription_roducts = "https://www.naturalpartners.com/us/browse/prescription-products/_/N-102723";
        String animal_health = "https://www.naturalpartners.com/us/browse/animal-health/_/N-102415";
        String homeopathy = "https://www.naturalpartners.com/us/browse/homeopathy/_/N-102454";

        list.add(nutritional_supplements);
        list.add(vitamins_and_minerals);
        list.add(herbs);
        list.add(personal_care);
        list.add(acupuncture_oriental_medicine);
        list.add(prescription_roducts);
        list.add(animal_health);
        list.add(homeopathy);

        for (String s : list) {
            driver.get(s);
            Thread.sleep(10000);
            WebElement main = driver.findElementByXPath("/html/body/div[4]/div/div/div/div[2]").findElement(By.tagName("section")).findElements(By.xpath("./*")).get(1);
            WebElement select = main.findElement(By.tagName("form")).findElement(By.tagName("div")).findElement(By.className("results")).findElement(By.className("sellers")).findElement(By.tagName("select"));
            new Select(select).selectByVisibleText("64");

            boolean firstAttempt = true;

            while (true) {
                while (!firstAttempt) {
                    Thread.sleep(10000);
                    main = driver.findElementByXPath("/html/body/div[4]/div/div/div/div[2]").findElement(By.tagName("section")).findElements(By.xpath("./*")).get(1);
                    WebElement next = main.findElement(By.className("nap_cat-action-bar")).findElement(By.tagName("form")).findElement(By.tagName("div")).findElement(By.className("pagination")).findElements(By.xpath("./*")).get(4);
                    System.out.println(next.getAttribute("class"));
                    executor.executeScript("arguments[0].click();", next.findElement(By.tagName("a")));
                    Thread.sleep(10000);
                    collectLinks();
                    continue;
                }

                if (firstAttempt) {
                    collectLinks();
                    firstAttempt = false;
                }

            }

        }

    }

    private void collectLinks() throws InterruptedException {
        System.out.println("qweqweq");
        WebElement main = driver.findElementByXPath("/html/body/div[4]/div/div/div/div[2]").findElement(By.tagName("section")).findElements(By.xpath("./*")).get(1);
        WebElement products = null;
        List<String> links = new ArrayList<>();

        String category = driver.findElementByXPath("/html/body/div[4]/div[1]/div/div[1]").getAttribute("innerText");
        String aClass = main.findElements(By.xpath("./*")).get(9).getAttribute("class");

        if (aClass.equalsIgnoreCase("row collapse")) {
            products = main.findElements(By.xpath("./*")).get(9);
        } else {
            products = main.findElements(By.xpath("./*")).get(8);
        }

        List<WebElement> list = products.findElement(By.tagName("div")).findElement(By.tagName("ul")).findElements(By.xpath("./*"));
        for (WebElement product : list) {
            String tagName = product.getTagName();
            if (tagName.equalsIgnoreCase("li")) {
                String link = product.findElement(By.tagName("div")).findElements(By.xpath("./*")).get(1).findElement(By.tagName("a")).getAttribute("href");
                links.add(link);
            }
        }


        for (String link : links) {
            driver2.get(link);
            Thread.sleep(10000);
            WebElement imageDiv = driver2.findElement(By.className("content")).findElements(By.xpath("./*")).get(0).findElement(By.tagName("div")).findElements(By.xpath("./*"))
                    // get 1 to next part
                    .get(2).findElements(By.xpath("./*")).get(1).findElements(By.xpath("./*")).get(0).findElements(By.xpath("./*")).get(0).findElement(By.tagName("div"))
                    .findElement(By.className("thumbList")).findElement(By.className("productScroll")).findElement(By.id("galleryThumb")).findElement(By.className("owl-wrapper-outer"))
                    .findElement(By.className("owl-wrapper"));

            for (WebElement href : imageDiv.findElements(By.xpath("./*"))) {
                String image = href.findElement(By.tagName("div")).findElement(By.tagName("a")).getAttribute("data-image");
                System.out.println(image);
            }


            WebElement details = driver2.findElement(By.className("content")).findElements(By.xpath("./*")).get(0)
                    .findElement(By.tagName("div")).findElements(By.xpath("./*")).get(2).findElements(By.xpath("./*"))
                    .get(1).findElements(By.xpath("./*")).get(1);
            System.out.println(details.getAttribute("class") + " classss");

            WebElement name = details.findElement(By.className("product-information")).findElement(By.tagName("div"))
                    .findElement(By.tagName("div")).findElement(By.tagName("h1")).findElement(By.tagName("div"));

            WebElement price = details.findElement(By.className("product-information")).findElement(By.tagName("div"))
                    .findElement(By.tagName("div")).findElement(By.className("show-for-small-up")).findElement(By.tagName("div"))
                    .findElements(By.xpath("./*")).get(0).findElement(By.tagName("div")).findElement(By.id("skublock")).findElements(By.xpath("./*")).get(2);


            WebElement desc = details.findElements(By.xpath("./*")).get(1).findElements(By.xpath("./*")).get(1);

            System.out.println(name.findElements(By.xpath("./*")).get(0).getAttribute("innerText") + "==");
            System.out.println(name.findElements(By.xpath("./*")).get(1).getAttribute("innerText") + "==");
            System.out.println(name.findElements(By.xpath("./*")).get(2).getAttribute("innerText") + "==");

            System.out.println(price.getAttribute("innerText").split(" ")[price.getAttribute("innerText").split(" ").length - 1] + "==");
            System.out.println(desc.getAttribute("innerText") + "==");
            System.out.println(category + "==category");

        }

    }
}
