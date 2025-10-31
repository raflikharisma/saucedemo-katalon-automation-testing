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

WebUI.waitForElementVisible(findTestObject('Page_Inventory/dropdown_sorting'), 10)
WebUI.waitForElementClickable(findTestObject('Page_Inventory/dropdown_sorting'), 10)
WebUI.selectOptionByValue(findTestObject('Page_Inventory/dropdown_sorting'), 'az', false)
WebUI.delay(1)


TestObject nameObj = new TestObject().addProperty('css', ConditionType.EQUALS, '.inventory_item_name')
List<WebElement> items = WebUiCommonHelper.findWebElements(nameObj, 10)
List<String> actual = items.collect { it.getText().trim() }


List<String> expected = new ArrayList<>(actual)
expected.sort(String.CASE_INSENSITIVE_ORDER)
assert actual == expected : "Sorting A→Z salah.\nActual: ${actual}\nExpected: ${expected}"

WebUI.comment("✅ Sorting A→Z OK: ${actual}")

WebUI.click(findTestObject('Page_Inventory/burger_button'))
WebUI.waitForElementClickable(findTestObject('Page_Inventory/logout_button'), 5)
WebUI.click(findTestObject('Page_Inventory/logout_button'))
WebUI.waitForElementVisible(findTestObject('Page_Login/input_username'), 10)
WebUI.closeBrowser()
