package org.example.users;

public interface IProfitable {

    int dailyBalance(int income, int expense);
    int totalBalance(int totalIncome, int totalExpense);
    int calculateIncome(int id, int companyKey);
    int calculateExpense(int id, int companyKey);
    int calculateTotalIncome(int companyKey);
    int calculateTotalExpense(int companyKey);
}
