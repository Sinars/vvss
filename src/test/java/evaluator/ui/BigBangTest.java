package evaluator.ui;

import evaluator.exception.DuplicateIntrebareException;
import evaluator.exception.InputValidationFailedException;
import evaluator.exception.NotAbleToCreateStatisticsException;
import evaluator.exception.NotAbleToCreateTestException;
import evaluator.model.Intrebare;
import evaluator.model.Statistica;
import evaluator.repository.IntrebariRepository;
import evaluator.service.Service;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.collections.Sets;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

public class BigBangTest {

    Service mockService;

    Service service;

    IntrebariRepository mockRepo;

    IntrebariRepository repo;

    final String filename = "D:\\VVSS\\5-ProiectEvaluatorExamen\\5-ProiectEvaluatorExamen\\ProiectEvaluatorExamen\\resources\\test.txt";

    @Before
    public void setUp() throws Exception {
        repo = new IntrebariRepository(filename);
        mockRepo = Mockito.mock(IntrebariRepository.class);
        mockService = new Service(mockRepo);
        service = new Service(repo);
    }

    @After
    public void tearDown() throws Exception {
        reset(mockRepo);
        Path path = Paths.get(filename);
        Files.newBufferedWriter(path, StandardOpenOption.TRUNCATE_EXISTING);

    }

    @Test
    public void BigBang() throws NotAbleToCreateStatisticsException, NotAbleToCreateTestException, DuplicateIntrebareException, InputValidationFailedException, IOException {

        when(mockRepo.getIntrebari()).thenReturn((fiveQuestionListDiffDomain()));
        Statistica statistica = mockService.getStatistica();
        when(mockRepo.getDistinctDomains()).thenReturn(createFiveDomains());
        evaluator.model.Test test = mockService.createNewTest();
        Intrebare intrebare = createGoodIntrebare();
        when(mockRepo.addIntrebare(intrebare)).thenReturn(intrebare);
        Intrebare output = mockService.addNewIntrebare(intrebare.getEnunt(), intrebare.getVarianta1(), intrebare.getVarianta2(), intrebare.getVarianta3(),
                intrebare.getVariantaCorecta(), intrebare.getDomeniu());
        assertEquals(5, statistica.getIntrebariDomenii().keySet().size());
        statistica.getIntrebariDomenii().values().forEach(value -> {
            assertEquals(1, (int) value);
        });
        assertEquals(5, test.getIntrebari().size());
        assertEquals(intrebare, output);

    }

    @Test
    public void BigBangNoMockito() throws DuplicateIntrebareException, InputValidationFailedException, IOException, NotAbleToCreateTestException, NotAbleToCreateStatisticsException {
        for (int i = 0; i < 5; i++) {
            Intrebare intrebare =createGoodIntrebare("A" + i);
            service.addNewIntrebare(intrebare.getEnunt(), intrebare.getVarianta1(), intrebare.getVarianta2(), intrebare.getVarianta3(),
                    intrebare.getVariantaCorecta(), intrebare.getDomeniu());
        }
        evaluator.model.Test test = service.createNewTest();
        assertEquals(5, test.getIntrebari().size());
        Statistica statistica = service.getStatistica();
        assertEquals(5, statistica.getIntrebariDomenii().keySet().size());
        statistica.getIntrebariDomenii().values().forEach(value -> {
            assertEquals(1, (int) value);
        });
    }

    @Test
    public void getStatistica() throws NotAbleToCreateStatisticsException {
        when(mockRepo.getIntrebari()).thenReturn(fiveQuestionListDiffDomain());
        Statistica statistica = mockService.getStatistica();
        assertEquals(5, statistica.getIntrebariDomenii().keySet().size());
        statistica.getIntrebariDomenii().values().forEach(value -> {
            assertEquals(1, (int) value);
        });
    }

    @Test
    public void createQuestion5Domain() throws NotAbleToCreateTestException {
        when(mockRepo.getIntrebari()).thenReturn(fiveQuestionListDiffDomain());
        when(mockRepo.getDistinctDomains()).thenReturn(createFiveDomains());
        evaluator.model.Test test = mockService.createNewTest();
        assertEquals(5, test.getIntrebari().size());
    }

    @Test
    public void addNewIntrebare() throws IOException, DuplicateIntrebareException, InputValidationFailedException {
        Intrebare intrebare = createGoodIntrebare();
        when(mockRepo.addIntrebare(intrebare)).thenReturn(intrebare);
        Intrebare output = mockService.addNewIntrebare(intrebare.getEnunt(), intrebare.getVarianta1(), intrebare.getVarianta2(), intrebare.getVarianta3(),
                intrebare.getVariantaCorecta(), intrebare.getDomeniu());
        assertEquals(intrebare, output);
    }

    private List<Intrebare> fiveQuestionListDiffDomain() {
        ArrayList<Intrebare> questions = new ArrayList<>();
        questions.add(createQuestion("D"));
        questions.add(createQuestion("A"));
        questions.add(createQuestion("C"));
        questions.add(createQuestion("E"));
        questions.add(createQuestion("F"));
        return questions;
    }


    private Intrebare createGoodIntrebare() {
        Intrebare intrebare = new Intrebare();
        intrebare.setDomeniu("Matematica");
        intrebare.setEnunt("Intrebare?");
        intrebare.setVarianta1("1)");
        intrebare.setVarianta2("2)");
        intrebare.setVarianta3("3)");
        intrebare.setVariantaCorecta("3");
        return intrebare;
    }
    private Intrebare createGoodIntrebare(String domain) {
        Intrebare intrebare = new Intrebare();
        intrebare.setDomeniu(domain);
        intrebare.setEnunt("Intrebare?");
        intrebare.setVarianta1("1)");
        intrebare.setVarianta2("2)");
        intrebare.setVarianta3("3)");
        intrebare.setVariantaCorecta("3");
        return intrebare;
    }



    private Set<String> createFiveDomains() {
        return Sets.newSet("D", "A", "C", "E", "F");
    }

    private Intrebare createQuestion(String domain) {
        Intrebare intrebare = new Intrebare();
        intrebare.setDomeniu(domain);
        intrebare.setEnunt("Intrebare?");
        intrebare.setVarianta1("1)");
        intrebare.setVarianta2("2)");
        intrebare.setVarianta3("3)");
        intrebare.setVariantaCorecta("3");
        return intrebare;
    }

}