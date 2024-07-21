package com.github.supermarket.telegram.command.bot;

import com.github.supermarket.common.domain.ListResponse;
import com.github.supermarket.service.catalog.CatalogService;
import com.github.supermarket.service.catalog.model.Catalog;
import com.github.supermarket.service.catalog.model.CatalogFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.commands.DeleteMyCommands;
import org.telegram.telegrambots.meta.api.methods.commands.GetMyCommands;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScope;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
public class SupermarketBotCommand {
    private static Logger logger = LoggerFactory.getLogger(SupermarketBotCommand.class);

    private final CatalogService catalogService;

    public SupermarketBotCommand(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    public void init(AbsSender sender, Long storeId) {
        ListResponse<Catalog> catalogResponse = catalogService.getByParams(CatalogFilter.builder().storeId(storeId).build());

        List<BotCommand> commands = new ArrayList<>();
        for (Catalog catalog : catalogResponse.getItems()) {
            commands.add(BotCommand.builder()
                    .command(catalog.getSysname().toLowerCase())
                    .description(catalog.getName())
                    .build()
            );
        }
        for (BaseCommand baseCommand : BaseCommand.values()) {
            commands.add(BotCommand.builder()
                    .command(baseCommand.getCommand())
                    .description(baseCommand.getDescription())
                    .build()
            );
        }
//        commands.add(BotCommand.builder()
//                .command("storecart")
//                .description("Корзина покупок")
//                .build()
//        );
//        commands.add(BotCommand.builder()
//                .command("about")
//                .description("О программе")
//                .build()
//        );

        try {
//            List<org.telegram.telegrambots.meta.api.objects.commands.BotCommand> commands =
//                    Arrays.stream(Command.values())
//                            .map(c -> org.telegram.telegrambots.meta.api.objects.commands.BotCommand.builder().command(c.getName()).description(c.getDesc()).build())
//                            .collect(Collectors.toList());

//            ArrayList<BotCommand> botCommands = sender.execute(GetMyCommands.builder().build());
//            logger.debug("GetMyCommands {}", botCommands);
//
//            Boolean aBoolean = sender.execute(DeleteMyCommands.builder().build());
//            logger.debug("DeleteMyCommands {}", aBoolean);

            Boolean aBoolean1 = sender.execute(SetMyCommands.builder().commands(commands).build());
            logger.debug("SetMyCommands {}", aBoolean1);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

}
