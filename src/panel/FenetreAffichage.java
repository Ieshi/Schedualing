package panel;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import ordonnancement.Ordonnanceur;
import ordonnancement.TacheP;

public class FenetreAffichage extends JFrame{
	 private JPanel mainPan = new JPanel();
	 private List<String> criteres = new ArrayList<String>();
	 private List<JPanel> sousPanel=new ArrayList<JPanel>();
	
	 public FenetreAffichage(List<TacheP> exec) {
		this.setTitle("Ordonnancement");
		
		this.setContentPane(mainPan); 
		
		initMainPan(exec);
		
		this.pack();
		
		this.setVisible(true);
	}
	 
	 protected void initMainPan(List<TacheP> exec) {
		 int i=0;
		 //On défini les panels des taches
		 for(i=0; i<exec.size(); i++) {
			 if(criteres.indexOf(exec.get(i).getNom())==-1 && !exec.get(i).getNom().toLowerCase().equals("null")) { //Si la tache n'existe pas dans la liste des taches déjà executée
				 criteres.add(exec.get(i).getNom());
				 sousPanel.add(new JPanel());
				 sousPanel.get(criteres.indexOf(exec.get(i).getNom())).add(new JTextArea(exec.get(i).getNom()));
			 }
		 }
			 
		 for(i=0; i<exec.size(); i++) {
			 
			 JPanel bleu = new JPanel();
			 bleu.setBackground(Color.blue);
			 
			 //Si c'est une execution de tache on met un carre bleu pour la tache et un rouge pour les autres
			 if(!exec.get(i).getNom().toLowerCase().equals("null"))
			 {
				 int index=criteres.indexOf(exec.get(i).getNom());
				 sousPanel.get(index).add(bleu);

				 for(int j=0; j<index; j++)
				 {
					 String nomTacheQuestion=criteres.get(j);
					 int indexTacheQuestion=indexOfByName(exec,nomTacheQuestion);
					 TacheP tacheEnQuestion=exec.get(indexTacheQuestion);
					 int periode=tacheEnQuestion.getPeriode();
					 if(tacheEnQuestion.getReveil()==i*Ordonnanceur.getQuantum() || (i*Ordonnanceur.getQuantum()>=periode && (i*Ordonnanceur.getQuantum())%periode==0 ) || (tacheEnQuestion.getcT()<tacheEnQuestion.getCharge() && tacheEnQuestion.getcT()>0) )
					 {
						 JPanel vert = new JPanel();
						 vert.setBackground(Color.green);
						 sousPanel.get(j).add(vert);
					 }
					 else
					{
						 JPanel rouge = new JPanel();
						 rouge.setBackground(Color.red);
						 sousPanel.get(j).add(rouge);
					 }
				 }
				 for(int j=index+1; j<criteres.size(); j++)
				 {
					 String nomTacheQuestion=criteres.get(j);
					 int indexTacheQuestion=indexOfByName(exec,nomTacheQuestion);
					 TacheP tacheEnQuestion=exec.get(indexTacheQuestion);
					 int periode=tacheEnQuestion.getPeriode();
					 if(tacheEnQuestion.getReveil()==i*Ordonnanceur.getQuantum() || (i*Ordonnanceur.getQuantum()>=periode && (i*Ordonnanceur.getQuantum())%periode==0 ))
					{
						 JPanel vert = new JPanel();
						 vert.setBackground(Color.green);
						 sousPanel.get(j).add(vert);
					 }
					 else
					 {
					 JPanel rouge = new JPanel();
					 rouge.setBackground(Color.red);
					 sousPanel.get(j).add(rouge);
					 }
				 }
			 }
			 //Sinon
			 else
			 {
				 for(int j=0; j<sousPanel.size(); j++)
				 {
					 String nomTacheQuestion=criteres.get(j);
					 int indexTacheQuestion=indexOfByName(exec,nomTacheQuestion);
					 TacheP tacheEnQuestion=exec.get(indexTacheQuestion);
					 int periode=tacheEnQuestion.getPeriode();
					 if(tacheEnQuestion.getReveil()==i*Ordonnanceur.getQuantum() || (i*Ordonnanceur.getQuantum()>=periode && (i*Ordonnanceur.getQuantum())%periode==0 && periode!=0))
					 {
						 JPanel vert = new JPanel();
						 vert.setBackground(Color.green);
						 sousPanel.get(j).add(vert);
					 }
					 else
					{
						 JPanel rouge = new JPanel();
						 rouge.setBackground(Color.red);
						 sousPanel.get(j).add(rouge);
					 }
				 }
			 }
			 
		 }

		 this.setLayout(new GridLayout(sousPanel.size()+1, 1));
		 //mainPan.add(sousPanel.get(0));
		 for(int k=0; k<sousPanel.size(); k++)
		 {
			mainPan.add(sousPanel.get(k));
		 }
		 
		 mainPan.add(new JTextArea("	Code couleur: Rouge:Tache bloquée, Bleu: Tache en cours d'execution, vert: Reveil de la Tache	"));
		 
	 }
	 
	 int indexOfByName(List<TacheP> liste, String nom)
	 {
		 for(int i=0; i<liste.size(); i++)
		 {
			 if(liste.get(i).getNom().equals(nom))
				 return i;
		 }
		 return -1;
	 }
}
