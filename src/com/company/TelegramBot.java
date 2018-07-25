package com.company;


import com.company.forEnum.IndexMenu;
import com.company.managers.ExpensesManager;
import com.company.managers.RevenuesManager;
import com.company.models.Expenses;
import com.company.models.Revenues;
import com.company.uiutils.ButtonTelegram;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class TelegramBot extends TelegramLongPollingBot {
    private static final String API = "610932395:AAGwebnLeAgvj_n6ev7kOwYhzuo653NQ2X4";
    private static final String newname = "BigGuru";

    private ExpensesManager expensesManager = new ExpensesManager();
    private RevenuesManager revenuesManager = new RevenuesManager();

  //  private int indexMenu = 0;
    private int indexRemoveE = 0;
    private int indexAddE = 0;
    private int indexAddR = 0;
    private int indexRemoveR = 0;
    private String categoryAddE = " ";
    private String nameAddE = " ";
    private String manyAddE = " ";
    private String dateAddE = " ";
    private String nameRemoveE = " ";
    private String manyRemoveE = " ";
    private String dateRemoveE = " ";
    IndexMenu indexMenuEnum;


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
            indexAddE = 1;
        } else if (callback.equals("viewE")) {
            String value = "Витрати";
            for (Expenses u : expensesManager.getExpenses()) {
                String category = u.getCategory();
                String name = u.getName();
                String many = u.getMoney() + "";
                String date = u.getDate();
                value = value + "\n" + "Категорія - " + category + "; " + "Назва  - " + name + "; " + "Ціна - " + many + "грн; " + "Дата - " + date + ".";
            }
            sendMessage.setText(value);
        } else if (callback.equals("removeE")) {
            indexRemoveE = 1;
            sendMessage.setText("Напишіть назву витрати, яку ви хочете видалити");
        } else if (callback.equals("addR")) {  // доходи меню
            sendMessage.setText("Напишіть людину, яка принесла дохід");
            indexAddR = 1;
        } else if (callback.equals("viewR")) {
            String value = "Доходи";
            for (Revenues u : revenuesManager.getRevenues()) {
                String name = u.getName();
                String type = u.getType();
                String many = u.getMoney() + "";
                String date = u.getDate();
                value = value + "\n" + "Назва  - " + name + "; " + "Тип" + type + "; " + "Ціна - " + many + "грн; " + "Дата - " + date + ".";

            }
            sendMessage.setText(value);
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

        if (indexAddE == 1 || indexAddR == 1) {
            Add(text, chatId, sendMessage);
        } else if (indexRemoveE == 1 || indexRemoveR == 1) {
            Remove(text, chatId, sendMessage);
        } else {
            sendMessage.setText("неправильна команда, для початку напишіть /start");
            sendMessage.setChatId(chatId);
            ;
        }

        if (text.contains("/start") || !(indexMenuEnum == null)) { //виклик головного меню


        switch (indexMenuEnum)
        {
            case Start:
                System.out.println("Working ...");
                indexMenuEnum = null;
                break;
            case Added:
                sendMessage.setText("Додано");
                sendMessage.setChatId(chatId);
                indexMenuEnum = null;
                break;
            case RemovedGood:
                sendMessage.setText("Видалено");
                sendMessage.setChatId(chatId);
                indexMenuEnum = null;
                break;
            case RemovedBad:
                sendMessage.setText("Я нічого не знайшов");
                sendMessage.setChatId(chatId);
                indexMenuEnum = null;
                break;
            case iDontFaund:
                sendMessage.setText("неправильна команда");
               sendMessage.setChatId(chatId);
                indexMenuEnum = null;
                break;
        }

//            if (indexMenu == 1 ) {
//                sendMessage.setText("Додано");
//                sendMessage.setChatId(chatId);
//                indexMenu = 0;
//            } else if (indexMenu == 2) {
//                sendMessage.setText("Видалено");
//                sendMessage.setChatId(chatId);
//                indexMenu = 0;
//            } else if (indexMenu == 3) {
//                sendMessage.setText("Я нічого не знайшов");
//                sendMessage.setChatId(chatId);
//                indexMenu = 0;
//            } else if (indexMenu == 4) {
//                sendMessage.setText("неправильна команда");
//                sendMessage.setChatId(chatId);
//                indexMenu = 0;
//            } else {
//                sendMessage.setText("Привіт");
//                sendMessage.setChatId(chatId);
//            }
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

        if (indexAddE == 1) {
            // add expenses
            categoryAddE = text;
            sendMessage.setText("Напишіть назву витрати");
            indexAddE = 2;
        } else if (indexAddE == 2) {
            nameAddE = text;
            sendMessage.setText("Напишіть суму, через крапку");
            indexAddE = 3;
        } else if (indexAddE == 3) {
            manyAddE = text;
            sendMessage.setText("Напишіть дату, через крапку, у форматі дд.мм.рррр");
            indexAddE = 4;
        } else if (indexAddE == 4) {
            dateAddE = text;
            expensesManager.getExpenses().add(new Expenses(categoryAddE, nameAddE, manyAddE, dateAddE));
            sendMessage.setText("Додано");
            indexAddE = 0;
            indexMenuEnum = IndexMenu.Added;
            //indexMenu = 1;
        } else if (indexAddR == 1) {
            // add revenues
            nameAddE = text;
            sendMessage.setText("Напишіть тип доходу");
            indexAddR = 2;
        } else if (indexAddR == 2) {
            categoryAddE = text;
            sendMessage.setText("Напишіть суму, через крапку");
            indexAddR = 3;
        } else if (indexAddR == 3) {
            manyAddE = text;
            sendMessage.setText("Напишіть дату, через крапку, у форматі дд.мм.рррр");
            indexAddR = 4;
        } else if (indexAddR == 4) {
            dateAddE = text;
            revenuesManager.getRevenues().add(new Revenues(nameAddE, categoryAddE, manyAddE, dateAddE));
            sendMessage.setText("Додано");
            indexAddR = 0;


           // indexMenu = 1;
            indexMenuEnum = IndexMenu.Added;
        }
        sendMessage.setChatId(chatId);
    }

    private void Remove(String text, long chatId, SendMessage sendMessage) {
        if (indexRemoveE == 1) {
            // delete expenses
            nameRemoveE = text;
            sendMessage.setText("Напишіть вартість витрати, яку ви хочете видалити");
            sendMessage.setChatId(chatId);

           // indexRemoveE = 2;
        } else if (indexRemoveE == 2) {
            manyRemoveE = text;
            sendMessage.setText("Напишіть дату, коли сталася покупка");
            sendMessage.setChatId(chatId);
            indexRemoveE = 3;
        } else if (indexRemoveE == 3) {
            dateRemoveE = text;
            for (int i = 0; i < expensesManager.getExpenses().size(); i++) {
                if (expensesManager.getExpenses().get(i).getName().equalsIgnoreCase(nameRemoveE)) {
                    if (expensesManager.getExpenses().get(i).getMoney().equalsIgnoreCase(manyRemoveE)) {
                        if (expensesManager.getExpenses().get(i).getDate().equalsIgnoreCase(dateRemoveE)) {
                            expensesManager.getExpenses().remove(i);
                            sendMessage.setText("Видалено");
                            sendMessage.setChatId(chatId);
                            //indexMenu = 2;
                            indexMenuEnum = IndexMenu.RemovedGood;
                            break;
                        }
                    }
                }
                if (i == expensesManager.getExpenses().size() - 1) {
                    sendMessage.setText("Я нічого не знайшов");
                    sendMessage.setChatId(chatId);
                  //  indexMenu = 3;
                    indexMenuEnum = IndexMenu.RemovedBad;
                }
            }
            indexRemoveE = 0;
        } else if (indexRemoveR == 1) {
            // delete revenes
            nameRemoveE = text;
            sendMessage.setText("Напишіть вартість витрати, яку ви хочете видалити");
            sendMessage.setChatId(chatId);
            indexRemoveR = 2;
        } else if (indexRemoveR == 2) {
            manyRemoveE = text;
            sendMessage.setText("Напишіть дату, коли сталася покупка");
            sendMessage.setChatId(chatId);
            indexRemoveR = 3;
        } else if (indexRemoveR == 3) {
            dateRemoveE = text;
            for (int i = 0; i < revenuesManager.getRevenues().size(); i++) {
                if (revenuesManager.getRevenues().get(i).getName().equalsIgnoreCase(nameRemoveE)) {
                    if (revenuesManager.getRevenues().get(i).getMoney().equalsIgnoreCase(manyRemoveE)) {
                        if (revenuesManager.getRevenues().get(i).getDate().equalsIgnoreCase(dateRemoveE)) {
                            revenuesManager.getRevenues().remove(i);
                            sendMessage.setText("Видалено");
                            sendMessage.setChatId(chatId);
                            //indexMenu = 2;
                            indexMenuEnum = IndexMenu.RemovedGood;
                            break;
                        }
                    }
                }
                if (i == revenuesManager.getRevenues().size() - 1) {
                    sendMessage.setText("Я нічого не знайшов");
                    sendMessage.setChatId(chatId);
                   // indexMenu = 3;
                    indexMenuEnum = IndexMenu.RemovedBad;
                }
            }
            indexRemoveR = 0;
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
