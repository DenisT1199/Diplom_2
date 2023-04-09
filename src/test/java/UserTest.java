import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.UserSteps;

import static constants.RandomData.*;

public class UserTest {

    private UserSteps userSteps;
    private String accessToken;

    @Before
    public void setUp() {
        userSteps = new UserSteps();
    }

    @Test
    @DisplayName("Успешное создание пользователя")
    @Description("Если введены правильные учетные данные, успешный запрос возвращает токен доступа.")
    public void createUniqueUserSuccess() {
        ValidatableResponse responseCreate = userSteps.createUser(RANDOM_EMAIL, RANDOM_PASS, RANDOM_NAME);
        userSteps.checkAnswerSuccess(responseCreate);
        accessToken = userSteps.getAccessToken(responseCreate);
    }

    @Test
    @DisplayName("Создайте пользователя, который уже зарегистрирован")
    @Description("Создание уже зарегистрированного пользователя и проверка ответа")
    public void createDuplicationUserForbidden() {
        ValidatableResponse responseCreate = userSteps.createUser(RANDOM_EMAIL, RANDOM_PASS, RANDOM_NAME);
        accessToken = userSteps.getAccessToken(responseCreate);
        ValidatableResponse responseIdentical = userSteps.createUser(RANDOM_EMAIL, RANDOM_PASS, RANDOM_NAME);
        userSteps.checkAnswerAlreadyExist(responseIdentical);
    }

    @Test
    @DisplayName("Создание пользователя без электронной почты")
    @Description("Создание пользователя без электронной почты и проверка ответа")
    public void createUserWithoutEmailForbidden() {
        ValidatableResponse responseCreate = userSteps.createUser("", RANDOM_PASS, RANDOM_NAME);
        userSteps.checkAnswerForbidden(responseCreate);
    }

    @Test
    @DisplayName("Создание пользователя без пароля")
    @Description("Создание пользователя без пароля и проверка ответа")
    public void createUserWithoutPasswordForbidden() {
        ValidatableResponse responseCreate = userSteps.createUser(RANDOM_EMAIL, "", RANDOM_NAME);
        userSteps.checkAnswerForbidden(responseCreate);
    }

    @Test
    @DisplayName("Создание пользователя без имени")
    @Description("Создание пользователя без имени и проверка ответа")
    public void createUserWithoutNameForbidden() {
        ValidatableResponse responseCreate = userSteps.createUser(RANDOM_EMAIL, RANDOM_PASS, "");
        userSteps.checkAnswerForbidden(responseCreate);
    }

    @After
    public void close() {
        userSteps.deletingUsersAfterTests(accessToken);
    }

}
