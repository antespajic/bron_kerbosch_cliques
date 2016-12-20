package hr.fer.projekt.clique;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.jgrapht.alg.BronKerboschCliqueFinder;

public class Main {

	public static void main(String[] args) {
		
		try {
			Files.readAllLines(Paths.get("./testFile.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		BronKerboschCliqueFinder<V, E>
	}
}
