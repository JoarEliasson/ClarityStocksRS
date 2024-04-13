package alphaVantage;

import analysis.model.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class AlphaVantageClient {

  private final String apiKey;
  private final HttpClient httpClient;
  private final AlphaVantageParser parser = new AlphaVantageParser();

  //Constructor. Creates instance of httpClient
  public AlphaVantageClient(String apiKey) {
    this.apiKey = apiKey;
    this.httpClient = HttpClient.newHttpClient();
  }

  public static void main(String[] args) {
    AlphaVantageClient client = new AlphaVantageClient("YKB1S8EYZ61LDH9B");
    List<AlphaVantageStockInfo> searchResults = client.searchEndpoint("");
    for (AlphaVantageStockInfo alphaVantageStockInfo : searchResults) {
      System.out.println(alphaVantageStockInfo);
    }

  }
  /** Method for evaluating the business performance of a company.Returns business performance object
   * @author Olivia Svensson
   * */

  private BusinessPerformanceEvaluator getBusinessPerformanceEvaluation() {
    FullStockOverview  fullStockOverview = new FullStockOverview();
    String name = fullStockOverview.getName();
    double ebidta = fullStockOverview.getEBITDA();
    double totalRevenue = fullStockOverview.getRevenueTTM();

    BusinessPerformanceEvaluator businessPerformanceEvaluator = new BusinessPerformanceEvaluator(name, ebidta, totalRevenue);
      return businessPerformanceEvaluator;
  }

  /** Method for getting dividend evaluation depending on fiscal year, also gets dividend yield depending on fiscal year. Returns dividend evaluation timing object
   * @author Olivia Svensson
   * */
  private DividendEvaluationTiming dividendEvaluationTiming(int year) {
      FullStockOverview fullStockOverview = new FullStockOverview();

    String symbol = fullStockOverview.getSymbol();
    int fiscalYear = year;
    double dividendPerShare = fullStockOverview.getDividendPerShare();
    double dividendYield = fullStockOverview.getDividendYield();
    long fiscalYearEnd = Long.parseLong(fullStockOverview.getFiscalYearEnd());

    DividendEvaluationTiming dividendEvaluationTiming = new DividendEvaluationTiming(symbol, fiscalYear, dividendYield, dividendYield, fiscalYearEnd);
    return dividendEvaluationTiming;
  }

  /** Method for evaluating stock price in relation to business performance. Returns stock price in relation to business performance object
   * @author Olivia Svensson
   * */
  private StockPriceInRelationToBusinessPerformance stockPriceInRelationToBusinessPerformance() {
    FullStockOverview fullStockOverview = new FullStockOverview();
    String symbol = fullStockOverview.getName();
    double peRatio = fullStockOverview.getPERatio();
    String sector = fullStockOverview.getSector();
    StockPriceInRelationToBusinessPerformance stockPriceInRelationToBusinessPerformance = new StockPriceInRelationToBusinessPerformance(symbol, peRatio, sector);
    return stockPriceInRelationToBusinessPerformance;
  }

/** Method for analysing the stock price according to the golden cross method.
 * @author Olivia Svensson
 * */
  private GoldenCross goldenCross() {
    FullStockOverview fullStockOverview = new FullStockOverview();
    String symbol = fullStockOverview.getSymbol();
    double ma50 = fullStockOverview.getDay50MovingAverage();
    double ma200 = fullStockOverview.getDay200MovingAverage();
    GoldenCross goldenCross = new GoldenCross(symbol, ma50, ma200);
    String description = goldenCross.getDesciption();
    return goldenCross;
  }

  public AlphaVantageStock getStock(String symbol) {
    FullStockOverview companyOverview = null;
    List<DailyDataPoint> timeSeries = null;
    try {
      companyOverview = getFullStockOverview(symbol);
      timeSeries = getTimeSeries(symbol, Interval.DAILY);
    } catch (Exception e) {
      e.printStackTrace();
    }
    assert companyOverview != null;
    assert timeSeries != null;
    for (DailyDataPoint dailyDataPoint : timeSeries) {
      System.out.println(dailyDataPoint);
    }

    List<DailyDataPoint> filteredDailyDataPoints = filterByYear(timeSeries,
        new int[]{2022, 2023, 2024});
    return new AlphaVantageStock(companyOverview, filteredDailyDataPoints,
        (new PERatioEvaluation(symbol, companyOverview.getPERatio())));
  }

  private List<DailyDataPoint> filterByYear(List<DailyDataPoint> dailyDataPoints, int[] years) {
    List<DailyDataPoint> filteredDailyDataPoints = new ArrayList<>();
    for (DailyDataPoint dailyDataPoint : dailyDataPoints) {
      String dateString = dailyDataPoint.getDate();
      String[] dateParts = dateString.split("-");
      int dataYear = Integer.parseInt(dateParts[0]);
      for (int year : years) {
        if (dataYear == year) {
          System.out.println(dailyDataPoint);
          filteredDailyDataPoints.add(dailyDataPoint);
        }
      }
    }
    return filteredDailyDataPoints;
  }

  public CompanyOverview getCompanyOverview(String symbol) {
    String url = String.format(
        "https://www.alphavantage.co/query?function=OVERVIEW&symbol=%s&apikey=%s", symbol, apiKey);
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .timeout(Duration.ofMinutes(1))
        .GET()
        .build();
    try {
      HttpResponse<String> response = httpClient.send(request,
          HttpResponse.BodyHandlers.ofString());
      if (response.statusCode() == 200) {
        return parser.parseCompanyOverview(response.body());
      } else {
        throw new RuntimeException(
            "Failed to fetch data: HTTP status code " + response.statusCode());
      }
    } catch (Exception e) {
      throw new RuntimeException("Error fetching company overview", e);
    }
  }

  public FullStockOverview getFullStockOverview(String symbol) {
    String url = String.format(
        "https://www.alphavantage.co/query?function=OVERVIEW&symbol=%s&apikey=%s", symbol, apiKey);
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .timeout(Duration.ofMinutes(1))
        .GET()
        .build();
    try {
      HttpResponse<String> response = httpClient.send(request,
          HttpResponse.BodyHandlers.ofString());
      if (response.statusCode() == 200) {
        return parser.parseFullStockOverview(response.body());
      } else {
        throw new RuntimeException(
            "Failed to fetch data: HTTP status code " + response.statusCode());
      }
    } catch (Exception e) {
      throw new RuntimeException("Error fetching company overview", e);
    }
  }

  public List<DailyDataPoint> getTimeSeries(String symbol, Interval interval) throws Exception {

    String urlString =
        "https://www.alphavantage.co/query?function=" + interval.getUrlParameter() + "&symbol="
            + symbol + "&outputsize=full&apikey=" + apiKey;
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(urlString))
        .build();
    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    return parser.parseTimeSeries(response.body());
  }

  public List<AlphaVantageStockInfo> searchEndpoint(String query) {
    String url = String.format(
        "https://www.alphavantage.co/query?function=SYMBOL_SEARCH&keywords=%s&apikey=%s", query,
        apiKey);
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .timeout(Duration.ofMinutes(1))
        .GET()
        .build();
    try {
      HttpResponse<String> response = httpClient.send(request,
          HttpResponse.BodyHandlers.ofString());
      if (response.statusCode() == 200) {
        return parser.parseSearchResults(response.body());
      } else {
        throw new RuntimeException(
            "Failed to fetch data: HTTP status code " + response.statusCode());
      }
    } catch (Exception e) {
      throw new RuntimeException("Error fetching search results", e);
    }
  }

  public List<String> getIncomeStatement(String symbol) {
    String url = String.format(
        "https://www.alphavantage.co/query?function=INCOME_STATEMENT&symbol=%s&apikey=%s", symbol, apiKey);
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .timeout(Duration.ofMinutes(1))
        .GET()
        .build();
    try {
      HttpResponse<String> response = httpClient.send(request,
          HttpResponse.BodyHandlers.ofString());
      if (response.statusCode() == 200) {
        return parser.parseIncomeStatement(response.body());
      } else {
        throw new RuntimeException(
            "Failed to fetch data: HTTP status code " + response.statusCode());
      }
    } catch (Exception e) {
      throw new RuntimeException("Error fetching company overview", e);
    }
  }

}
