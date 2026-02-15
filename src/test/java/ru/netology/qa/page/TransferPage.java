package ru.netology.qa.page;

import com.codeborne.selenide.SelenideElement;
import ru.netology.qa.data.DataHelper;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class TransferPage {
    private SelenideElement amountInput = $("[data-test-id='amount'] input");
    private final SelenideElement fromInput = $("[data-test-id='from'] input");
    private final SelenideElement transferButton = $("[data-test-id='action-transfer']");
    private final SelenideElement header = $("[data-test-id='dashboard']");

    public TransferPage() {
        header.shouldBe(visible);
    }

    public DashboardPage makeTransfer(String amount, DataHelper.CardInfo cardInfo) {
        amountInput.setValue(amount);
        fromInput.setValue(cardInfo.getNumber());
        transferButton.click();
        return new DashboardPage();
    }
}
