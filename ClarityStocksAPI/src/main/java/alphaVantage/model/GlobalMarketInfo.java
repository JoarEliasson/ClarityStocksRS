package alphaVantage.model;

import alphaVantage.model.data.global.DailyTopLists;
import alphaVantage.model.data.global.MarketStatus;
import java.util.List;

public class GlobalMarketInfo {

  private DailyTopLists dailyTopLists;
  private List<MarketStatus> marketStatus;

  public DailyTopLists getDailyTopLists() {
    return dailyTopLists;
  }

  public void setDailyTopLists(DailyTopLists dailyTopLists) {
    this.dailyTopLists = dailyTopLists;
  }

  public List<MarketStatus> getMarketStatus() {
    return marketStatus;
  }

  public void setMarketStatus(List<MarketStatus> marketStatus) {
    this.marketStatus = marketStatus;
  }
}