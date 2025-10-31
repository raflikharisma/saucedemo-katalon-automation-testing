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


WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.baseURL)
WebUI.waitForElementVisible(findTestObject('Page_Login/input_username'), 10)
WebUI.setText(findTestObject('Page_Login/input_username'), GlobalVariable.username)
WebUI.setEncryptedText(findTestObject('Page_Login/input_password'), GlobalVariable.password)
WebUI.click(findTestObject('Page_Login/login_button'))
WebUI.waitForElementVisible(findTestObject('Page_Inventory/dropdown_sorting'), 10)
WebUI.click(findTestObject('Page_Inventory/button_add-to-cart-sauce-labs-backpack'))
WebUI.click(findTestObject('Page_Inventory/button_add-to-cart-sauce-labs-onesie'))
String badge = WebUI.getText(findTestObject('Page_Inventory/cart_badge'))
WebUI.verifyMatch(badge, '2', false)
WebUI.comment("✅ 2 item berhasil ditambahkan ke cart (badge = ${badge})")
WebUI.click(findTestObject('Page_Inventory/burger_button'))
WebUI.waitForElementClickable(findTestObject('Page_Inventory/logout_button'), 5)
WebUI.click(findTestObject('Page_Inventory/logout_button'))
WebUI.waitForElementVisible(findTestObject('Page_Login/input_username'), 10)
WebUI.closeBrowser()

