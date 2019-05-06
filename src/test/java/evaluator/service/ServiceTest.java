package evaluator.service;

import evaluator.exception.DuplicateIntrebareException;
import evaluator.exception.InputValidationFailedException;
import evaluator.exception.NotAbleToCreateStatisticsException;
import evaluator.exception.NotAbleToCreateTestException;
import evaluator.model.Intrebare;
import evaluator.model.Statistica;
import evaluator.repository.IntrebariRepository;
import jdk.internal.util.xml.impl.Input;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.util.collections.Sets;

import java.io.IOException;
import java.io.InvalidClassException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class ServiceTest {


    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private IntrebariRepository repo;
    private Service service;

    @Before
    public void setUp() throws Exception {
        repo = mock(IntrebariRepository.class);
        service = new Service(repo);
    }

    @After
    public void tearDown() throws Exception {
        reset(repo);
    }

    @Test(expected = NotAbleToCreateStatisticsException.class)
    public void getStatisticaError() throws NotAbleToCreateStatisticsException {
        service.getStatistica();
    }

    @Test
    public void getStatistica() throws NotAbleToCreateStatisticsException {
        when(repo.getIntrebari()).thenReturn(fiveQuestionListDiffDomain());
        Statistica statistica = service.getStatistica();
        assertEquals(5, statistica.getIntrebariDomenii().keySet().size());
        statistica.getIntrebariDomenii().values().forEach(value-> {
            assertEquals(1, (int)value);
        }) ;
    }


    @Test
    public void addNewIntrebare() throws IOException, DuplicateIntrebareException, InputValidationFailedException {
        Intrebare intrebare = createGoodIntrebare();
        when(repo.addIntrebare(intrebare)).thenReturn(intrebare);
        Intrebare output = service.addNewIntrebare(intrebare.getEnunt(), intrebare.getVarianta1(), intrebare.getVarianta2(), intrebare.getVarianta3(),
                intrebare.getVariantaCorecta(), intrebare.getDomeniu());
        assertEquals(intrebare, output);
    }

    @Test
    public void addNewIntrebareEnuntSizeZero() throws DuplicateIntrebareException, InputValidationFailedException, IOException {
        expectedException.expect(InputValidationFailedException.class);
        expectedException.expectMessage(CoreMatchers.equalTo("Enuntul este vid!"));
        Intrebare intrebare = createEnuntSizeZeroQuestion();
        service.addNewIntrebare(intrebare.getEnunt(), intrebare.getVarianta1(), intrebare.getVarianta2(), intrebare.getVarianta3(),
                intrebare.getVariantaCorecta(), intrebare.getDomeniu());
    }

    @Test
    public void addNewIntrebareEnuntSizeLong() throws DuplicateIntrebareException, InputValidationFailedException, IOException {
        expectedException.expect(InputValidationFailedException.class);
        expectedException.expectMessage("Lungimea enuntului depaseste 100 de caractere!");
        Intrebare intrebare = createLongEnuntSizeQuestion();
        service.addNewIntrebare(intrebare.getEnunt(), intrebare.getVarianta1(), intrebare.getVarianta2(), intrebare.getVarianta3(),
                intrebare.getVariantaCorecta(), intrebare.getDomeniu());
    }

    @Test
    public void addNewIntrebareEnuntLowerCase() throws DuplicateIntrebareException, InputValidationFailedException, IOException {
        expectedException.expect(InputValidationFailedException.class);
        expectedException.expectMessage("Prima litera din enunt nu e majuscula!");
        Intrebare intrebare = createLoweCaseEnuntIntrebare();
        service.addNewIntrebare(intrebare.getEnunt(), intrebare.getVarianta1(), intrebare.getVarianta2(), intrebare.getVarianta3(),
                intrebare.getVariantaCorecta(), intrebare.getDomeniu());
    }

    @Test
    public void addNewIntrebareEnuntNoQuestionMark() throws DuplicateIntrebareException, InputValidationFailedException, IOException {
        expectedException.expect(InputValidationFailedException.class);
        expectedException.expectMessage("Ultimul caracter din enunt nu e '?'!");
        Intrebare intrebare = createIntrebareNoQuestionMark();
        service.addNewIntrebare(intrebare.getEnunt(), intrebare.getVarianta1(), intrebare.getVarianta2(), intrebare.getVarianta3(),
                intrebare.getVariantaCorecta(), intrebare.getDomeniu());
    }

    @Test
    public void addNewIntrebareNoDomain() throws DuplicateIntrebareException, InputValidationFailedException, IOException {
        expectedException.expect(InputValidationFailedException.class);
        expectedException.expectMessage("Domeniul este vid!");
        Intrebare intrebare = createNoDomainQuestion();
        service.addNewIntrebare(intrebare.getEnunt(), intrebare.getVarianta1(), intrebare.getVarianta2(), intrebare.getVarianta3(),
                intrebare.getVariantaCorecta(), intrebare.getDomeniu());
    }

    @Test
    public void addNewIntrebareLongDomain() throws DuplicateIntrebareException, InputValidationFailedException, IOException {
        expectedException.expect(InputValidationFailedException.class);
        expectedException.expectMessage("Lungimea domeniului depaseste 30 de caractere!");
        Intrebare intrebare = createLongDomainQuestion();
        service.addNewIntrebare(intrebare.getEnunt(), intrebare.getVarianta1(), intrebare.getVarianta2(), intrebare.getVarianta3(),
                intrebare.getVariantaCorecta(), intrebare.getDomeniu());
    }

    @Test
    public void addNewIntrebareDomainLoweCase() throws DuplicateIntrebareException, InputValidationFailedException, IOException {
        expectedException.expect(InputValidationFailedException.class);
        expectedException.expectMessage("Prima litera din domeniu nu e majuscula!");
        Intrebare intrebare = createLowerCaseDomainQuestion();
        service.addNewIntrebare(intrebare.getEnunt(), intrebare.getVarianta1(), intrebare.getVarianta2(), intrebare.getVarianta3(),
                intrebare.getVariantaCorecta(), intrebare.getDomeniu());
    }

    @Test
    public void addNewIntrebareTC3() throws DuplicateIntrebareException, InputValidationFailedException, IOException {
        expectedException.expect(InputValidationFailedException.class);
        expectedException.expectMessage("Ultimul caracter din enunt nu e '?'!");
        Intrebare intrebare = createTC3();
        service.addNewIntrebare(intrebare.getEnunt(), intrebare.getVarianta1(), intrebare.getVarianta2(), intrebare.getVarianta3(),
                intrebare.getVariantaCorecta(), intrebare.getDomeniu());
    }

    @Test
    public void addNewIntrebareTC4() throws DuplicateIntrebareException, InputValidationFailedException, IOException {
        Intrebare intrebare = createTC4();
        when(repo.addIntrebare(intrebare)).thenReturn(intrebare);
        Intrebare output = service.addNewIntrebare(intrebare.getEnunt(), intrebare.getVarianta1(), intrebare.getVarianta2(), intrebare.getVarianta3(),
                intrebare.getVariantaCorecta(), intrebare.getDomeniu());
        assertEquals(intrebare, output);
    }

    @Test
    public void TC5() throws IOException, DuplicateIntrebareException, InputValidationFailedException {
        Intrebare intrebare = createTC5();
        when(repo.addIntrebare(intrebare)).thenReturn(intrebare);
        Intrebare output = service.addNewIntrebare(intrebare.getEnunt(), intrebare.getVarianta1(), intrebare.getVarianta2(), intrebare.getVarianta3(),
                intrebare.getVariantaCorecta(), intrebare.getDomeniu());
        assertEquals(intrebare, output);
    }

    @Test
    public void TC8() throws IOException, DuplicateIntrebareException, InputValidationFailedException {
        Intrebare intrebare = createTC8();
        when(repo.addIntrebare(intrebare)).thenReturn(intrebare);
        Intrebare output = service.addNewIntrebare(intrebare.getEnunt(), intrebare.getVarianta1(), intrebare.getVarianta2(), intrebare.getVarianta3(),
                intrebare.getVariantaCorecta(), intrebare.getDomeniu());
        assertEquals(intrebare, output);
    }

    @Test
    public void TC9() throws IOException, DuplicateIntrebareException, InputValidationFailedException {
        Intrebare intrebare = createTC9();
        when(repo.addIntrebare(intrebare)).thenReturn(intrebare);
        Intrebare output = service.addNewIntrebare(intrebare.getEnunt(), intrebare.getVarianta1(), intrebare.getVarianta2(), intrebare.getVarianta3(),
                intrebare.getVariantaCorecta(), intrebare.getDomeniu());
        assertEquals(intrebare, output);
    }

    @Test
    public void TC10() throws IOException, DuplicateIntrebareException, InputValidationFailedException {
        Intrebare intrebare = createTC10();
        when(repo.addIntrebare(intrebare)).thenReturn(intrebare);
        Intrebare output = service.addNewIntrebare(intrebare.getEnunt(), intrebare.getVarianta1(), intrebare.getVarianta2(), intrebare.getVarianta3(),
                intrebare.getVariantaCorecta(), intrebare.getDomeniu());
        assertEquals(intrebare, output);
    }

    @Test
    public void createQuestionFailDomain() throws NotAbleToCreateTestException {
        expectedException.expect(NotAbleToCreateTestException.class);
        expectedException.expectMessage("Nu exista suficiente intrebari pentru crearea unui test!(5)");
        when(repo.getIntrebari()).thenReturn(fourQuestionList());
        when(repo.getDistinctDomains()).thenReturn(createDomains());
        service.createNewTest();
    }

    @Test
    public void createQuestionFailQuestion() throws NotAbleToCreateTestException {
        expectedException.expect(NotAbleToCreateTestException.class);
        expectedException.expectMessage("Nu exista suficiente domenii pentru crearea unui test!(5)");
        when(repo.getIntrebari()).thenReturn(fiveQuestionList());
        service.createNewTest();
    }


    @Test
    public void createQuestion5Domain() throws NotAbleToCreateTestException {
        when(repo.getIntrebari()).thenReturn(fiveQuestionListDiffDomain());
        when(repo.getDistinctDomains()).thenReturn(createFiveDomains());
        evaluator.model.Test test = service.createNewTest();
        assertEquals(5, test.getIntrebari().size());
    }

    @Test
    public void create6Question5Domain() throws NotAbleToCreateTestException {
        when(repo.getIntrebari()).thenReturn(sixQuestionList());
        when(repo.getDistinctDomains()).thenReturn(createFiveDomains());
        evaluator.model.Test test = service.createNewTest();
        assertEquals(5, test.getIntrebari().size());
    }


    private Set<String> createDomains() {
        return Sets.newSet("D", "A", "C", "E");
    }

    private Set<String> createFiveDomains() {
        return Sets.newSet("D", "A", "C", "E", "F");
    }

    private List<Intrebare> sixQuestionList() {
        ArrayList<Intrebare> questions = new ArrayList<>();
        questions.add(createQuestion("D"));
        questions.add(createQuestion("A"));
        questions.add(createQuestion("C"));
        questions.add(createQuestion("E"));
        questions.add(createQuestion("F"));
        questions.add(createQuestion("F"));
        return questions;
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

    private List<Intrebare> fiveQuestionList() {
        ArrayList<Intrebare> questions = new ArrayList<>();
        questions.add(createQuestion("D"));
        questions.add(createQuestion("A"));
        questions.add(createQuestion("C"));
        questions.add(createQuestion("E"));
        questions.add(createQuestion("E"));
        return questions;
    }

    private List<Intrebare> fourQuestionList() {
        ArrayList<Intrebare> questions = new ArrayList<>();
        questions.add(createQuestion("D"));
        questions.add(createQuestion("A"));
        questions.add(createQuestion("C"));
        questions.add(createQuestion("E"));
        return questions;
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

    private Intrebare createLowerCaseDomainQuestion() {
        Intrebare intrebare = new Intrebare();
        intrebare.setDomeniu("unDomeniu");
        intrebare.setEnunt("Intrebare");
        intrebare.setVarianta1("1)");
        intrebare.setVarianta2("2)");
        intrebare.setVarianta3("3)");
        intrebare.setVariantaCorecta("3");
        return intrebare;
    }

    private Intrebare createIntrebareNoQuestionMark() {
        Intrebare intrebare = new Intrebare();
        intrebare.setDomeniu("Matematica");
        intrebare.setEnunt("Intrebare");
        intrebare.setVarianta1("1)");
        intrebare.setVarianta2("2)");
        intrebare.setVarianta3("3)");
        intrebare.setVariantaCorecta("3");
        return intrebare;
    }

    private Intrebare createNoDomainQuestion() {
        Intrebare intrebare = new Intrebare();
        intrebare.setDomeniu("");
        intrebare.setEnunt("Intrebare?");
        intrebare.setVarianta1("1)");
        intrebare.setVarianta2("2)");
        intrebare.setVarianta3("3)");
        intrebare.setVariantaCorecta("3");
        return intrebare;
    }

    private Intrebare createLongDomainQuestion() {
        Intrebare intrebare = new Intrebare();
        intrebare.setDomeniu("UnDomeniuMultPreaLungCaSaIncapa");
        intrebare.setEnunt("Intrebare?");
        intrebare.setVarianta1("1)");
        intrebare.setVarianta2("2)");
        intrebare.setVarianta3("3)");
        intrebare.setVariantaCorecta("3");
        return intrebare;
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

    private Intrebare createLoweCaseEnuntIntrebare() {
        Intrebare intrebare = new Intrebare();
        intrebare.setDomeniu("Matematica");
        intrebare.setEnunt("intrebare?");
        intrebare.setVarianta1("1)");
        intrebare.setVarianta2("2)");
        intrebare.setVarianta3("3)");
        intrebare.setVariantaCorecta("3");
        return intrebare;
    }


    private Intrebare createEnuntSizeZeroQuestion() {
        Intrebare intrebare = new Intrebare();
        intrebare.setDomeniu("Matematica");
        intrebare.setEnunt("");
        intrebare.setVarianta1("1)");
        intrebare.setVarianta2("2)");
        intrebare.setVarianta3("3)");
        intrebare.setVariantaCorecta("3");
        return intrebare;
    }

    private Intrebare createLongEnuntSizeQuestion() {
        Intrebare intrebare = new Intrebare();
        intrebare.setDomeniu("Matematica");
        intrebare.setEnunt("Nu stiu ce amar sa scriu de 100 de caractere, prea multe de scris ma plictisesc ce intrebare stupida?");
        intrebare.setVarianta1("1)");
        intrebare.setVarianta2("2)");
        intrebare.setVarianta3("3)");
        intrebare.setVariantaCorecta("3");
        return intrebare;
    }

    private Intrebare createTC3() {
        Intrebare intrebare = new Intrebare();
        intrebare.setDomeniu("Matematica");
        intrebare.setEnunt("M");
        intrebare.setVarianta1("1)");
        intrebare.setVarianta2("2)");
        intrebare.setVarianta3("3)");
        intrebare.setVariantaCorecta("3");
        return intrebare;
    }

    private Intrebare createTC4() {
        Intrebare intrebare = new Intrebare();
        intrebare.setDomeniu("Matematica");
        intrebare.setEnunt("Nu stiu ce amar sa scriu de 100 de caractere, prea multe de scris ma plictisesc ce intrebare stupi?");
        intrebare.setVarianta1("1)");
        intrebare.setVarianta2("2)");
        intrebare.setVarianta3("3)");
        intrebare.setVariantaCorecta("3");
        return intrebare;
    }

    private Intrebare createTC5() {
        Intrebare intrebare = new Intrebare();
        intrebare.setDomeniu("Matematica");
        intrebare.setEnunt("Nu stiu ce amar sa scriu de 100 de caractere, prea multe de scris ma plictisesc ce intrebare stupid?");
        intrebare.setVarianta1("1)");
        intrebare.setVarianta2("2)");
        intrebare.setVarianta3("3)");
        intrebare.setVariantaCorecta("3");
        return intrebare;
    }

    private Intrebare createTC8() {
        Intrebare intrebare = new Intrebare();
        intrebare.setDomeniu("D");
        intrebare.setEnunt("Intrebare?");
        intrebare.setVarianta1("1)");
        intrebare.setVarianta2("2)");
        intrebare.setVarianta3("3)");
        intrebare.setVariantaCorecta("3");
        return intrebare;
    }

    private Intrebare createTC9() {
        Intrebare intrebare = new Intrebare();
        intrebare.setDomeniu("UnDomeniuMultPreaLungCaSaInca");
        intrebare.setEnunt("Intrebare?");
        intrebare.setVarianta1("1)");
        intrebare.setVarianta2("2)");
        intrebare.setVarianta3("3)");
        intrebare.setVariantaCorecta("3");
        return intrebare;
    }

    private Intrebare createTC10() {
        Intrebare intrebare = new Intrebare();
        intrebare.setDomeniu("UnDomeniuMultPreaLungCaSaIncap");
        intrebare.setEnunt("Intrebare?");
        intrebare.setVarianta1("1)");
        intrebare.setVarianta2("2)");
        intrebare.setVarianta3("3)");
        intrebare.setVariantaCorecta("3");
        return intrebare;
    }


}