package com.company.managers;

import com.company.models.Expenses;

import java.util.ArrayList;
import java.util.List;

public class ExpensesManager {
    private List<Expenses> expenses = new ArrayList<>();

    public List<Expenses> getExpenses() {
        return expenses;
    }

}
