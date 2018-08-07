package com.example.praty.climatecontrol;

import org.json.JSONException;
import org.json.JSONObject;

public class WeatherData {

    private String mTemperature;
    private int mCondition;
    private String mIconName;
    private String mCity;

    public static WeatherData fromJson(JSONObject jsonObject){

      try {
          WeatherData weatherData = new WeatherData();

          weatherData.mCity = jsonObject.getString("name");
          weatherData.mCondition=jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
          weatherData.mIconName=getIconName(weatherData.mCondition);

          double temp=jsonObject.getJSONObject("main").getDouble("temp")-273.15;
          int RoundedValue= (int)Math.rint(temp);

          weatherData.mTemperature=Integer.toString(RoundedValue);


          return weatherData;
      }catch(JSONException e){
          e.printStackTrace();
          return null;
      }
    }

    private static String getIconName(int condition){

        if(condition>=0 && condition<300) return "tstorm1";
        else if(condition>=300 && condition<500) return "light_rain";
        else if(condition>=500 && condition<600) return "shower3";
        else if(condition>=600 && condition<=700) return "snow4";
        else if(condition>=701 && condition<=771) return "fog";
        else if(condition>=772 && condition<800) return "tstorm3";
        else if(condition==800) return "sunny";
        else if(condition>=801 && condition<=804)return "cloudy2";
        else if(condition>=900 && condition<=902) return "tstorm3";
        else if(condition==903) return "snow5";
        else if(condition==904) return "sunny";
        else if(condition>=905 && condition<=1000) return "tstorm3";

        return "dunno";
    }

    public String getmTemperature() {
        return mTemperature + "°C";
    }

    public String getmIconName() {
        return mIconName;
    }

    public String getmCity() {
        return mCity;
    }
}
