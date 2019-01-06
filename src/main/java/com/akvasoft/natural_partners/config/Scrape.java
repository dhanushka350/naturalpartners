package com.akvasoft.natural_partners.config;

import com.akvasoft.natural_partners.Modal;
import com.akvasoft.natural_partners.ProductRepo;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;

@RestController
public class Scrape implements InitializingBean {
    private FirefoxDriver driver = null;
    private FirefoxDriver driver2 = null;

    @Autowired
    private ProductRepo productRepo;

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

        list.add(animal_health);
        list.add(prescription_roducts);
        list.add(acupuncture_oriental_medicine);
        list.add(personal_care);
        list.add(herbs);
        list.add(vitamins_and_minerals);
        list.add(nutritional_supplements);
        list.add(homeopathy);

        int count = 1;
        for (String s : list) {
            String cate = getCurrentCategory(count);
            driver.get(s);
            Thread.sleep(5000);
            WebElement main = driver.findElementByXPath("/html/body/div[4]/div/div/div/div[2]").findElement(By.tagName("section")).findElements(By.xpath("./*")).get(1);
            WebElement select = main.findElement(By.tagName("form")).findElement(By.tagName("div")).findElement(By.className("results")).findElement(By.className("sellers")).findElement(By.tagName("select"));
            new Select(select).selectByVisibleText("64");

            boolean firstAttempt = false;
            boolean endCategory = false;
            while (true) {

                while (!firstAttempt) {
                    Thread.sleep(5000);
                    main = driver.findElementByXPath("/html/body/div[4]/div/div/div/div[2]").findElement(By.tagName("section")).findElements(By.xpath("./*")).get(1);
                    String page = main.findElement(By.className("nap_cat-action-bar")).findElement(By.tagName("form")).findElement(By.tagName("div")).findElement(By.className("pagination")).getAttribute("innerText");
                    try {
                        WebElement next = main.findElement(By.className("nap_cat-action-bar")).findElement(By.tagName("form")).findElement(By.tagName("div")).findElement(By.className("pagination")).findElements(By.xpath("./*")).get(4);
                        executor.executeScript("arguments[0].click();", next.findElement(By.tagName("a")));
                    } catch (IndexOutOfBoundsException f) {
                        System.out.println("No paginations");
                        Thread.sleep(10000);
                        collectLinks(cate);
                        endCategory = true;
                        break;
                    }

                    page = page.replace("<", "");
                    page = page.replace(">", "");
                    System.out.println(page.split("of")[0].trim() + "-" + page.split("of")[1].trim() + "===========================");

                    if (page.split("of")[0].trim().equalsIgnoreCase(page.split("of")[1].trim())) {
                        endCategory = true;
                        break;
                    }

                    Thread.sleep(10000);
                    collectLinks(cate);
                }

                if (firstAttempt) {
                    collectLinks(cate);
                    firstAttempt = false;
                }

                if (endCategory) {
                    System.out.println("next category");
                    break;
                }

            }
        }

    }

    private String getCurrentCategory(int count) {

        if (count == 7) {
            return "nutritional-supplements";
        } else if (count == 6) {
            return "vitamins-minerals";
        } else if (count == 5) {
            return "herbs";
        } else if (count == 4) {
            return "personal-care-specialty";
        } else if (count == 3) {
            return "acupuncture-oriental-medicine";
        } else if (count == 2) {
            return "prescription-products";
        } else if (count == 1) {
            return "animal-health";
        } else {
            return "homeopathy";
        }
    }

    private void collectLinks(String cate) throws InterruptedException {
        Modal modal = null;

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


        // save data

        for (String link : links) {
            modal = new Modal();
            driver2.get(link);
            Thread.sleep(5000);
            WebElement imageDiv = driver2.findElement(By.className("content")).findElements(By.xpath("./*")).get(0).findElement(By.tagName("div")).findElements(By.xpath("./*"))
                    // get 1 to next part
                    .get(2).findElements(By.xpath("./*")).get(1).findElements(By.xpath("./*")).get(0).findElements(By.xpath("./*")).get(0).findElement(By.tagName("div"))
                    .findElement(By.className("thumbList")).findElement(By.className("productScroll")).findElement(By.id("galleryThumb")).findElement(By.className("owl-wrapper-outer"))
                    .findElement(By.className("owl-wrapper"));

            for (WebElement href : imageDiv.findElements(By.xpath("./*"))) {
                String image = href.findElement(By.tagName("div")).findElement(By.tagName("a")).getAttribute("data-image");
                System.out.println(image);

                modal.setImages(modal.getImages() + image + ",");
            }


            WebElement details = driver2.findElement(By.className("content")).findElements(By.xpath("./*")).get(0)
                    .findElement(By.tagName("div")).findElements(By.xpath("./*")).get(2).findElements(By.xpath("./*"))
                    .get(1).findElements(By.xpath("./*")).get(1);
//            System.out.println(details.getAttribute("class") + " classss");

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
//            System.out.println(category + "==category");

            modal.setCategory(cate);
            modal.setTitle(name.findElements(By.xpath("./*")).get(0).getAttribute("innerText"));
            modal.setName(name.findElements(By.xpath("./*")).get(1).getAttribute("innerText"));
            modal.setCode(name.findElements(By.xpath("./*")).get(2).getAttribute("innerText"));
            modal.setPrice(price.getAttribute("innerText").split(" ")[price.getAttribute("innerText").split(" ").length - 1]);
            modal.setDesc(desc.getAttribute("innerText").replace("\n", " "));
            if (productRepo.findTopByCodeEqualsAndAndCategoryEquals(modal.getCode(), cate) == null) {
                productRepo.save(modal);
            } else {
                System.err.println("already exist..");
            }

        }

    }
}
