package ru.netology.web.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import lombok.val;
import org.openqa.selenium.support.FindBy;
import ru.alfabank.alfatest.cucumber.annotations.Name;
import ru.alfabank.alfatest.cucumber.api.AkitaPage;
import ru.netology.web.data.DataHelper;

@Name("Страница счетов")
public class DashboardPage extends AkitaPage {
    @Name("Заголовок")
    @FindBy(css = "[data-test-id=dashboard]")
    public SelenideElement heading;
    @Name("Список карт")
    @FindBy(css = "li.list__item")
    public ElementsCollection accountList;
    @Name("Список кнопок")
    @FindBy(css = "[data-test-id=action-deposit]")
    public ElementsCollection accountButtons;

    public void takeAllActualBalanceFromPage() {
        if (accountList.size() > 1) {
            val actual1st = Integer.parseInt(accountList.get(0).getText().split(" ")[5]);
            val actual2nd = Integer.parseInt(accountList.get(1).getText().split(" ")[5]);
            DataHelper.setActualFirstAccountBalance(actual1st);
            DataHelper.setActualSecondAccountBalance(actual2nd);
        }
    }

    public Integer takeActualBalanceFromPage(Integer accountNumber) {
        return Integer.parseInt(accountList.get(accountNumber - 1).getText().split(" ")[5]);
    }

    public AkitaPage replenishAccount(Integer accountNumber) {
        accountButtons.get(accountNumber - 1).click();
        return Selenide.page(ReplenishPage.class);
    }
}
