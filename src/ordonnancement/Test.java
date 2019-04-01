package ordonnancement;

import java.util.ArrayList;
import java.util.List;

import panel.FenetreAffichage;

public class Test {

	public static void main(String[] args) {
		TacheP T1=new TacheP(0, 10, 40, 1, "Catégorie 1", 40);
		TacheP T2=new TacheP(0, 20, 60, 2, "Catégorie 2", 60);
		TacheP T3=new TacheP(0, 20, 80, 3, "Catégorie 3", 80);
		
		List<TacheP> listTacheP= new ArrayList<TacheP>();
		listTacheP.add(T1);
		listTacheP.add(T2);
		listTacheP.add(T3);
		Ordonnanceur ord=new Ordonnanceur(listTacheP, new ArrayList<TacheAP>());
		ord.politiqueRR();
		System.out.println(ord.getExec() + "\n Size: " + ord.getExec().size());
		
		FenetreAffichage F=new FenetreAffichage(ord.getExec());
		

	}

}
