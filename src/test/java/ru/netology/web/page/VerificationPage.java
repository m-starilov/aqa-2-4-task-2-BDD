package ru.netology.web.page;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.FindBy;
import ru.alfabank.alfatest.cucumber.annotations.Name;
import ru.alfabank.alfatest.cucumber.api.AkitaPage;
import ru.netology.web.data.DataHelper;

@Name("Подтверждение входа")
public class VerificationPage extends AkitaPage {
    @Name("Код")
    @FindBy(css = "[data-test-id=code] input")
    private SelenideElement codeField;
    @Name("Продолжить")
    @FindBy(css = "[data-test-id=action-verify]")
    private SelenideElement verifyButton;

    public void validVerify(DataHelper.VerificationCode verificationCode) {
        codeField.setValue(verificationCode.getCode());
        verifyButton.click();
    }
}
