package alphaVantage;

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

    public AlphaVantageClient(String apiKey) {
        this.apiKey = apiKey;
        this.httpClient = HttpClient.newHttpClient();
    }

    public CompanyOverview getCompanyOverview(String symbol) throws Exception {
        String url = String.format("https://www.alphavantage.co/query?function=OVERVIEW&symbol=%s&apikey=%s", symbol, apiKey);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofMinutes(1))
                .GET()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return parser.parseCompanyOverview(response.body());
            } else {
                throw new RuntimeException("Failed to fetch data: HTTP status code " + response.statusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error fetching company overview", e);
        }
    }

    public List<DataPoint> getTimeSeries(String symbol, Interval interval) throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        String urlString = "https://www.alphavantage.co/query?function=" + interval.getUrlParameter() + "&symbol=" + symbol + "&apikey=" + apiKey;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlString))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return parser.parseTimeSeries(response.body());
    }

    public List<DataPoint> getFilteredSeries(int month, int year) {

        if (month == -1) {
            return getFilteredSeries(year);
        }

        List<DataPoint> timeSeries = new ArrayList<>();
        List<DataPoint> unfilteredTimeSeries = new ArrayList<>();
        try {
            unfilteredTimeSeries = getTimeSeries("AAPL", Interval.DAILY);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (DataPoint dataPoint : unfilteredTimeSeries) {
            int dataPointYear = Integer.parseInt(dataPoint.getDate().substring(0, 4));
            int dataPointMonth = Integer.parseInt(dataPoint.getDate().substring(5, 7));
            if (dataPointYear == year && dataPointMonth == month) {
                timeSeries.add(dataPoint);
            }
        }
        return timeSeries;
    }

    public List<DataPoint> getFilteredSeries(int year) {
        List<DataPoint> timeSeries = new ArrayList<>();
        List<DataPoint> unfilteredTimeSeries = new ArrayList<>();
        try {
            unfilteredTimeSeries = getTimeSeries("AAPL", Interval.DAILY);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (DataPoint dataPoint : unfilteredTimeSeries) {
            int dataPointYear = Integer.parseInt(dataPoint.getDate().substring(0, 4));
            if (dataPointYear == year) {
                timeSeries.add(dataPoint);
            }
        }
        return timeSeries;
    }
}
