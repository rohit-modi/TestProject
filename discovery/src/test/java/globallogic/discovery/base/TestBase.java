package globallogic.discovery.base;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class TestBase {
	
	WebDriver driver;
	WebDriverWait wait;
	List<WebElement> listofElement,ListofAntother;
	BufferedWriter br;
	
	@ BeforeTest
	public void launchBrowser()
	{
		System.setProperty("webdriver.chrome.driver", "./driver/chromedriver.exe");
		driver=new ChromeDriver();
		driver.get("https://go.discovery.com/");
		driver.manage().window().maximize();
		wait=new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.titleContains("Discovery - Official"));
	}
	
	
	@Test
	public void TC002()
	{
		JavascriptExecutor js = (JavascriptExecutor) driver;
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	
		
		// Xpath for '>' button 	
		WebElement ele=	driver.findElement(By.xpath("//*[@class='popularShowsCarousel__header']/..//*[@class='icon-arrow-right']"));
	    if(!ele.isDisplayed())
	    {
	    	Assert.assertTrue(false, " > element doesn't exist finding using xpath //*[@class='popularShowsCarousel__header']/..//*[@class='icon-arrow-right']");
	    }
	    else
	    {
	    	Assert.assertTrue(true,"Element > exist and clicked");
	    }
		// Looping to reach last show
	    
	     performMultipleClick(ele);
	
	
	// xpath for last show "Explore the show" button
			
	ele=driver.findElement(By.xpath("//*[@class='carousel-tile-wrapper carousel__tileWrapper' and @aria-hidden='false']//button"));
	js.executeScript("window.scrollBy(0,250)", "");
	
	if(ele.isDisplayed())
	{
		ele.click();
		Assert.assertTrue(true, "last show 'Explore the show' button exist");
	}
	else
	{
		Assert.assertTrue(false, "last show 'Explore the show' button does not exist");
	}
	//xpath for Show more button
	js.executeScript("window.scrollBy(0,250)", "");
	ele=driver.findElement(By.xpath("//*[@class='episodeList__showMoreButton']"));
	wait.until(ExpectedConditions.visibilityOf(ele));	

	// Click  more button to shows all the show
	
	if(ele.isDisplayed())
	{
		Assert.assertTrue(true, "show more button button exist");
	}
	else
	{
		Assert.assertTrue(false, "show more button doesn't exist");
	}
	
	
	performMultipleClick(ele);
	// get the list of show's title and duration and store to array list and arraylist to file 
    listofElement= driver.findElements(By.xpath("//ul[@class='episodeList__list']//*[@class='episodeTitle']"));
	ListofAntother= driver.findElements(By.xpath("//ul[@class='episodeList__list']//*[@class='minutes']"));
	try
	{
		File f=new File("./file/output.txt");
		if(!f.exists())
		{
		f.createNewFile();	
		}
		FileWriter fso =new FileWriter(f);
		 br=new BufferedWriter(fso);
		
		
	}catch(IOException e)
	{
	System.out.println("Input output exception occured");	
	}
	
	for(int i=0;i<listofElement.size();i++)
	{
		//System.out.println(listofElement.get(i)+"------ "+ListofAntother.get(i));
		WebElement ele_title=listofElement.get(i);
		wait.until(ExpectedConditions.visibilityOf(ele_title));
		WebElement ele_duration=ListofAntother.get(i);
		wait.until(ExpectedConditions.visibilityOf(ele_duration));

		System.out.println(ele_title.getText()+" == "+ele_duration.getText());
		try {
			br.write(ele_title.getText()+" == "+ele_duration.getText());
			br.write("\n");
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	try {
		br.flush();
		br.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
	}
	@AfterTest
	public void tearDown()
	{
		driver.quit();
	}


	private void performMultipleClick(WebElement ele) {
		try {
		    while(ele.isDisplayed())
			{
				ele.click();
				wait.until(ExpectedConditions.visibilityOf(ele));	

			}
		  }catch(StaleElementReferenceException e)
			{
				//e.printStackTrace();
			}
		
	}
	

}
