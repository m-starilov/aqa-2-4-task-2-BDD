package ru.netology.web.page;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.FindBy;
import ru.alfabank.alfatest.cucumber.annotations.Name;
import ru.alfabank.alfatest.cucumber.api.AkitaPage;
import ru.netology.web.data.DataHelper;

public class ReplenishPage extends AkitaPage {
    @Name("Поле суммы")
    @FindBy(css = "[data-test-id=amount] input")
    public SelenideElement amountField;
    @Name("Поле откуда")
    @FindBy(css = "[data-test-id=from] input")
    public SelenideElement fromField;
    @Name("Кнопка пополнить")
    @FindBy(css = "[data-test-id=action-transfer]")
    public SelenideElement replenishButton;
    private final String deleteString = Keys.chord(Keys.CONTROL, "a") + Keys.DELETE;

    public DashboardPage makeTransaction(DataHelper.DataForTransaction dataForTransaction) {
        amountField.setValue(deleteString).setValue(dataForTransaction.getAmount());
        fromField.setValue(deleteString).setValue(dataForTransaction.getAccountFrom());
        replenishButton.click();
        return Selenide.page(DashboardPage.class);
    }

}
