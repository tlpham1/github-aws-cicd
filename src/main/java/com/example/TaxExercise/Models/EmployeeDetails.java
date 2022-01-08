package com.example.TaxExercise.Models;

import com.opencsv.bean.CsvBindByName;
import java.lang.Math;
public class EmployeeDetails {

    @CsvBindByName(column = "Name")
    private String strName;
    @CsvBindByName(column = "Surname")
    private String strSurname;
    @CsvBindByName(column = "AnnualSalary")
    private int intAnnualSalary;
    @CsvBindByName(column = "SuperRate")
    private double fltSuperRate;
    @CsvBindByName(column = "PaymentStartDate")
    private String strPaymentStartDate;
    @CsvBindByName
    private String strPayPeriod;
    @CsvBindByName
    private int intGrossIncome;
    @CsvBindByName
    private int intIncomeTax;
    @CsvBindByName
    private int intNetIncome;
    @CsvBindByName
    private int intSuper;


    public EmployeeDetails(){
        strName = "";
        strSurname = "";
        intAnnualSalary = 0;
        fltSuperRate = 0.0;
        strPaymentStartDate = "";
    }

    public EmployeeDetails(String newName, String newSurname, int newAnnualSalary, double newSuperRate, String newPaymentStartDate){

        setStrName(newName);
        setStrSurname(newSurname);
        setIntAnnualSalary(newAnnualSalary);
        setFltSuperRate(newSuperRate);
        setStrPaymentStartDate(newPaymentStartDate);

    }

    public void GeneratePay(){
        setStrPayPeriod(getStrPaymentStartDate());
        setIntGrossIncome(getIntAnnualSalary());
        setIntIncomeTax(getIntAnnualSalary());
        setIntNetIncome();
        setIntSuper();

    }

    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

    public String getStrSurname() {
        return strSurname;
    }

    public void setStrSurname(String strSurname) {
        this.strSurname = strSurname;
    }

    public int getIntAnnualSalary() {
        return intAnnualSalary;
    }

    public void setIntAnnualSalary(int intAnnualSalary) {
        this.intAnnualSalary = intAnnualSalary;
    }

    public double getFltSuperRate() {
        return fltSuperRate;
    }

    public void setFltSuperRate(double fltSuperRate) {
        this.fltSuperRate = fltSuperRate;
    }

    public String getStrPaymentStartDate() {
        return strPaymentStartDate;
    }

    public void setStrPaymentStartDate(String strPaymentStartDate) {
        this.strPaymentStartDate = strPaymentStartDate;
    }

    public String getStrPayPeriod() {
        return strPayPeriod;
    }

    public void setStrPayPeriod(String strPayPeriod) {
        this.strPayPeriod = strPayPeriod;
    }

    public int getIntGrossIncome() {
        return intGrossIncome;
    }

    public void setIntGrossIncome(int intGrossIncome) { // Note: Round down
        int newVal = (int)Math.floor((intGrossIncome/ 12));
        this.intGrossIncome = newVal;
    }

    public int getIntIncomeTax() {
        return intIncomeTax;
    }
/*
For example, the payment in March for an employee with an annual salary of $60,050 and a super rate of 9% is:

pay period = Month of March (01 March to 31 March)
gross income = 60,050 / 12 = 5,004.16666667 (round down) = 5,004
income tax = (3,572 + (60,050 - 37,000) x 0.325) / 12 = 921.9375 (round up) = 922
net income = 5,004 - 922 = 4,082
super = 5,004 x 9% = 450.36 (round down) = 450

 */
    public void setIntIncomeTax(int intIncomeTax) { // Note: Round up
        int intNewVal = 0;
        int intCurrentTax = 0;
        double fltExtraTax = 0;
        int intMaxTax = 0;

        if(intIncomeTax <= 18200){
            intCurrentTax = 0;
            fltExtraTax = 0;
            intMaxTax = 0;
        }
        else if(intIncomeTax > 18200 && intIncomeTax <= 37000){
            intCurrentTax = 0;
            fltExtraTax = 0.19;
            intMaxTax = 18200;
        }
        else if(intIncomeTax > 37000 && intIncomeTax<= 87000){
            intCurrentTax = 3572;
            fltExtraTax = 0.325;
            intMaxTax = 37000;
        }
        else if(intIncomeTax > 87000 && intIncomeTax <= 180000){
            intCurrentTax = 19822;
            fltExtraTax = 0.37;
            intMaxTax = 87000;
        }
        else {
            intCurrentTax = 54232;
            fltExtraTax = 0.45;
            intMaxTax = 180000;
        }

        intNewVal = (int)Math.ceil(((intCurrentTax + ( intIncomeTax - intMaxTax) * fltExtraTax) / 12));
        this.intIncomeTax = intNewVal;
    }

    public int getIntNetIncome() {
        return intNetIncome;
    }

    public void setIntNetIncome() {
        int newVal = getIntGrossIncome() - getIntIncomeTax();
        this.intNetIncome = newVal;
    }

    public int getIntSuper() {
        return intSuper;
    }

    public void setIntSuper() { //Round Down

        double fltRate = getFltSuperRate();
        int newVal = (int)Math.floor((getIntGrossIncome() * fltRate));
        this.intSuper = newVal;
    }

}
