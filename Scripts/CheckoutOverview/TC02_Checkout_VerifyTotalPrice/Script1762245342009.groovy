import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.webui.common.WebUiCommonHelper
import org.openqa.selenium.WebElement
import java.math.BigDecimal
import java.math.RoundingMode


WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.baseURL)

// LOGIN
WebUI.waitForElementVisible(findTestObject('Page_Login/input_username'), 10)
WebUI.setText(findTestObject('Page_Login/input_username'), GlobalVariable.username)
WebUI.setEncryptedText(findTestObject('Page_Login/input_password'), GlobalVariable.password)
WebUI.click(findTestObject('Page_Login/login_button'))
WebUI.waitForElementVisible(findTestObject('Page_Inventory/dropdown_sorting'), 10)

// ADD TO CART
WebUI.click(findTestObject('Page_Inventory/button_add-to-cart-sauce-labs-backpack'))
WebUI.click(findTestObject('Page_Inventory/button_add-to-cart-sauce-labs-bike-light'))
WebUI.waitForElementVisible(findTestObject('Page_Inventory/cart_badge'), 5)
String badge = WebUI.getText(findTestObject('Page_Inventory/cart_badge'))
WebUI.verifyMatch(badge, '2', false)
WebUI.comment("✅ 2 item berhasil ditambahkan ke cart (badge = ${badge})")

// NAVIGATE TO CART
WebUI.click(findTestObject('Page_Inventory/cart_badge'))
TestObject cartNamesObj = new TestObject().addProperty('css', ConditionType.EQUALS, '.cart_item .inventory_item_name')
List<WebElement> cartNameEls = WebUiCommonHelper.findWebElements(cartNamesObj, 10)
List<String> cartNames = cartNameEls.collect { it.getText().trim() }
WebUI.comment("🛒 Items in Cart: ${cartNames}")
assert cartNames.size() == 2 : "Jumlah item di cart bukan 2. Actual: ${cartNames.size()}"

WebUI.verifyElementPresent(findTestObject('Page_Cart/item_Sauce Labs Backpack'), 5)
WebUI.verifyElementPresent(findTestObject('Page_Cart/item_Sauce Labs Bike Light'), 5)
WebUI.waitForElementClickable(findTestObject('Object Repository/Page_Cart/button_checkout'), 5)

// NAVIGATE TO CHECKOUT
WebUI.click(findTestObject('Object Repository/Page_Cart/button_checkout'))
WebUI.verifyElementPresent(findTestObject('Page_Checkout/input_checkout_first-name'), 3)
WebUI.setText(findTestObject('Page_Checkout/input_checkout_first-name'), 'Rafli')
WebUI.setText(findTestObject('Page_Checkout/input_checkout_last-name'), 'Kharisma')
WebUI.setText(findTestObject('Page_Checkout/input_checkout_postal-code'), '73144')    
WebUI.waitForElementClickable(findTestObject('Page_Checkout/button_checkout_continue'), 3) 
WebUI.click(findTestObject('Page_Checkout/button_checkout_continue'))
WebUI.verifyMatch(WebUI.getUrl(),".*/checkout-step-two.html", true)


// 1) Bandingkan nama item: Cart vs Overview
TestObject overviewNamesObj = new TestObject().addProperty('css', ConditionType.EQUALS, '.cart_item .inventory_item_name')
List<WebElement> overviewNameEls = WebUiCommonHelper.findWebElements(overviewNamesObj, 10)
List<String> overviewNames = overviewNameEls.collect { it.getText().trim() }

assert overviewNames == cartNames : "❌ Item mismatch.\nCart: ${cartNames}\nOverview: ${overviewNames}"
WebUI.comment("✅ Items OK. Cart vs Overview sama: ${overviewNames}")

// 2) Verifikasi perhitungan harga (Subtotal, Tax, Total)
def money = { String s -> new BigDecimal(s.replaceAll('[^0-9\\.]','')).setScale(2, RoundingMode.HALF_UP) }

// ambil semua harga item yang ditampilkan di Overview
TestObject priceObj = new TestObject().addProperty('css', ConditionType.EQUALS, '.cart_item .inventory_item_price')
List<WebElement> priceEls = WebUiCommonHelper.findWebElements(priceObj, 10)
assert priceEls.size() > 0 : 'Tidak ada item price di Checkout Overview'

List<BigDecimal> prices = priceEls.collect { money(it.getText()) }
BigDecimal subtotal = prices.inject(BigDecimal.ZERO) { acc, p -> acc.add(p) }.setScale(2, RoundingMode.HALF_UP)

// ambil label subtotal/tax/total dari ringkasan
String itemTotalText = WebUI.getText(findTestObject('Page_CheckoutOverview/item_subtotal_price'))  
String taxText       = WebUI.getText(findTestObject('Page_CheckoutOverview/item_tax'))        
String totalText     = WebUI.getText(findTestObject('Page_CheckoutOverview/item_total_price'))      

BigDecimal itemTotal = money(itemTotalText)
BigDecimal tax       = money(taxText)
BigDecimal total     = money(totalText)

// assertions
assert subtotal.compareTo(itemTotal) == 0 :
		"❌ Item total tidak sesuai.\nSubtotal dihitung: ${subtotal}\nLabel item total: ${itemTotal}\nPrices: ${prices}"

assert itemTotal.add(tax).compareTo(total) == 0 :
		"❌ Total bukan penjumlahan.\nItem total: ${itemTotal} + Tax: ${tax} ≠ Total label: ${total}"

WebUI.comment("✅ Totals OK — Subtotal ${subtotal} | Tax ${tax} | Total ${total} | Items ${prices}")


WebUI.closeBrowser()