package com.company;


import com.company.forEnum.IndexAddExpenses;
import com.company.forEnum.IndexAddRevenues;
import com.company.forEnum.IndexMenu;
import com.company.managers.ExpensesManager;
import com.company.managers.RevenuesManager;
import com.company.models.Expenses;
import com.company.models.Revenues;
import com.company.uiutils.ButtonTelegram;
import com.company.utils.Utils;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TelegramBot extends TelegramLongPollingBot {
    private static final String API = "610932395:AAGwebnLeAgvj_n6ev7kOwYhzuo653NQ2X4";
    private static final String newname = "BigGuru";

//    private ExpensesManager expensesManager = ExpensesManager.ExpensesManagerSingl();
//    private RevenuesManager revenuesManager = RevenuesManager.RevenuesManagerSingl();


    private String categoryAdd = " ";
    private String nameAdd = " ";
    private String moneyAdd = " ";
    private String dateAdd = " ";
    private String nameRemoveE = " ";
    private String manyRemoveE = " ";
    private String dateRemoveE = " ";
    private String finishAction = "";
    IndexMenu indexMenu;

    private ExecutorService executorService = Executors.newCachedThreadPool();

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            processMessage(update.getMessage());

        } else if (update.hasCallbackQuery()) {
            processCallBack(update.getCallbackQuery());

        }
    }

    private void processCallBack(CallbackQuery callbackQuery) {
        String callback = callbackQuery.getData(); // зміній присвоюємо значення яке присилає клавіша при натисненні
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(callbackQuery.getMessage().getChatId());//
        if (callback.equals("expenses")) {
            // витрати
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            inlineKeyboardMarkup.setKeyboard(ButtonTelegram.Bottonnew("Додати", "addE", "Переглянути", "viewE", "Забрати", "removeE"));
            sendMessage.setText("Витрати");
            sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        } else if (callback.equals("revenues")) {
            // доходи
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            inlineKeyboardMarkup.setKeyboard(ButtonTelegram.Bottonnew("Додати", "addR", "Переглянути", "viewR", "Видалити", "removeR"));
            sendMessage.setText("Доходи");
            sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        } else if (callback.equals("addE")) { //витрати меню
            indexMenu = IndexMenu.AddCategoryExpeses;
            sendMessage.setText("Напишіть категорію витрат");
        } else if (callback.equals("viewE")) {
            String value = "Витрати";
            for (Expenses u : ExpensesManager.getExpenses()) {
                String category = u.getCategory();
                String name = u.getName();
                String many = u.getMoney() + "";
                String date = u.getDate();
                value = value + "\n" + "Категорія - " + category + "; " + "Назва  - " + name + "; " + "Ціна - " + many + "грн; " + "Дата - " + date + ".";

            }
            sendMessage.setText(value);
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            inlineKeyboardMarkup.setKeyboard(ButtonTelegram.Bottonnew("Витрати", "expenses", "Доходи", "revenues", "Баланс", "balance"));
            sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        } else if (callback.equals("removeE")) {
            indexMenu = IndexMenu.RemoveExpensesName;
            sendMessage.setText("Напишіть назву витрати, яку ви хочете видалити");
        } else if (callback.equals("addR")) {  // доходи меню
            indexMenu = IndexMenu.AddCategoryRevenues;
            sendMessage.setText("Напишіть людину, яка принесла дохід");

        } else if (callback.equals("viewR")) {
            String value = "Доходи";
            for (Revenues u : RevenuesManager.getRevenues()) {
                String name = u.getName();
                String type = u.getType();
                String many = u.getMoney() + "";
                String date = u.getDate();
                value = value + "\n" + "Назва  - " + name + "; " + "Тип" + type + "; " + "Ціна - " + many + "грн; " + "Дата - " + date + ".";
            }
            sendMessage.setText(value);
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            inlineKeyboardMarkup.setKeyboard(ButtonTelegram.Bottonnew("Витрати", "expenses", "Доходи", "revenues", "Баланс", "balance"));
            sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        } else if (callback.equals("removeR")) {
            indexMenu = IndexMenu.RemoveRevenuesName;
            sendMessage.setText("Напишіть назву імя людини, чий дохід ви хочете видалити");
        } else if (callback.equals("balance")) {
            sendMessage.setText("Balance!");
        }

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * Processes text message from user
     *
     * @param message {@link Message} message received from user
     */
    private void processMessage(Message message) {
        String text = message.getText();
        Long chatId = message.getChatId();
        SendMessage sendMessage = new SendMessage();
// chenge

        if (text.equalsIgnoreCase("/start")) {
            indexMenu = IndexMenu.Start;
            finishAction = "Привіт. ";
            menu(text, chatId, sendMessage);
        } else if (!(indexMenu == null)) {
            menu(text, chatId, sendMessage);
            sendMessage.setChatId(chatId);
        } else {
            indexMenu = IndexMenu.iDontFaund;
            menu(text, chatId, sendMessage);
        }
        if ((indexMenu == IndexMenu.Menu)) {
            menu(text, chatId, sendMessage);
            sendMessage.setChatId(chatId);
        }

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    private void menu(String text, long chatId, SendMessage sendMessage) {
        if (!(indexMenu == null)) {
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            switch (indexMenu) {
                case Menu:
                    sendMessage.setText(finishAction + "Оберіть категорію");
                    finishAction = " ";
                    sendMessage.setChatId(chatId);
                    inlineKeyboardMarkup.setKeyboard(ButtonTelegram.Bottonnew("Витрати", "expenses", "Доходи", "revenues", "Баланс", "balance"));
                    sendMessage.setReplyMarkup(inlineKeyboardMarkup);
                    break;
                case iDontFaund:
                    sendMessage.setText("неправильна команда");
                    sendMessage.setChatId(chatId);
                    sendMessage.setChatId(chatId);
                    inlineKeyboardMarkup.setKeyboard(ButtonTelegram.Bottonnew("Витрати", "expenses", "Доходи", "revenues", "Баланс", "balance"));
                    sendMessage.setReplyMarkup(inlineKeyboardMarkup);
                    break;
                case AddCategoryExpeses:
                    categoryAdd = text;
                    sendMessage.setText("Напишіть назву витрати");
                    indexMenu = IndexMenu.AddNameExpenses;
                    break;
                case AddNameExpenses:
                    nameAdd = text;
                    sendMessage.setText("Напишіть суму, через крапку");
                    indexMenu = IndexMenu.AddCostExpenses;
                    break;
                case AddCostExpenses:
                    if (Utils.isNumeric(text)) {
                        moneyAdd = text;
                        sendMessage.setText("Напишіть дату, через крапку, у форматі дд.мм.рррр");
                        indexMenu = IndexMenu.AddDateExpenses;
                        break;
                    } else {
                        sendMessage.setText("неправильно ведена сума. Напишіть суму, через крапку");
                        indexMenu = IndexMenu.AddCostExpenses;
                        break;
                    }
                case AddDateExpenses:
                    if (Utils.isDate(text)) {
                        dateAdd = text;
                        ExpensesManager.getExpenses().add(new Expenses(categoryAdd, nameAdd, moneyAdd, dateAdd));
                        sendMessage.setText("Додано");
                        finishAction = "Додано. ";
                        indexMenu = IndexMenu.Menu;
                        break;
                    } else {
                        sendMessage.setText("Неправильно ведена дата. Напишіть дату, через крапку, у форматі дд.мм.рррр");
                        indexMenu = IndexMenu.AddDateExpenses;
                        break;
                    }
                case AddCategoryRevenues:
                    categoryAdd = text;
                    sendMessage.setText("Напишіть тип доходу");
                    indexMenu = IndexMenu.AddNameRevenues;
                    break;
                case AddNameRevenues:
                    nameAdd = text;
                    sendMessage.setText("Напишіть суму, через кому");
                    indexMenu = IndexMenu.AddCostRevenues;
                    break;
                case AddCostRevenues:
                    if (Utils.isNumeric(text)) {
                        moneyAdd = text;
                        sendMessage.setText("Напишіть дату, через крапку, у форматі дд.мм.рррр");
                        indexMenu = IndexMenu.AddDateRevenues;
                        break;
                    } else {
                        sendMessage.setText("неправильно ведена сума. Напишіть суму, через крапку");
                        indexMenu = IndexMenu.AddCostRevenues;
                        break;
                    }
                case AddDateRevenues:
                    if (Utils.isDate(text)) {
                        dateAdd = text;
                        RevenuesManager.getRevenues().add(new Revenues(categoryAdd, nameAdd, moneyAdd, dateAdd));
                        sendMessage.setText("Додано");
                        finishAction = "Додано. ";
                        indexMenu = IndexMenu.Menu;
                        break;
                    } else {
                        sendMessage.setText("Неправильно ведена дата. Напишіть дату, через крапку, у форматі дд.мм.рррр");
                        indexMenu = IndexMenu.AddDateRevenues;
                        break;
                    }
                case RemoveExpensesName:
                    nameRemoveE = text;
                    sendMessage.setText("Напишіть вартість витрати, яку ви хочете видалити");
                    indexMenu = IndexMenu.RemoveExpesesCost;
                    break;
                case RemoveExpesesCost:
                    manyRemoveE = text;
                    sendMessage.setText("Напишіть дату, коли сталася покупка");
                    indexMenu = IndexMenu.RemoveExpesesDate;
                     break;
                case RemoveExpesesDate:
                    dateRemoveE = text;
                    for (int i = 0; i < ExpensesManager.getExpenses().size(); i++) {
                        if (ExpensesManager.getExpenses().get(i).getName().equalsIgnoreCase(nameRemoveE)) {
                            if (ExpensesManager.getExpenses().get(i).getMoney().equalsIgnoreCase(manyRemoveE)) {
                                if (ExpensesManager.getExpenses().get(i).getDate().equalsIgnoreCase(dateRemoveE)) {
                                    ExpensesManager.getExpenses().remove(i);
                                    sendMessage.setText("Видалено. ");
                                    indexMenu = IndexMenu.Menu;
                                    finishAction = "Видалено. ";
                                    break;
                                }
                            }
                        }
                        if (i == ExpensesManager.getExpenses().size() - 1) {
                            sendMessage.setText("Я нічого не знайшов");
                            finishAction = "Я нічого не знайшов";
                            indexMenu = IndexMenu.Menu;
                        }
                    }
                    break;
                case RemoveRevenuesName:
                    nameRemoveE = text;
                    sendMessage.setText("Напишіть вартість витрати, яку ви хочете видалити");
                    indexMenu = IndexMenu.RemoveRevenuesCost;
                    break;
                case RemoveRevenuesCost:
                    manyRemoveE = text;
                    sendMessage.setText("Напишіть дату, коли сталася покупка");
                    indexMenu = IndexMenu.RemoveRevenuesDate;
                    break;
                case RemoveRevenuesDate:
                    dateRemoveE = text;
                    for (int i = 0; i < RevenuesManager.getRevenues().size(); i++) {
                        if (RevenuesManager.getRevenues().get(i).getName().equalsIgnoreCase(nameRemoveE)) {
                            if (RevenuesManager.getRevenues().get(i).getMoney().equalsIgnoreCase(manyRemoveE)) {
                                if (RevenuesManager.getRevenues().get(i).getDate().equalsIgnoreCase(dateRemoveE)) {
                                    RevenuesManager.getRevenues().remove(i);
                                    indexMenu = IndexMenu.Menu;
                                    finishAction = "Видалено. ";
                                    break;
                                }
                            }
                        }
                        if (i == RevenuesManager.getRevenues().size() - 1) {
                            finishAction = "Я нічого не знайшов. ";
                            indexMenu = IndexMenu.Menu;
                        }
                    }
                    
                    break;
            }
        }
    }


    @Override
    public String getBotUsername() {
        return newname;
    }

    @Override
    public String getBotToken() {
        return API;
    }
}
