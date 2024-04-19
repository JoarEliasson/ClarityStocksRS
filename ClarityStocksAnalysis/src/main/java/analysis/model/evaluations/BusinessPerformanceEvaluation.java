package analysis.model.evaluations;

import analysis.model.interfaces.RatingEvaluation;

/**
 * Class for calculating the business performance of a company. Returns an EBITDA margin.
 * <p>
 * EBITDA margin is calculated by EBITDA divided by the companies total revenue.
 * <p>
 *   EBITDA stands for Earnings Before Interest, Taxes, Depreciation, and Amortization.
 *   The EBITDA margin is a measure of a company's operating profitability as a percentage of its
 *   total revenue.
 * </p>
 *
 * @author Olivia Svensson
 */
public class BusinessPerformanceEvaluation implements RatingEvaluation {

  private final String symbol;
  private final double EBITDA;
  private final double revenueTTM;
  private double EBITDAMargin;
  private int rating;
  private String description;

  public BusinessPerformanceEvaluation(String symbol, double EBITDA, double revenueTTM) {
    this.symbol = symbol;
    this.EBITDA = EBITDA;
    this.revenueTTM = revenueTTM;
    evaluate();
  }

  private void evaluateEBIDTAMargin() {
    EBITDAMargin = EBITDA / revenueTTM;
  }

  @Override
  public void evaluate() {
    evaluateEBIDTAMargin();
    if (EBITDAMargin > 0.20) {
      rating = 5;
      description = "There is an indication that " + symbol + " has a great performance";
    } else if (EBITDAMargin > 0.15) {
      rating = 4;
      description = "There is an indication that " + symbol + " has a good performance";
    } else if (EBITDAMargin > 0.10) {
      rating = 3;
      description = "There is an indication that " + symbol + " has an average performance";
    } else if (EBITDAMargin > 0.05) {
      rating = 2;
      description = "There is an indication that " + symbol + " has an average performance";
    } else if (EBITDAMargin > 0.025) {
      rating = 1;
      description = "There is an indication that " + symbol + " has a weak performance";
    } else {
      rating = 0;
      description = "There is an indication that " + symbol + " has a very weak performance";
    }
  }

  @Override
  public String getSymbol() {
    return symbol;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public int getRating() {
    return rating;
  }

  @Override
  public String getValue() {
    return String.format("EBITDA: %.2f EBITDA Margin: %.2f", EBITDA, EBITDAMargin);
  }
}
