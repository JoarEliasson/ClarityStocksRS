package alphaVantage.regression;

import alphaVantage.controller.AlphaVantageClient;
import alphaVantage.model.data.fundamental.IncomeStatement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *  Class for making the analysis of the linear regression. The linear regression requires some more
 *  analysis which makes having a separate class for these more comprehensible.
 * @author Olivia Svensson
 * */

public class LinearRegressionAnalysis {
  private LinearRegression[] linearRegressions;
  private IncomeStatement incomeStatement;
  private AlphaVantageClient alphaVantageClient;
  private String symbol;
  private String rSquareDescription = "";
  private ArrayList<Long> incomeStatementsList;
  private int numberOfIncomeStatements;
  private List<String> results = new ArrayList<>();

  public LinearRegressionAnalysis(IncomeStatement incomeStatement, String symbol, AlphaVantageClient alphaVantageClient){
    this.incomeStatement = incomeStatement;
    this.symbol = symbol;
    this.alphaVantageClient = alphaVantageClient;
    numberOfIncomeStatements = 24;
    incomeStatementsList = new ArrayList<Long>(numberOfIncomeStatements);

    //amount of statements in IncomeStatement class which are able to run the analysis on
    linearRegressions = new LinearRegression[numberOfIncomeStatements];
  }

  /**
   * Method which populates the list of income statements with income statements from the stock company.
   * The arraylist indexes are ordered in the same way that the variables in the class IncomeStatement are declared.
   * */
  private void populateIncomeStatementList() {
    incomeStatementsList.set(0,incomeStatement.getGrossProfit());
    incomeStatementsList.set(1,incomeStatement.getTotalRevenue());
    incomeStatementsList.set(2, incomeStatement.getCostOfRevenue());
    incomeStatementsList.set(3, incomeStatement.getCostofGoodsAndServicesSold());
    incomeStatementsList.set(4, incomeStatement.getOperatingIncome());
    incomeStatementsList.set(5, incomeStatement.getSellingGeneralAndAdministrative());
    incomeStatementsList.set(6, incomeStatement.getResearchAndDevelopment());
    incomeStatementsList.set(7, incomeStatement.getOperatingExpenses());
    incomeStatementsList.set(8, incomeStatement.getInvestmentIncomeNet());
    incomeStatementsList.set(9, incomeStatement.getInterestIncome());
    incomeStatementsList.set(10, incomeStatement.getInterestExpense());
    incomeStatementsList.set(11, incomeStatement.getNonInterestIncome());
    incomeStatementsList.set(12, incomeStatement.getOtherNonOperatingIncome());
    incomeStatementsList.set(13, incomeStatement.getDepreciation());
    incomeStatementsList.set(14, incomeStatement.getDepreciationAndAmortization());
    incomeStatementsList.set(15, incomeStatement.getIncomeBeforeTax());
    incomeStatementsList.set(16, incomeStatement.getIncomeTaxExpense());
    incomeStatementsList.set(17, incomeStatement.getInterestAndDebtExpense());
    incomeStatementsList.set(18, incomeStatement.getNetIncomeFromContinuingOperations());
    incomeStatementsList.set(19, incomeStatement.getEbit());
    incomeStatementsList.set(20, incomeStatement.getEbitda());
    incomeStatementsList.set(21, incomeStatement.getNetIncome());
  }

  /**
   * Method for getting the array of linear regressions made of all the income statements.
   * */
  public LinearRegression[] getLinearRegressions() {
    return this.linearRegressions;
  }


  /**
   * Method for making all the linear regressions based on the income statements.
   * The method has a for-loop which loops through all the elements of the linear regression array. For each element
   * the method assigns the element in the array to a new linear regression made.
   * */
  private void makeLinearRegressions() {
    for(int i = 0; i < linearRegressions.length; i++) {
      linearRegressions[i] = new LinearRegression(symbol,alphaVantageClient, incomeStatementsList.get(i));
    }
  }

  /**
   * Method which sorts the linear regressions array to an ascending order based on the regressions r-square value.
   * The r-square value is an indicator to show which income statements have the highest correlation with the price of
   * the stock.
   * */
  private String sortByRSquare() {
    /*
    double[] rSquareArray = new double[linearRegressions.length];
    for(int i = 0; i < linearRegressions.length; i++) {
      rSquareArray[i] = linearRegressions[i].getRSquare();
    }
    //sorts in ascending order
    Arrays.sort(rSquareArray);
  //sort array into descending order
    int count = 0;
    double[] rSquareArrayDesc = new double[linearRegressions.length];
    for(int i = rSquareArray.length; i > 0; i--) {
      rSquareArrayDesc[count] = rSquareArray[i];
      count++;
    }

     */

    //bubble sort algorithm
    int n = linearRegressions.length;
    double temp = 0.0;
    for(int i = 0; i < n; i++) {
      for(int j = 1; j < (n - i); j++) {
        if(linearRegressions[j-1].getRSquare() > linearRegressions[j].getRSquare()) {
          temp = linearRegressions[j-1].getRSquare();
          linearRegressions[j-1] = linearRegressions[j];
          linearRegressions[j].setRSquare(temp);
        }
      }
    }

    return rSquareDescription = "The highest r-square values are: \n" + linearRegressions[0].getRSquare() + " from " +
       linearRegressions[0] +  ". \n" +
       linearRegressions[1].getRSquare() + " from " + linearRegressions[1] +  ". \n" +
      linearRegressions[2].getRSquare() + " from " + linearRegressions[2] +  ".";


  }


}
