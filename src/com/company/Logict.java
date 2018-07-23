package com.company;


import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Logict extends TelegramLongPollingBot {
    private static final String APII = "610932395:AAGwebnLeAgvj_n6ev7kOwYhzuo653NQ2X4";
    private static final String newname = "BigGuru";
    ListForAll listForAll = new ListForAll();
    int indexMenu = 0;
    int indexRemoveE = 0;
    int indexAddE = 0;
    int indexAddR = 0;
    int indexRemoveR = 0;
    String categoryAddE = " ";
    String nameAddE = " ";
    String manyAddE = " ";
    String dateAddE = " ";
    String nameRemoveE = " ";
    String manyRemoveE = " ";
    String dateRemoveE = " ";


    @Override
    public void onUpdateReceived(Update update) {

        SendMessage SendMessage = new SendMessage();
        ButtonTelegram NewBotton = new ButtonTelegram();

        if (update.hasMessage()) {
            String text = update.getMessage().getText();
            Long chaill = update.getMessage().getChatId();

            if (indexAddE == 1) {              // add expenses
                categoryAddE = text;
                SendMessage.setText("Напишіть назву витрати");
                SendMessage.setChatId(chaill);
                indexAddE = 2;
            } else if (indexAddE == 2) {
                nameAddE = text;
                SendMessage.setText("Напишіть суму, через крапку");
                SendMessage.setChatId(chaill);
                indexAddE = 3;
            } else if (indexAddE == 3) {
                manyAddE = text;
                SendMessage.setText("Напишіть дату, через крапку, у форматі дд.мм.рррр");
                SendMessage.setChatId(chaill);
                indexAddE = 4;
            } else if (indexAddE == 4) {
                dateAddE = text;
                listForAll.expenses.add(new Expenses(categoryAddE, nameAddE, manyAddE, dateAddE));
                SendMessage.setText("Додано");
                SendMessage.setChatId(chaill);
                indexAddE = 0;
                indexMenu = 1;
            }else if (indexAddR == 1) {              // add revenues
                nameAddE = text;
                SendMessage.setText("Напишіть тип доходу");
                SendMessage.setChatId(chaill);
                indexAddR = 2;
            } else if (indexAddR == 2) {
                categoryAddE = text;
                SendMessage.setText("Напишіть суму, через крапку");
                SendMessage.setChatId(chaill);
                indexAddR = 3;
            } else if (indexAddR == 3) {
                manyAddE = text;
                SendMessage.setText("Напишіть дату, через крапку, у форматі дд.мм.рррр");
                SendMessage.setChatId(chaill);
                indexAddR = 4;
            } else if (indexAddR == 4) {
                dateAddE = text;
                listForAll.revenues.add(new Revenues(nameAddE, categoryAddE, manyAddE, dateAddE));
                SendMessage.setText("Додано");
                SendMessage.setChatId(chaill);
                indexAddR = 0;
                indexMenu = 1;
            } else if (indexRemoveE == 1) // delete expenses
            {
                nameRemoveE = text;
                SendMessage.setText("Напишіть вартість витрати, яку ви хочете видалити");
                SendMessage.setChatId(chaill);
                indexRemoveE = 2;
            } else if (indexRemoveE == 2) {
                manyRemoveE = text;
                SendMessage.setText("Напишіть дату, коли сталася покупка");
                SendMessage.setChatId(chaill);
                indexRemoveE = 3;
            } else if (indexRemoveE == 3) {
                dateRemoveE = text;
                for (int i = 0; i < listForAll.expenses.size(); i++) {
                    if (listForAll.expenses.get(i).name.equalsIgnoreCase(nameRemoveE)) {
                        if (listForAll.expenses.get(i).many.equalsIgnoreCase(manyRemoveE)) {
                            if (listForAll.expenses.get(i).date.equalsIgnoreCase(dateRemoveE)) {
                                listForAll.expenses.remove(i);
                                SendMessage.setText("Видалено");
                                SendMessage.setChatId(chaill);
                                indexMenu = 2;
                                break;
                            }
                        }
                    }
                    if (i == listForAll.expenses.size() - 1) {
                        SendMessage.setText("Я нічого не знайшов");
                        SendMessage.setChatId(chaill);
                        indexMenu = 3;
                    }
                }
                indexRemoveE = 0;
            } else if (indexRemoveR == 1) // delete revenes
            {
                nameRemoveE = text;
                SendMessage.setText("Напишіть вартість витрати, яку ви хочете видалити");
                SendMessage.setChatId(chaill);
                indexRemoveR = 2;
            } else if (indexRemoveR == 2) {
                manyRemoveE = text;
                SendMessage.setText("Напишіть дату, коли сталася покупка");
                SendMessage.setChatId(chaill);
                indexRemoveR = 3;
            } else if (indexRemoveR == 3) {
                dateRemoveE = text;
                for (int i = 0; i < listForAll.revenues.size(); i++) {
                    if (listForAll.revenues.get(i).name.equalsIgnoreCase(nameRemoveE)) {
                        if (listForAll.revenues.get(i).many.equalsIgnoreCase(manyRemoveE)) {
                            if (listForAll.revenues.get(i).date.equalsIgnoreCase(dateRemoveE)) {
                                listForAll.revenues.remove(i);
                                SendMessage.setText("Видалено");
                                SendMessage.setChatId(chaill);
                                indexMenu = 2;
                                break;
                            }
                        }
                    }
                    if (i == listForAll.revenues.size() - 1) {
                        SendMessage.setText("Я нічого не знайшов");
                        SendMessage.setChatId(chaill);
                        indexMenu = 3;
                    }
                }
                indexRemoveR = 0;
            } else {
                SendMessage.setText("неправильна команда, для початку напишіть /start");
                SendMessage.setChatId(chaill);
            }

            if (text.contains("/start") || indexMenu == 1 || indexMenu == 2 || indexMenu == 3) { //виклик головного меню
                if (indexMenu == 1) {
                    SendMessage.setText("Додано");
                    SendMessage.setChatId(chaill);
                    indexMenu = 0;
                } else if (indexMenu == 2) {
                    SendMessage.setText("Видалено");
                    SendMessage.setChatId(chaill);
                    indexMenu = 0;
                } else if (indexMenu == 3) {
                    SendMessage.setText("Я нічого не знайшов");
                    SendMessage.setChatId(chaill);
                    indexMenu = 0;
                } else {
                    SendMessage.setText("Привіт");
                    SendMessage.setChatId(chaill);
                }
                InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                inlineKeyboardMarkup.setKeyboard(NewBotton.Bottonnew("Витрати", "expenses", "Доходи", "revenues", "Баланс", "balance"));
                SendMessage.setReplyMarkup(inlineKeyboardMarkup);
            }


        } else if (update.hasCallbackQuery()) {

            String callbeck = update.getCallbackQuery().getData(); // зміній присвоюємо значення яке присилає клавіша при натисненні
            SendMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());//
            if (callbeck.equals("expenses")) {             //витрати
                InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                inlineKeyboardMarkup.setKeyboard(NewBotton.Bottonnew("Додати", "addE", "Переглянути", "viewE", "Забрати", "removeE"));
                SendMessage.setText("Витрати");
                SendMessage.setReplyMarkup(inlineKeyboardMarkup);

            } else if (callbeck.equals("revenues")) {   // доходи
                InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                inlineKeyboardMarkup.setKeyboard(NewBotton.Bottonnew("Додати", "addR", "Переглянути", "viewR", "Видалити", "removeR"));
                SendMessage.setText("Доходи");
                SendMessage.setReplyMarkup(inlineKeyboardMarkup);

            } else if (callbeck.equals("addE")) { //витрати меню
                SendMessage.setText("Напишіть категорію витрат");
                indexAddE = 1;
            } else if (callbeck.equals("viewE")) {
                String value = "Витрати";
                for (Expenses u : listForAll.expenses) {
                    String category = u.category;
                    String name = u.name;
                    String many = u.many + "";
                    String date = u.date;
                    value = value + "\n" + "Категорія - " + category + "; " + "Назва  - " + name + "; " + "Ціна - " + many + "грн; " + "Дата - " + date + ".";
                }
                SendMessage.setText(value);
            } else if (callbeck.equals("removeE")) {
                indexRemoveE = 1;
                SendMessage.setText("Напишіть назву витрати, яку ви хочете видалити");
            } else if (callbeck.equals("addR")) {  // доходи меню
                SendMessage.setText("Напишіть людину, яка принесла дохід");
                indexAddR = 1;
            } else if (callbeck.equals("viewR")) {
                String value = "Доходи";
                for (Revenues u : listForAll.revenues) {
                    String name = u.name;
                    String type = u.type;
                    String many = u.many + "";
                    String date = u.date;
                    value = value + "\n" + "Назва  - " + name + "; " + "Тип" + type + "; " + "Ціна - " + many + "грн; " + "Дата - " + date + ".";
                }
                SendMessage.setText(value);
            } else if (callbeck.equals("removeR")) {
                indexRemoveR = 1;
                SendMessage.setText("Напишіть назву імя людини, чий дохід ви хочете видалити");
            }
        }

        try {
            execute(SendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return newname;
    }

    @Override
    public String getBotToken() {
        return APII;
    }
}
