package evaluator.service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import evaluator.exception.InputValidationFailedException;
import evaluator.model.Intrebare;
import evaluator.model.Statistica;
import evaluator.model.Test;
import evaluator.repository.IntrebariRepository;
import evaluator.exception.DuplicateIntrebareException;
import evaluator.exception.NotAbleToCreateStatisticsException;
import evaluator.exception.NotAbleToCreateTestException;
import evaluator.util.InputValidation;

public class Service {
	
	private IntrebariRepository intrebariRepository;

	private Random random;
	public Service(IntrebariRepository intrebariRepository) {
		this.intrebariRepository = intrebariRepository;
		random = new Random();
	}
	
	public Intrebare addNewIntrebare(String enunt, String varianta1, String varianta2, String varianta3,
									 String variantaCorecta, String domeniu) throws DuplicateIntrebareException, InputValidationFailedException, IOException {

		InputValidation.validateDomeniu(domeniu);
		InputValidation.validateEnunt(enunt);
		InputValidation.validateVarianta1(varianta1);
		InputValidation.validateVarianta2(varianta2);
		InputValidation.validateVarianta3(varianta3);
		InputValidation.validateVariantaCorecta(variantaCorecta);
		Intrebare intrebare = new Intrebare(enunt, varianta1, varianta2, varianta3, variantaCorecta, domeniu);

		return intrebariRepository.addIntrebare(intrebare);
	}

	private Set<Intrebare> distinctIntrebari() {
		List<Intrebare> intrebari = intrebariRepository.getIntrebari();
		return intrebari.stream().filter(intrebare ->
				intrebari.stream().filter(intrebare1 -> intrebare1.getDomeniu().equals(intrebare.getDomeniu())).count() == 1
		).collect(Collectors.toSet());
	}

	private Intrebare getRandom() {
		int max = intrebariRepository.getIntrebari().size();
		return intrebariRepository.getNthIntrebare(random.nextInt(max));
	}

	public Test createNewTest() throws NotAbleToCreateTestException{
		
		if(intrebariRepository.getIntrebari().size() < 5)
			throw new NotAbleToCreateTestException("Nu exista suficiente intrebari pentru crearea unui test!(5)");
		
		if(distinctIntrebari().size() < 5)
			throw new NotAbleToCreateTestException("Nu exista suficiente domenii pentru crearea unui test!(5)");
		
		List<Intrebare> testIntrebari = new LinkedList<Intrebare>();
		List<String> domenii = new LinkedList<String>();
		Intrebare intrebare;
		Test test = new Test();
		
		while(testIntrebari.size() < 5){
			intrebare = getRandom();
			
			if(!testIntrebari.contains(intrebare) && !domenii.contains(intrebare.getDomeniu())){
				testIntrebari.add(intrebare);
				domenii.add(intrebare.getDomeniu());
			}
			
		}
		
		test.setIntrebari(testIntrebari);
		return test;
		
	}
	
	public Statistica getStatistica() throws NotAbleToCreateStatisticsException{

		List<Intrebare> intrebari = intrebariRepository.getIntrebari();
		if(intrebari.isEmpty())
			throw new NotAbleToCreateStatisticsException("Repository-ul nu contine nicio intrebare!");
		
		Statistica statistica = new Statistica();
		statistica.setIntrebariDomenii(intrebari.stream().collect(Collectors.toMap(Intrebare::getDomeniu,
				item-> (int)intrebari.stream().filter(intrebare -> intrebare.getDomeniu().equals(item.getDomeniu())).count())));
		return statistica;
	}

}
