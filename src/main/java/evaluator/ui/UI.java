package evaluator.ui;

import evaluator.exception.DuplicateIntrebareException;
import evaluator.exception.InputValidationFailedException;
import evaluator.exception.NotAbleToCreateTestException;
import evaluator.service.Service;
import evaluator.exception.NotAbleToCreateStatisticsException;
import evaluator.model.Statistica;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class UI {

    private Service controller;

    private BufferedReader console = new BufferedReader(new InputStreamReader(System.in));


    public UI(Service controller) {
        this.controller = controller;
    }

    public void run() {
        boolean activ = true;
        String optiune;
        while (activ) {

            System.out.println("");
            System.out.println("1.Adauga intrebare");
            System.out.println("2.Creeaza test");
            System.out.println("3.Statistica");
            System.out.println("4.Exit");
            System.out.println("");

            try {
                optiune = console.readLine();
                switch (optiune) {
                    case "1":
                        System.out.println("Enunt: ");
                        String enunt = console.readLine();
                        System.out.println("Var 1: ");
                        String var1 = console.readLine();
                        System.out.println("Var 2: ");
                        String var2 = console.readLine();
                        System.out.println("Var 3: ");
                        String var3 = console.readLine();
                        System.out.println("Corect: ");
                        String corect = console.readLine();
                        System.out.println("Domeniu: ");
                        String domeniu = console.readLine();
                        controller.addNewIntrebare(enunt, var1, var2, var3, corect, domeniu);
                        break;
                    case "2":
                        controller.createNewTest();
                        break;
                    case "3":
                        Statistica statistica;
                        statistica = controller.getStatistica();
                        System.out.println(statistica);


                        break;
                    case "4":
                        activ = false;
                        break;
                    default:
                        break;
                }
            } catch (IOException | DuplicateIntrebareException | InputValidationFailedException | NotAbleToCreateTestException | NotAbleToCreateStatisticsException e) {
                System.out.println(e.getMessage());


            }

        }
    }
}
