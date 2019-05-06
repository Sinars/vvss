package evaluator.repository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;


import evaluator.model.Intrebare;
import evaluator.exception.DuplicateIntrebareException;

public class IntrebariRepository {

	private List<Intrebare> intrebari;
	private String fileName;

	public IntrebariRepository(String file) {
		loadIntrebariFromFile(file);
//		intrebari = new ArrayList<>();
		fileName = file;
	}
	
	public Intrebare addIntrebare(Intrebare i) throws DuplicateIntrebareException, IOException {
		if(exists(i))
			throw new DuplicateIntrebareException("Intrebarea deja exista!");
		intrebari.add(i);
		writeToFile(i);
		return i;
	}

	private void writeToFile(Intrebare i) throws IOException {
		Path path = Paths.get(fileName);
		Files.write(path, i.toString().getBytes(), StandardOpenOption.APPEND);
	}
	
	public boolean exists(Intrebare i){
		return (intrebari.contains(i));
	}

	public Intrebare getNthIntrebare(int n) {
		return intrebari.get(n);
	}

	public Set<String> getDistinctDomains(){
		Set<String> domains = new TreeSet<String>();
		for(Intrebare intrebre : intrebari)
			domains.add(intrebre.getDomeniu());
		return domains;
	}

	public List<Intrebare> loadIntrebariFromFile(String f){

		intrebari = new LinkedList<>();
		BufferedReader br = null;
		String line = null;
		Intrebare intrebare;
		
		try{
			br = new BufferedReader(new FileReader(f));
			line = br.readLine();
			while(line != null){
				String[] data = line.split(" ");
				line = br.readLine();

				intrebare = new Intrebare();
				intrebare.setEnunt(data[0]);
				intrebare.setVarianta1(data[1]);
				intrebare.setVarianta2(data[2]);
				intrebare.setVarianta3(data[3]);
				intrebare.setVariantaCorecta(data[4]);
				intrebare.setDomeniu(data[5]);
				intrebari.add(intrebare);
				line = br.readLine();
			}
		
		}
		catch (IOException e) {
			System.out.println("You shouldn't get here");
		}
		
		return intrebari;
	}

	public Set<Intrebare> getDistinctIntrebari() {
		return intrebari.stream().filter(intrebare ->
				intrebari.stream().filter(intrebare1 -> intrebare1.getDomeniu().equals(intrebare.getDomeniu())).count() == 1
		).collect(Collectors.toSet());
	}

	public List<Intrebare> getIntrebari() {
		return intrebari;
	}

}
