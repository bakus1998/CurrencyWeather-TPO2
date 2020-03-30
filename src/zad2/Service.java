/**
 *
 *  @author Baka Krzysztof S16696
 *
 */

package zad2;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.*;
import java.util.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class Service {
    private String weatherApi = "eb3bab36dac02e3d24e68a7cd3da4df0";
    Map<String, Double> currency;
    Map<String, Double> currencyCN;
    Map<String, String> countries = new HashMap<>();
    Map<String, String> countriesCC = new HashMap<>();

    private String country;

    public Service(String country) {
        this.country=country;
    }

    public String getURLcon(String site) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        String json="";
        int responseCode =0;

        URL url = new URL(site);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        responseCode=httpURLConnection.getResponseCode();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        while((line=bufferedReader.readLine())!=null){
            sb.append(line+"\n");
            json+=line+"\n";
        }
        return json;

    }

    public String getWeather(String miasto) {
        String toConnection = "http://api.openweathermap.org/data/2.5/weather?q="+miasto+"&appid="+weatherApi;
        String s="";
        System.out.println(toConnection);
        try {
            s=getURLcon(toConnection);
        } catch (IOException e) {
            System.err.println("Błąd połączenia!");
        }
        System.out.println(s);
        return s;
    }

    public Double getRateFor(String kod_waluty) {
        makeIso();
        makeCurrency();
        String countriesGet = countries.get(country);
        String countriesCCGet = countriesCC.get(countriesGet);
        double toRet=0;
        try {
            String connection = getURLcon("https://api.exchangeratesapi.io/latest?base="+countriesCCGet+"&symbols="+kod_waluty);
            JSONObject jsonObject = new JSONObject(connection.substring(connection.indexOf('{')));
            if(jsonObject.getJSONObject("rates")!=null) {
                toRet = jsonObject.getJSONObject("rates").getDouble(kod_waluty);
            }else {
                System.err.println("Błędne dane!");
            }
        }catch (IOException e){
            System.err.println("Błąd połączenia!");
        }
        System.out.println("getRateFor " + kod_waluty + " to " + countriesCCGet + ": "  + toRet);
        return toRet;
    }


    public Double getNBPRate() {
        String countriesGet = countries.get(country);
        String countriesCCGet = countriesCC.get(countriesGet);
        System.out.println(countriesGet);
        System.out.println(countriesCCGet);

        double toRet =1;
        if(!countriesCCGet.equals("PLN")) {
            toRet = currency.get(countriesCCGet);
        }

        System.out.println("getNBPRate: " + toRet);
        return toRet;
    }



    public void makeIso() {
        Locale.setDefault(new Locale("en", "UK"));
        for (String iso : Locale.getISOCountries()) {
            Locale l = new Locale("", iso);
            countries.put(l.getDisplayCountry(), iso);
            Currency c = Currency.getInstance(l);
            try {
                countriesCC.put(iso, c.getCurrencyCode());
            } catch (NullPointerException e) {
                System.out.printf("");
            }
        }

    }

    public void makeCurrency() {
        try {
            currency = new HashMap<>();
            currencyCN = new HashMap<>();
            String connection = getURLcon("http://api.nbp.pl/api/exchangerates/tables/a?format=json");
            String connection2 = getURLcon("http://api.nbp.pl/api/exchangerates/tables/b?format=json");
            JSONObject jsonObject = new JSONObject(connection.substring(connection.indexOf('{')));
            JSONArray jsonArray = jsonObject.getJSONArray("rates");

            JSONObject jsonObject2 = new JSONObject(connection2.substring(connection2.indexOf('{')));
            JSONArray jsonArray2 = jsonObject2.getJSONArray("rates");

            for (int i = 0; i < jsonArray.length(); i++) {
                String code = jsonArray.getJSONObject(i).getString("code");
                double v = jsonArray.getJSONObject(i).getDouble("mid");
                currency.put(code, v);
            }

            for (int i = 0; i < jsonArray2.length(); i++) {
                String code = jsonArray2.getJSONObject(i).getString("code");
                double v = jsonArray2.getJSONObject(i).getDouble("mid");
                currency.put(code, v);
            }

        } catch (IOException e) {

        }
    }

}