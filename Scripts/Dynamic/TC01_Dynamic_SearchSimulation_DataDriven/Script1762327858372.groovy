import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.webui.common.WebUiCommonHelper
import org.openqa.selenium.WebElement
import internal.GlobalVariable as GlobalVariable

// -------- Test Data Driven Variables --------
// kamu ambil dari Data Files di Test Suite
String keyword = searchkeyword
boolean expectedFound = expectedfound.toString().toBoolean()


WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.baseURL)

// LOGIN CEPAT
WebUI.waitForElementVisible(findTestObject('Page_Login/input_username'), 10)
WebUI.setText(findTestObject('Page_Login/input_username'), GlobalVariable.username)
WebUI.setEncryptedText(findTestObject('Page_Login/input_password'), GlobalVariable.password)
WebUI.click(findTestObject('Page_Login/login_button'))
WebUI.waitForElementVisible(findTestObject('Page_Inventory/dropdown_sorting'), 10)

// Ambil semua nama produk
TestObject nameObj = new TestObject().addProperty('css', ConditionType.EQUALS, '.inventory_item_name')
List<WebElement> elements = WebUiCommonHelper.findWebElements(nameObj, 10)
List<String> productNames = elements.collect { it.getText().toLowerCase() }

WebUI.comment("🔍 Searching for keyword: '${keyword}' ...")

// Filter produk yang mengandung keyword
List<String> matches = productNames.findAll { it.contains(keyword.toLowerCase()) }

boolean found = matches.size() > 0

if (found) {
    WebUI.comment("✅ Produk ditemukan: ${matches}")
} else {
    WebUI.comment("❌ Tidak ada produk cocok untuk '${keyword}'")
}

// Verifikasi sesuai data
assert found == expectedFound :
    "❌ Expected found=${expectedFound}, tapi hasilnya found=${found} untuk keyword '${keyword}'"

WebUI.comment("✅ Test data '${keyword}' -> hasil sesuai ekspektasi (${expectedFound})")

WebUI.closeBrowser()
