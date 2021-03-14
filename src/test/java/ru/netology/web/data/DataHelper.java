package ru.netology.web.data;

import lombok.Getter;
import lombok.Setter;
import lombok.Value;

public class DataHelper {
    private DataHelper() {
    }

    @Getter
    @Setter
    private static int actualFirstAccountBalance;

    @Getter
    @Setter
    private static int actualSecondAccountBalance;

    @Value
    public static class AuthInfo {
        String login;
        String password;
    }

    public static AuthInfo getAuthInfo() {
        return new AuthInfo("vasya", "qwerty123");
    }

    @Value
    public static class VerificationCode {
        String code;
    }

    public static VerificationCode getVerificationCodeFor(AuthInfo authInfo) {
        return new VerificationCode("12345");
    }

    @Value
    public static class DataForTransaction {
        String accountFrom;
        String amount;
    }

    public static DataForTransaction dataForRestoreAccounts(Integer restoreAmount) {
        String accountFrom;
        String amount;
        if (restoreAmount > 0) {
            accountFrom = "5559 0000 0000 0001";
        }
        else {
            restoreAmount = Math.abs(restoreAmount);
            accountFrom = "5559 0000 0000 0002";
        }
        amount = Integer.toString(restoreAmount);
        return new DataForTransaction(accountFrom, amount);
    }

    public static DataForTransaction dataForTransfer(String accountFrom, Integer amount) {
        return new DataForTransaction(accountFrom, Integer.toString(amount));
    }

    public static Integer getRestoreAmount() {
        return (actualFirstAccountBalance - actualSecondAccountBalance) / 2;
    }
}
