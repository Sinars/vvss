package evaluator;

import java.io.IOException;

import evaluator.service.Service;
import evaluator.repository.IntrebariRepository;
import evaluator.ui.UI;

//functionalitati
//F01.	 adaugarea unei noi intrebari pentru un anumit domeniu (enunt intrebare, raspuns 1, raspuns 2, raspuns 3, raspunsul corect, domeniul) in setul de intrebari disponibile;
//F02.	 crearea unui nou test (testul va contine 5 intrebari alese aleator din cele disponibile, din domenii diferite);
//F03.	 afisarea unei statistici cu numarul de intrebari organizate pe domenii.

public class StartApp {

	private static final String file = "src\\main\\java\\evaluator\\intrebari.txt";
	
	public static void main(String[] args) throws IOException {


		IntrebariRepository repo = new IntrebariRepository(file);
		Service appController = new Service(repo);
		UI ui = new UI(appController);
		ui.run();

	}

}
