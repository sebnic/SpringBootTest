package demo.springBoot.rest;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import demo.springBoot.tool.csv.CSVTool;
import demo.springBoot.tool.csv.CSVToolException;

@RestController
@EnableAutoConfiguration
public class SpringBootRestController {

	@RequestMapping(value="/")
	public String hello() {
		return "Hello";
	}
	
	public static void main(String[] args) throws CSVToolException {
		SpringApplication.run(SpringBootRestController.class, args);
		CSVTool<TaxiStation> csvTool = new CSVTool<TaxiStation>();
		List<TaxiStation> taxiStations = csvTool.getCSVObjects("/paris_taxis_stations.csv", TaxiStation.class);
	}
}
