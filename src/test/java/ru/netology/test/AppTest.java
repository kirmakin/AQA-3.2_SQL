package ru.netology.test;

import lombok.val;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;

import ru.netology.page.LoginPage;
import ru.netology.data.DbHelper;

import java.sql.SQLException;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DbHelper.getStatusFromDb;

public class AppTest {
    DbHelper mySql = new DbHelper();

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    @Test
    void shouldCheckLogin() {
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getValidAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DbHelper.getVerificationCode(authInfo.getLogin());
        val dashboardPage = verificationPage.verify(verificationCode);
        dashboardPage.checkIfVisible();
    }

    @Test
    void shouldCheckIfBlockedAfter3LoginWithInvalidPassword() {
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfoWithInvalidPassword();
        loginPage.validLogin(authInfo);
        loginPage.cleanLoginFields();
        loginPage.validLogin(authInfo);
        loginPage.cleanLoginFields();
        loginPage.validLogin(authInfo);
        val statusSQL = getStatusFromDb(authInfo.getLogin());
        assertEquals("blocked", statusSQL);
    }

    @AfterAll
    static void close() {
        DbHelper.cleanDb();
    }
}
