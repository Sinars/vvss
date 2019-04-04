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

	private Intrebare getRandom(List<Intrebare> intrebari) {
        int max = intrebari.size();
        return intrebari.get(random.nextInt(max));
    }

	public Test createNewTest() throws NotAbleToCreateTestException{
		
		if(intrebariRepository.getIntrebari().size() < 5)
			throw new NotAbleToCreateTestException("Nu exista suficiente intrebari pentru crearea unui test!(5)");
		
		if(intrebariRepository.getDistinctDomains().size() < 5)
			throw new NotAbleToCreateTestException("Nu exista suficiente domenii pentru crearea unui test!(5)");
		
		List<Intrebare> testIntrebari = new LinkedList<Intrebare>();
		List<String> domenii = new LinkedList<String>();
		Intrebare intrebare;
		Test test = new Test();
		List<Intrebare> totalIntrebari = new ArrayList<>(intrebariRepository.getIntrebari());
		while(testIntrebari.size() < 5){
			intrebare = getRandom(totalIntrebari);
			if(!testIntrebari.contains(intrebare) && !domenii.contains(intrebare.getDomeniu())){
				testIntrebari.add(intrebare);
				domenii.add(intrebare.getDomeniu());
			}
            totalIntrebari.remove(intrebare);
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
