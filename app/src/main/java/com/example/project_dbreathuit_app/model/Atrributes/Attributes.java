package com.example.project_dbreathuit_app.model.Atrributes;

import com.google.gson.annotations.SerializedName;

public class Attributes{

	@SerializedName("sunIrradiance")
	private SunIrradiance sunIrradiance;

	@SerializedName("NO")
	private NO nO;

	@SerializedName("rainfall")
	private Rainfall rainfall;

	@SerializedName("notes")
	private Notes notes;

	@SerializedName("uVIndex")
	private UVIndex uVIndex;

	@SerializedName("O3")
	private O3 o3;

	@SerializedName("PM25")
	private PM25 pM25;

	@SerializedName("sunAzimuth")
	private SunAzimuth sunAzimuth;

	@SerializedName("CO2")
	private CO2 cO2;

	@SerializedName("CO2_average")
	private CO2Average cO2Average;

	@SerializedName("sunZenith")
	private SunZenith sunZenith;

	@SerializedName("NO2")
	private NO2 nO2;

	@SerializedName("AQI_predict")
	private AQIPredict aQIPredict;

	@SerializedName("SO2")
	private SO2 sO2;

	@SerializedName("temperature")
	private Temperature temperature;

	@SerializedName("AQI")
	private AQI aQI;

	@SerializedName("PM10")
	private PM10 pM10;

	@SerializedName("humidity")
	private Humidity humidity;

	@SerializedName("location")
	private Location location;

	@SerializedName("windDirection")
	private WindDirection windDirection;

	@SerializedName("windSpeed")
	private WindSpeed windSpeed;

	@SerializedName("sunAltitude")
	private SunAltitude sunAltitude;
	@SerializedName("place")
	private Place place;

	public SunIrradiance getSunIrradiance(){
		return sunIrradiance;
	}
	public Place getPlace(){
		return place;
	}
	public NO getNO(){
		return nO;
	}

	public Rainfall getRainfall(){
		return rainfall;
	}

	public Notes getNotes(){
		return notes;
	}

	public UVIndex getUVIndex(){
		return uVIndex;
	}

	public O3 getO3(){
		return o3;
	}

	public PM25 getPM25(){
		return pM25;
	}

	public SunAzimuth getSunAzimuth(){
		return sunAzimuth;
	}

	public CO2 getCO2(){
		return cO2;
	}

	public CO2Average getCO2Average(){
		return cO2Average;
	}

	public SunZenith getSunZenith(){
		return sunZenith;
	}

	public NO2 getNO2(){
		return nO2;
	}

	public AQIPredict getAQIPredict(){
		return aQIPredict;
	}

	public SO2 getSO2(){
		return sO2;
	}

	public Temperature getTemperature(){
		return temperature;
	}

	public AQI getAQI(){
		return aQI;
	}

	public PM10 getPM10(){
		return pM10;
	}

	public Humidity getHumidity(){
		return humidity;
	}

	public Location getLocation(){
		return location;
	}

	public WindDirection getWindDirection(){
		return windDirection;
	}

	public WindSpeed getWindSpeed(){
		return windSpeed;
	}

	public SunAltitude getSunAltitude(){
		return sunAltitude;
	}
}