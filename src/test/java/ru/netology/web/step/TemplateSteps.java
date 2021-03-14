package ru.netology.web.step;

import cucumber.api.java.ru.Когда;
import cucumber.api.java.ru.Пусть;
import cucumber.api.java.ru.Тогда;
import lombok.val;
import ru.alfabank.alfatest.cucumber.api.AkitaPage;
import ru.alfabank.alfatest.cucumber.api.AkitaScenario;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;
import ru.netology.web.page.ReplenishPage;
import ru.netology.web.page.VerificationPage;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.page;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.alfabank.alfatest.cucumber.api.AkitaScenario.withPage;
import static ru.alfabank.tests.core.helpers.PropertyLoader.loadProperty;

public class TemplateSteps {
    private final AkitaScenario scenario = AkitaScenario.getInstance();

    @Пусть("^пользователь залогинен с именем \"([^\"]*)\" и паролем \"([^\"]*)\"$")
    public void loginWithNameAndPassword(String login, String password) {
        val loginUrl = loadProperty("loginUrl");
        open(loginUrl);

        withPage(LoginPage.class, AkitaPage::appeared);
        val authInfo = new DataHelper.AuthInfo(login, password);
        withPage(LoginPage.class, page -> page.validLogin(authInfo));

        val verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        withPage(VerificationPage.class, page -> page.validVerify(verificationCode));
        withPage(DashboardPage.class, AkitaPage::appeared);
    }

    @Когда("^он переводит \"([^\"]*)\" рублей с карты с номером \"([^\"]*)\" " +
            "на свою \"([^\"]*)\" карту с главной страницы")
    public void transactAmount(String amount, String accountFrom, Integer accountNumber) {
        scenario.setCurrentPage(page(DashboardPage.class));
        val dashboardPage = (DashboardPage) scenario.getCurrentPage().appeared();
        dashboardPage.takeAllActualBalanceFromPage();
        restoreAccounts(dashboardPage);
        scenario.setCurrentPage(dashboardPage.replenishAccount(accountNumber));

        val replenishPage = (ReplenishPage) scenario.getCurrentPage().appeared();
        val dataForTransaction = DataHelper
                .dataForTransfer(accountFrom, Integer.parseInt(amount.replaceAll("\\s+","")));
        scenario.setCurrentPage(replenishPage.makeTransaction(dataForTransaction));

        scenario.getCurrentPage().appeared();
    }

    @Тогда("^баланс его \"([^\"]*)\" карты из списка на главной странице должен стать \"([^\"]*)\" рублей")
    public void transactionVerification(Integer accountNumber, String expectedAmount) {
        scenario.setCurrentPage(page(DashboardPage.class));
        val dashboardPage = (DashboardPage) scenario.getCurrentPage().appeared();
        val expectedAmountInt = Integer.parseInt(expectedAmount.replaceAll("\\s+",""));
        assertEquals(expectedAmountInt, dashboardPage.takeActualBalanceFromPage(accountNumber));
    }

    void restoreAccounts(DashboardPage dashboardPage) {
        val restoreAmount = DataHelper.getRestoreAmount();
        if (restoreAmount != 0) {
            val dataForTransaction = DataHelper.dataForRestoreAccounts(restoreAmount);
            if (restoreAmount > 0)
                dashboardPage.replenishAccount(2);
            else dashboardPage.replenishAccount(1);

            scenario.setCurrentPage(page(ReplenishPage.class));
            val replenishPage = (ReplenishPage) scenario.getCurrentPage().appeared();
            scenario.setCurrentPage(replenishPage.makeTransaction(dataForTransaction));
            scenario.getCurrentPage().appeared();
            dashboardPage.takeAllActualBalanceFromPage();
        }
    }
}
