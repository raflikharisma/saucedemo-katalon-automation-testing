import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.webui.common.WebUiCommonHelper
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import org.openqa.selenium.WebElement
import internal.GlobalVariable as GlobalVariable


WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.baseURL)
WebUI.waitForElementVisible(findTestObject('Page_Login/input_username'), 10)
WebUI.setText(findTestObject('Page_Login/input_username'), GlobalVariable.username)
WebUI.setEncryptedText(findTestObject('Page_Login/input_password'), GlobalVariable.password)
WebUI.click(findTestObject('Page_Login/login_button'))

WebUI.waitForElementVisible(findTestObject('Page_Inventory/product_sauce-labs-backpack'), 10)
WebUI.waitForElementClickable(findTestObject('Page_Inventory/product_sauce-labs-backpack'), 10)

String selectedProductName = WebUI.getText(findTestObject('Page_Inventory/product_sauce-labs-backpack'))
WebUI.comment("🛒 Produk yang diklik: ${selectedProductName}")

WebUI.click(findTestObject('Page_Inventory/product_sauce-labs-backpack'))
WebUI.waitForElementVisible(findTestObject('Page_Cart/link_backtoproducts'), 10)
WebUI.verifyMatch(WebUI.getUrl(), '.*/inventory-item\\.html(\\?.*)?$', true)
WebUI.comment('✅ Redirect ke halaman product detail berhasil')


String detailProductName = WebUI.getText(findTestObject('Page_Inventory/header_product_detail'))
WebUI.verifyMatch(detailProductName, selectedProductName, false)
WebUI.comment("✅ Nama produk sesuai: ${detailProductName}")


WebUI.verifyElementPresent(findTestObject('Page_Inventory/header_product_detail'), 5)
WebUI.comment('✅ Halaman detail lengkap & siap digunakan')

WebUI.click(findTestObject('Page_Cart/link_backtoproducts'))
WebUI.waitForElementVisible(findTestObject('Page_Inventory/burger_button'), 10)
WebUI.click(findTestObject('Page_Inventory/burger_button'))
WebUI.waitForElementClickable(findTestObject('Page_Inventory/logout_button'), 5)
WebUI.click(findTestObject('Page_Inventory/logout_button'))
WebUI.waitForElementVisible(findTestObject('Page_Login/input_username'), 10)
WebUI.closeBrowser()
