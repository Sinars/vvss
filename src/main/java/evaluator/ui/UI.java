package evaluator.ui;

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
        while(activ){

            System.out.println("");
            System.out.println("1.Adauga intrebare");
            System.out.println("2.Creeaza test");
            System.out.println("3.Statistica");
            System.out.println("4.Exit");
            System.out.println("");

            try {
                optiune = console.readLine();
                switch(optiune){
                    case "1" :
                        controller.addNewIntrebare();
                        break;
                    case "2" :
                        break;
                    case "3" :
                        Statistica statistica;
                        try {
                            statistica = controller.getStatistica();
                            System.out.println(statistica);
                        } catch (NotAbleToCreateStatisticsException e) {
                            // TODO
                        }

                        break;
                    case "4" :
                        activ = false;
                        break;
                    default:
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }
}
