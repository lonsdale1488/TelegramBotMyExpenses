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

    private int indexRemoveE = 0;
    private int indexRemoveR = 0;
    private String categoryAdd = " ";
    private String nameAdd = " ";
    private String moneyAdd = " ";
    private String dateAdd = " ";
    private String nameRemoveE = " ";
    private String manyRemoveE = " ";
    private String dateRemoveE = " ";
    IndexMenu indexMenu;
    IndexAddExpenses indexAddExpenses;
    IndexAddRevenues indexAddRevenues;
    private ExecutorService executorService = Executors.newCachedThreadPool();

//    public void onUpdateReceived(Update update) {
//        if (update.hasMessage() && update.getMessage().hasText()) {
//            Message message = update.getMessage();
//            if (message.getText().equals("/start") || message.getText().equals("/start@" + getBotUsername())) {
//                executorService.submit(() -> {
//                    startUser(update.getMessage());
//                });
//            } else if (message.getText().equals("/cancel") || message.getText().equals("/cancel@" + getBotUsername())) {
//                executorService.submit(() -> cancelOperation(message));
//            } else if (message.getText().equals(ADD_NEW_SERIAL)) {
//                executorService.submit(() -> startAddNewSerial(message));
//            } else if (message.getText().equals(LIST_SERIALS)) {
//                executorService.submit(() -> showSerialCategories(message));
//            } else {
//                executorService.submit(() -> checkUserStep(message));
//            }
//        } else if (update.hasCallbackQuery()) {
//            executorService.submit(() -> checkUserCallback(update.getCallbackQuery()));
//        }
//    }
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
            sendMessage.setText("Напишіть категорію витрат");
            indexAddExpenses = IndexAddExpenses.AddCategory;
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
            indexRemoveE = 1;
            sendMessage.setText("Напишіть назву витрати, яку ви хочете видалити");
        } else if (callback.equals("addR")) {  // доходи меню
            sendMessage.setText("Напишіть людину, яка принесла дохід");
            indexAddRevenues = IndexAddRevenues.AddName;
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
            indexRemoveR = 1;
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

        if (!(indexAddExpenses == null) || !(indexAddRevenues == null)) {
            Add(text, chatId, sendMessage);
            sendMessage.setChatId(chatId);
        } else if (indexRemoveR == 1 || indexRemoveE == 1) {
            Remove(text, sendMessage);
            sendMessage.setChatId(chatId);
        } else if (text.equalsIgnoreCase("/start"))
        {indexMenu = IndexMenu.Start;}
        else { indexMenu = IndexMenu.iDontFaund;
        }
        if (!(indexMenu == null) ) { //виклик головного меню
            if (!(indexMenu == null)) {
                switch (indexMenu) {
                    case Start:
                        sendMessage.setText("Привіт");
                        sendMessage.setChatId(chatId);
                        indexMenu = null;
                        break;
                    case Added:
                        sendMessage.setText("Додано");
                        sendMessage.setChatId(chatId);
                        indexMenu = null;
                        break;
                    case RemovedGood:
                        sendMessage.setText("Видалено");
                        sendMessage.setChatId(chatId);
                        indexMenu = null;
                        break;
                    case RemovedBad:
                        sendMessage.setText("Я нічого не знайшов");
                        sendMessage.setChatId(chatId);
                        indexMenu = null;
                        break;
                    case iDontFaund:
                        sendMessage.setText("неправильна команда");
                        sendMessage.setChatId(chatId);
                        indexMenu = null;
                        break;
                }
            }
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            inlineKeyboardMarkup.setKeyboard(ButtonTelegram.Bottonnew("Витрати", "expenses", "Доходи", "revenues", "Баланс", "balance"));
            sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        }

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void Add(String text, long chatId, SendMessage sendMessage) {

        if (!(indexAddExpenses == null)) {
            switch (indexAddExpenses) {
                case AddCategory:
                    categoryAdd = text;
                    sendMessage.setText("Напишіть назву витрати");
                    indexAddExpenses = IndexAddExpenses.AddName;
                    break;
                case AddName:
                    nameAdd = text;
                    sendMessage.setText("Напишіть суму, через крапку");
                    indexAddExpenses = IndexAddExpenses.AddCost;
                    break;
                case AddCost:
                    if (Utils.isNumeric(text)) {
                        moneyAdd = text;
                        sendMessage.setText("Напишіть дату, через крапку, у форматі дд.мм.рррр");
                        indexAddExpenses = IndexAddExpenses.AddDate;
                        break;
                    } else {
                        sendMessage.setText("неправильно ведена сума. Напишіть суму, через крапку");
                        indexAddExpenses = IndexAddExpenses.AddCost;
                        break;
                    }
                case AddDate:
                    if (Utils.isDate(text)) {
                        dateAdd = text;
                        ExpensesManager.getExpenses().add(new Expenses(categoryAdd, nameAdd, moneyAdd, dateAdd));
                        indexMenu = IndexMenu.Added;
                        indexAddExpenses = null;
                        sendMessage.setText("Додано");
                        break;
                    } else {
                        sendMessage.setText("Неправильно ведена дата. Напишіть дату, через крапку, у форматі дд.мм.рррр");
                        indexAddExpenses = IndexAddExpenses.AddDate;
                        break;
                    }

            }
        }
        if (!(indexAddRevenues == null)) {
            switch (indexAddRevenues) {
                case AddName:
                    nameAdd = text;
                    sendMessage.setText("Напишіть тип доходу");
                    indexAddRevenues = IndexAddRevenues.AddCategory;
                    break;
                case AddCategory:
                    categoryAdd = text;
                    sendMessage.setText("Напишіть суму, через кому");
                    indexAddRevenues = IndexAddRevenues.AddCost;
                    break;
                case AddCost:
                    if (Utils.isNumeric(text)) {
                        moneyAdd = text;
                        sendMessage.setText("Напишіть дату, через крапку, у форматі дд.мм.рррр");
                        indexAddRevenues = IndexAddRevenues.AddDate;
                        break;
                    } else {
                        sendMessage.setText("Неправильно введена сума. Напишіть суму, через крапку");
                        indexAddRevenues = IndexAddRevenues.AddCost;
                        break;
                    }
                case AddDate:
                    if (Utils.isDate(text)) {
                        dateAdd = text;
                        RevenuesManager.getRevenues().add(new Revenues(nameAdd, categoryAdd, moneyAdd, dateAdd));
                        sendMessage.setText("Додано");
                        indexAddRevenues = null;
                        indexMenu = IndexMenu.Added;
                        break;
                    } else {
                        sendMessage.setText("Напишіть дату, через крапку, у форматі дд.мм.рррр");
                        indexAddRevenues = IndexAddRevenues.AddDate;
                        break;
                    }
            }
        }

    }

    private void Remove(String text, SendMessage sendMessage) {
        if (indexRemoveE == 1) {
            // delete expenses
            nameRemoveE = text;
            sendMessage.setText("Напишіть вартість витрати, яку ви хочете видалити");
            indexRemoveE = 2;
        } else if (indexRemoveE == 2) {
            manyRemoveE = text;
            sendMessage.setText("Напишіть дату, коли сталася покупка");
            indexRemoveE = 3;
        } else if (indexRemoveE == 3) {
            RemoveInBaseE(sendMessage, text);
        } else if (indexRemoveR == 1) {
            // delete revenes
            nameRemoveE = text;
            sendMessage.setText("Напишіть вартість витрати, яку ви хочете видалити");
            indexRemoveR = 2;
        } else if (indexRemoveR == 2) {
            manyRemoveE = text;
            sendMessage.setText("Напишіть дату, коли сталася покупка");
            indexRemoveR = 3;
        } else if (indexRemoveR == 3) {
            RemoveInBaseR(sendMessage, text);
        }
    }

    private void RemoveInBaseE(SendMessage sendMessage, String text) {
        dateRemoveE = text;
        for (int i = 0; i < ExpensesManager.getExpenses().size(); i++) {
            if (ExpensesManager.getExpenses().get(i).getName().equalsIgnoreCase(nameRemoveE)) {
                if (ExpensesManager.getExpenses().get(i).getMoney().equalsIgnoreCase(manyRemoveE)) {
                    if (ExpensesManager.getExpenses().get(i).getDate().equalsIgnoreCase(dateRemoveE)) {
                        ExpensesManager.getExpenses().remove(i);
                        sendMessage.setText("Видалено");
                        indexMenu = IndexMenu.RemovedGood;
                        break;
                    }
                }
            }
            if (i == ExpensesManager.getExpenses().size() - 1) {
                sendMessage.setText("Я нічого не знайшов");
                indexMenu = IndexMenu.RemovedBad;
            }
        }
        indexRemoveE = 0;
    }

    private void RemoveInBaseR(SendMessage sendMessage, String text) {
        dateRemoveE = text;
        for (int i = 0; i < RevenuesManager.getRevenues().size(); i++) {
            if (RevenuesManager.getRevenues().get(i).getName().equalsIgnoreCase(nameRemoveE)) {
                if (RevenuesManager.getRevenues().get(i).getMoney().equalsIgnoreCase(manyRemoveE)) {
                    if (RevenuesManager.getRevenues().get(i).getDate().equalsIgnoreCase(dateRemoveE)) {
                        RevenuesManager.getRevenues().remove(i);
                        sendMessage.setText("Видалено");
                        indexMenu = IndexMenu.RemovedGood;
                        break;
                    }
                }
            }
            if (i == RevenuesManager.getRevenues().size() - 1) {
                sendMessage.setText("Я нічого не знайшов");
                // indexMenu = 3;
                indexMenu = IndexMenu.RemovedBad;
            }
        }
        indexRemoveR = 0;
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
