package alphaVantage.regression;

import org.apache.commons.math3.stat.regression.SimpleRegression;

/**
 * Interface which all the linear regression calculator classes implements. The interface contains
 * methods which can be applied generally for linear regressions to be made.
 *
 * @author Olivia Svensson, Joar Eliasson
 */
public interface LinearRegressions {

  void fetchAdjustedData(String fiscalDateEnding);

  void reverseVariableData();

  long[] getVariableData(IncomeStatementVariable variable);

  double[] getIndexedVariableData(long[] variableData);

  SimpleRegression getRegression(String variableName, double[] indexedVariableData);

  String fetchDescription(SimpleRegression regression, String variableName);

  String fetchPrediction(double independentVariable, SimpleRegression regression);

  RegressionResult runRegression(IncomeStatementVariable variable);
}
