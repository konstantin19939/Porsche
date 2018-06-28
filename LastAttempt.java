import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class LastAttempt {
	
	static WebDriver driver;
	public static double basePrice;
	static List<Double> totalEquipmentPrice;
	JavascriptExecutor executor;
	
	
	@BeforeClass
	public void Load() {
		totalEquipmentPrice = new ArrayList();
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.get("https://www.porsche.com/usa/modelstart/");
		executor = (JavascriptExecutor) driver;
		driver.manage().window().maximize();
	}
	
	@Test(priority=1)
	public void opener() {
		driver.findElement(By.className("b-teaser-preview-wrapper")).click();
		driver.findElement(By.xpath("//*[@id=\"m982120\"]/div[2]/div/a/span")).click();
		
		//driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
	}
	@Test(priority=2)
	public void verifyPrice() {
		String verifyPrice = driver.findElement(By.className("m-14-model-price")).getText();
		 basePrice = Price(verifyPrice);
		
		
		driver.findElement(By.xpath("//*[@id=\"m982120\"]/div[2]/div/a/span")).click();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		String parentWindow = driver.getWindowHandle();
		Set<String> handles = driver.getWindowHandles();
		for (String windowHandle : handles) {
			if (!windowHandle.equals(parentWindow)) {
				driver.switchTo().window(windowHandle);
			}
		}

		String verifyNewBasePrice = driver.findElement(By.xpath("//*[@id=\"s_price\"]/div[1]/div[1]/div[2]")).getText();
		double NewbasePrice = Price(verifyNewBasePrice);
		
		Assert.assertEquals( basePrice , NewbasePrice);
		
		
		
		
	}
	
	@Test(priority=3)
	public void verifyEquipment() {
		String equipmentPrice = driver.findElement(By.xpath("//*[@id=\"s_price\"]/div[1]/div[2]/div[2]")).getText();
		double EPrice = Price(equipmentPrice);
		
		Assert.assertEquals(0.0,EPrice);
				
		
		
	}
	
	@Test(priority=4)
	public void TheSumOf()  {
		System.out.println(basePrice);
		Assert.assertTrue(verifyTPrice(basePrice));
		
	}
	
	@Test(priority=5)
	public void SelectColor()  {
		driver.findElement(By.cssSelector("span[style='background-color: rgb(0, 120, 138);']")).click();
		WebElement colorPrice = driver.findElement(By.xpath("//div[@class='tt_row']/div[@style='visibility: visible;']"));

		
		
		Assert.assertTrue(verifyEPrice(colorPrice));
		Assert.assertTrue(verifyTPrice(basePrice));
	}
	
	@Test (priority=6)
	public void verify_Rim_Price() {

		WebElement element=driver.findElement(By.xpath("//ul[@class='tileWheels']/li[@data-price='$3,750']"));
		executor.executeScript("arguments[0].click();", element);
			
		WebElement RimPElement = driver.findElement(By.xpath("//*[@id='s_exterieur_x_IRA']/div[2]/div[1]/div/div[2]"));

		Assert.assertTrue(verifyEPrice(RimPElement));
		Assert.assertTrue(verifyTPrice(basePrice));
	}

	@Test(priority=7)
	public void verify_Power_Seat_Price() {

		WebElement element = driver.findElement(By.id("s_interieur_x_PP06"));
		executor.executeScript("arguments[0].click();", element);

		WebElement PowerSeatElement = driver.findElement(By.xpath("//*[@id=\"seats_73\"]/div[2]/div[1]/div[3]/div"));

		Assert.assertTrue(verifyEPrice(PowerSeatElement));
		Assert.assertTrue(verifyTPrice(basePrice));
	}

	@Test (priority=8)
	public void verify_Interior_Carbon_Fiber_Price() {
		WebElement interiorCFiber = driver.findElement(By.id("IIC_subHdl"));
		executor.executeScript("arguments[0].click();", interiorCFiber);

		WebElement iCFoption = driver.findElement(By.xpath("//*[@id=\'vs_table_IIC_x_PEKH_x_c01_PEKH\']"));
		executor.executeScript("arguments[0].click();", iCFoption);

		WebElement iCFP = driver.findElement(By.xpath("//*[@id=\"vs_table_IIC_x_PEKH\"]/div[1]/div[2]/div"));

		Assert.assertTrue(verifyEPrice(iCFP));
		Assert.assertTrue(verifyTPrice(basePrice));

	}

	@Test (priority=9)
	public void verify_PDK_PCCB_Price() {
		WebElement Performance = driver.findElement(By.id("IMG_subHdl"));
		executor.executeScript("arguments[0].click();", Performance);
		
		
		WebElement PDK = driver.findElement(By.id("vs_table_IMG_x_M250_x_c14_M250_x_shorttext"));
		executor.executeScript("arguments[0].click();", PDK);

		WebElement pdkP = driver.findElement(By.xpath("//*[@id=\'vs_table_IMG_x_M250\']/div[1]/div[2]/div"));
		Assert.assertTrue(verifyEPrice(pdkP));
		Assert.assertTrue(verifyTPrice(basePrice));

		WebElement PCCB = driver.findElement(By.id("vs_table_IMG_x_M450_x_c94_M450_x_shorttext"));
		executor.executeScript("arguments[0].click();", PCCB);

		WebElement pccbP = driver.findElement(By.xpath("//*[@id=\"vs_table_IMG_x_M450\"]/div[1]/div[2]/div"));
		Assert.assertTrue(verifyEPrice(pccbP));
		Assert.assertTrue(verifyTPrice(basePrice));
	}

	@AfterClass
	public void tearDown() {
		driver.quit();
	}
	
	
	 public static double Price(String str) {
			

		   String temp = new String();
		    for(int i = 0;i<str.length();i++){
		        if(Character.isDigit(str.charAt(i)) ||str.charAt(i) == '.'){
		            temp+=str.charAt(i) + "";
		           
		        } 
		    }

		    return Double.parseDouble(temp);
		}
	
	 public static boolean verifyTPrice(double basePrice)  {
		 
	
		 String equipmentPrice = driver.findElement(By.xpath("//*[@id=\"s_price\"]/div[1]/div[2]/div[2]")).getText();
		 double ePrice = Price(equipmentPrice);
		
		 String deliveryFeePrice =  driver.findElement(By.xpath("//*[@id=\"s_price\"]/div[1]/div[3]/div[2]")).getText();
		 double dPrice = Price(deliveryFeePrice);
	
		 String TotalPrice = driver.findElement(By.xpath("//*[@id=\"s_price\"]/div[1]/div[4]/div[2]")).getText();
		 double TPrice = Price(TotalPrice);
		 
		 if(TPrice== dPrice+ePrice+basePrice) {
			 return true;
		 }else {
			 return false;
		 }
		 
	 }
	 
	 public static boolean verifyEPrice(WebElement element) {
		 String elemPrice = element.getText();
		 double price1 = Price(elemPrice);
		 totalEquipmentPrice.add(price1);
		 
		 String equipmentPrice = driver.findElement(By.xpath("//*[@id=\"s_price\"]/div[1]/div[2]/div[2]")).getText();
		 double ePrice = Price(equipmentPrice);
		 
		 double totalEquipPrice = 0;
		 for(Double double1 : totalEquipmentPrice) {
			 totalEquipPrice+=double1;
			 
		 }
		 if(ePrice==totalEquipPrice) {
			 return true;
		 }else {
			System.out.println("False");
		 }
		 return false;
		 
		 
	 }

}
