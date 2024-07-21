package com.github.supermarket.telegram.command.bot;

import lombok.Getter;

@Getter
public enum BaseCommand {
    BASKET("basket", "Корзина покупок"),
    HELP("help", "Помощь");
//    ABOUT("about", "О программе");

    private String command;
    private String description;

    BaseCommand(String command, String description) {
        this.command = command;
        this.description = description;
    }
}
