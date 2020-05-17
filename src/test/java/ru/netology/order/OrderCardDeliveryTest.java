package ru.netology.order;

import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.generator.DataGenerator;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class OrderCardDeliveryTest {
    DataGenerator dataGenerator = new DataGenerator();
    SelenideElement form = $("form[class='form form_size_m form_theme_alfa-on-white']");

    @BeforeEach
    void Setup() {
        open("http://localhost:9999");
    }

    @Test
    void shouldSubmitRequest() {
        String name = dataGenerator.makeName();
        String phone = dataGenerator.makePhone();
        String city = dataGenerator.makeCity();

        form.$("[placeholder='Город']").setValue(city);
        form.$("[placeholder='Дата встречи']").doubleClick().sendKeys(dataGenerator.forwardDate(3));
        form.$("[name=name]").setValue(name);
        form.$("[name=phone]").setValue(phone);
        form.$(".checkbox__box").click();
        $(".button__text").click();
        $(withText("Успешно")).shouldBe(visible);

        open("http://localhost:9999");
        form.$("[placeholder='Город']").setValue(city);
        form.$("[placeholder='Дата встречи']").doubleClick().sendKeys(dataGenerator.forwardDate(4));
        form.$("[name=name]").setValue(name);
        form.$("[name=phone]").setValue(phone);
        form.$(".checkbox__box").click();
        $(".button__text").click();
        $(withText("У вас уже запланирована встреча на другую дату. Перепланировать?")).shouldBe(visible);
        $("[data-test-id=replan-notification] button.button").click();
        $(withText("Успешно")).shouldBe(visible);
    }

    @Test
    void shoulNotSubmitWithoutCity() {
        $("[placeholder='Дата встречи']").doubleClick().sendKeys(dataGenerator.forwardDate(3));
        $("[name=name]").setValue(dataGenerator.makeName());
        $("[name=phone]").setValue(dataGenerator.makePhone());
        $(".checkbox__box").click();
        $(".button__text").click();
        $(".input_theme_alfa-on-white.input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldNotSubmitWithoutName() {
        $("[placeholder='Город']").setValue(dataGenerator.makeCity());
        $("[placeholder='Дата встречи']").doubleClick().sendKeys(dataGenerator.forwardDate(3));
        $("[name=phone]").setValue(dataGenerator.makePhone());
        $(".checkbox__box").click();
        $(".button__text").click();
        $(".input_theme_alfa-on-white.input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldNotSumbitWithIncorrectName() {
        SelenideElement selenideElement = form.$("[placeholder='Город']").setValue(dataGenerator.makeCity());
        $("[placeholder='Дата встречи']").doubleClick().sendKeys(dataGenerator.forwardDate(3));
        $("[name=name]").setValue("Vasiliy Ivanov");
        $("[name=phone]").setValue(dataGenerator.makePhone());
        $(".checkbox__box").click();
        $(".button__text").click();
        $(".input_theme_alfa-on-white.input_invalid .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldNotSubmitWithoutPhone() {
        $("[placeholder='Город']").setValue(dataGenerator.makeCity());
        $("[placeholder='Дата встречи']").doubleClick().sendKeys(dataGenerator.forwardDate(3));
        $("[name=name]").setValue(dataGenerator.makeName());
        $(".checkbox__box").click();
        $(".button__text").click();
        $(".input_theme_alfa-on-white.input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldNotSubmitWithoutCheckbox() {
        $("[placeholder='Город']").setValue(dataGenerator.makeCity());
        $("[placeholder='Дата встречи']").doubleClick().sendKeys(dataGenerator.forwardDate(3));
        $("[name=name]").setValue(dataGenerator.makeName());
        $("[name=phone]").setValue(dataGenerator.makePhone());
        $(".button__text").click();
        $(".input_invalid").shouldHave(exactText("Я соглашаюсь с условиями обработки и использования моих персональных данных"));
    }
}

