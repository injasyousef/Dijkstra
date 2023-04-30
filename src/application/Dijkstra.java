package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Dijkstra {
	static ArrayList<Country> Countrys = new ArrayList<Country>();
	static HashMap<String, Country> allNodes = new HashMap<>();
	private Country source;
	private Country destination;
	private PriorityQueue<Country> heap;

	public Dijkstra() {
		// TODO Auto-generated constructor stub
	}

	public Dijkstra(ArrayList<Country> Countrys, Country source, Country destination) {// for each path between tow
																						// countries
		heap = new PriorityQueue<>(Countrys.size());// make an heap with countries number size
		this.destination = destination;
		this.Countrys = Countrys;
		for (Country country : Countrys) {// reset all countries cost
			country.resetTemps();// make all with max integer cost
			if (country == source) {
				country.setTempCost(0);// make the source country cost to zero
			}
			heap.add(country);// add all countries to the heap
		}
	}

	public void generateDijkstra() {
		while (!heap.isEmpty() && heap.contains(destination)) {
			Country minUnknown = heap.poll();
			LinkedList<Edge> adjacentsList = minUnknown.getAdjacentsList();
			
			for (Edge edge : adjacentsList) {
				Country adjacentCity = edge.getAdjacentCity();
				
					if ((minUnknown.getTempCost() + edge.getDistance()) < adjacentCity.getTempCost()) {
						heap.remove(adjacentCity);
						adjacentCity.setTempCost(minUnknown.getTempCost() + edge.getDistance());
						adjacentCity.setTempPrev(minUnknown);
						heap.add(adjacentCity);
					}
	
			}
		}
	}

	private String pathString;
	 String distanceString;

	public Country[] pathTo(Country destination) {
		LinkedList<Country> countries = new LinkedList<>();
		Country iterator = destination;
		distanceString = String.format("%.2f", destination.getTempCost());
		while (iterator != source) {
			countries.addFirst(iterator);
			pathString = iterator.getFullName() + ": " + String.format("%.2f", iterator.getTempCost()) + "  KM" + "\n"
					+ "\t\t*  " + pathString;
			iterator = iterator.getTempPrev();
		}

		return countries.toArray(new Country[0]);
	}

	public String getPathString() {
		if (countOccurrences(pathString, '\n') <= 1) {
			pathString = "No Path";
			distanceString = "\t\t\t------------------";
			return pathString;
		}

		pathString = "\t" + pathString;

		int truncateIndex = pathString.lastIndexOf('\n');
		pathString = pathString.substring(0, truncateIndex);

		return pathString;
	}

	private static int countOccurrences(String haystack, char needle) {
		int count = 0;
		for (int i = 0; i < haystack.length(); i++) {
			if (haystack.charAt(i) == needle) {
				count++;
			}
		}
		return count;
	}

	public static ArrayList<Country> readFile() throws FileNotFoundException {// method whose read the file
		String line = null;// to read each line
		int numberOfCountryes, numberOfEdges;
		File file = new File("data.txt");// the data file
		Scanner scan = new Scanner(file);
		line = scan.nextLine();// read the first line only
		String[] str = line.split(" ");
		numberOfCountryes = Integer.parseInt(str[0]);// first integer is number of countries
		numberOfEdges = Integer.parseInt(str[1]);// second integer is number of edges
		for (int i = 0; i < numberOfCountryes; i++) {// to read all countries data
			float x, y;
			line = scan.nextLine();// each line is country
			String[] strN = line.split(" ");
			x = (float) Double.parseDouble(strN[1]);
			y = (float) Double.parseDouble(strN[2]);
			Country newCountry = new Country(strN[0], x, y);// make an new country
			Countrys.add(newCountry);// add it to the array list
			allNodes.putIfAbsent(strN[0], newCountry);// add it to the hash map

		}
		for (int i = 0; i < numberOfEdges; i++) {// to read all edges
			line = scan.nextLine();// each line is country
			String[] strN = line.split(" ");
			String fromCityName = strN[0], toCityName = strN[1];// name of source and destination country
			Country fromCity = allNodes.get(fromCityName), toCity = allNodes.get(toCityName);
			double distance = distance(fromCity.x,fromCity.y,toCity.x,toCity.y);
			fromCity.addAdjacentCity(toCity, distance); // add an path between them
		}

		return Countrys;

	}
	
	public static double distance(double x1, double y1, double x2, double y2) {
		 return 6378.8
					* Math.acos((Math.sin(Math.toRadians(y1)) * Math.sin(Math.toRadians(y2)))
							+ Math.cos(Math.toRadians(y1)) * Math.cos(Math.toRadians(y2))
									* Math.cos(Math.toRadians(x1) - Math.toRadians(x2)));
		  }


}
