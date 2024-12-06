package com.epicure.project.model.dto.response;

import lombok.Data;

@Data
public class IncomeReportResponse {

    private double dailyIncome;
    private double weeklyIncome;
    private double monthlyIncome;
    private double yearlyIncome;
}
