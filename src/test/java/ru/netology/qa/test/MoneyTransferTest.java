package ru.netology.qa.test;

import org.junit.jupiter.api.Test;
import ru.netology.qa.data.DataHelper;
import ru.netology.qa.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MoneyTransferTest {
    @Test
    void shouldTransferMoneyBetweenOwnCards() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCode();
        var dashboardPage = verificationPage.validVerify(verificationCode);
        DataHelper.CardInfo firstCardInfo = DataHelper.getFirstCardInfo();
        var secondCardInfo = DataHelper.getSecondCardInfo();
        int startBalanceFirst = dashboardPage.getCardBalance(firstCardInfo);
        int startBalanceSecond = dashboardPage.getCardBalance(secondCardInfo);
        var transferPage = dashboardPage.selectCard(firstCardInfo);
        int amount = DataHelper.generateValidAmount(startBalanceSecond);
        dashboardPage = transferPage.makeTransfer(String.valueOf(amount), secondCardInfo);
        assertEquals(startBalanceFirst + amount, dashboardPage.getCardBalance(firstCardInfo));
        assertEquals(startBalanceSecond - amount, dashboardPage.getCardBalance(secondCardInfo));
        assertTrue(dashboardPage.getCardBalance(secondCardInfo) >= 0,
                "Баланс карты списания ушел в минус: " + dashboardPage.getCardBalance(secondCardInfo));
    }

    @Test
    void shouldNotTransferAmountMoreThanBalance() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCode();
        var dashboardPage = verificationPage.validVerify(verificationCode);

        DataHelper.CardInfo firstCardInfo = DataHelper.getFirstCardInfo();
        var secondCardInfo = DataHelper.getSecondCardInfo();
        int startBalanceSecond = dashboardPage.getCardBalance(secondCardInfo);
        int startBalanceFirst = dashboardPage.getCardBalance(firstCardInfo);

        var transferPage = dashboardPage.selectCard(firstCardInfo);
        int amount = DataHelper.generateInvalidAmount(startBalanceSecond);
        dashboardPage = transferPage.makeTransfer(String.valueOf(amount), secondCardInfo);

        int actualBalanceSecond = dashboardPage.getCardBalance(secondCardInfo);
        int actualBalanceFirst = dashboardPage.getCardBalance(firstCardInfo);


        assertEquals(startBalanceSecond, actualBalanceSecond, "Баланс карты 2 изменился!");
        assertEquals(startBalanceFirst, actualBalanceFirst, "Баланс карты 1 изменился!");

    }


}
