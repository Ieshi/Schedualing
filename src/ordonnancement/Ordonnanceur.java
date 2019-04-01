package ordonnancement;

import java.util.ArrayList;
import java.util.List;

public class Ordonnanceur {
	private Tache tacheCourante;
	private List<TacheP> listTachePreteP= new ArrayList<TacheP>();
	private List<TacheP> listTacheP;
	private List<TacheP> listTachePBloque= new ArrayList<TacheP>();
	private List<TacheAP> listTacheAPBloque= new ArrayList<TacheAP>();
	private List<TacheAP> listTacheAP;
	private List<TacheAP> listTachePreteAP= new ArrayList<TacheAP>();
	

	private List<TacheP> exec=new ArrayList<TacheP>();
	
	
	private int ppcm;
	private static int quantum=10;
	private int temps=0;

	public String toString() {
		String res="Tache courante: " + tacheCourante +"\n";
		res+="List tache P: " + listTacheP +"\n";
		res+="List tache prete P: " + listTachePreteP +"\n";
		res+="List tache bloquées P: " + listTachePBloque +"\n";
		res+="List tache AP: " + listTacheAP +"\n";
		res+="List tache prete AP: " + listTachePreteAP +"\n";
		res+="List tache bloquées AP: " + listTacheAPBloque +"\n";
		
		return res;
	}
	
	public Ordonnanceur(List<TacheP> listTacheP, List<TacheAP> listTacheAP){
		this.listTacheAP=listTacheAP;
		this.listTacheP=listTacheP;
		//Initialisation des liste des taches prêtes et bloquée
		initListesTache();
		setPpcm(calculerPPCMListe());
	}
	
	
	private List<TacheP> addToListP(List<TacheP> listeTache, TacheP T) {
		
		int k=0;
		boolean ajoute=false;
		List<TacheP> listTmp=new ArrayList<TacheP>();
		while(k<listeTache.size()) {
			if(T.getPriorite()>listeTache.get(k).getPriorite())
			{
				listTmp.add(listeTache.get(k));
				k++;
			}
			else if(!ajoute)
			{
				listTmp.add(T);
				ajoute=true;
			}
			else
			{
				listTmp.add(listeTache.get(k));
				k++;
			}

		}
		if(!ajoute)
			listTmp.add(T);
		return listTmp;
	}
	
	private List<TacheAP> addToListAP(List<TacheAP> listeTache, TacheAP T) {
		
		int k=0;
		boolean ajoute=false;
		List<TacheAP> listTmp=new ArrayList<TacheAP>();
		while(k<listeTache.size()) {
			if(T.getPriorite()>listeTache.get(k).getPriorite())
			{
				listTmp.add(listeTache.get(k));
				k++;
			}
			else if(!ajoute)
			{
				listTmp.add(T);
				ajoute=true;
			}
			else
			{
				listTmp.add(listeTache.get(k));
				k++;
			}

		}
		if(!ajoute)
			listTmp.add(T);
		return listTmp;
	}
	
	int calculerPPCMListe() {
		int[] listPeriodes = new int[listTacheP.size()];
		
		for(int i=0; i<listTacheP.size(); i++) {
			listPeriodes[i]=listTacheP.get(i).getPeriode();
		}
		ppcm=calculerPPCM(listPeriodes[0],listPeriodes[1]);
		for(int i=2; i<listPeriodes.length; i++)
		{
			ppcm=calculerPPCM(ppcm,listPeriodes[i]);
		}
		return ppcm;
		
	}
	
	int calculerPPCM(int a, int b) {
		if(a%b==0 && a>=b)
			return a;
		else if(b%a==0 && a<=b)
			return b;
		else {
			int reste=0;
			
			if(a>b)
				reste=a%b;
			else
				reste=b%a;
			
			int nb1=a;
			int nb2=b;
			int preReste=reste;
			
			while(reste!=0) {
				preReste=reste;
				nb1=nb2;
				nb2=reste;
				reste=nb2%nb1;
			}
			
			return a*b/preReste;
		}
		
	}
	
	void executerTacheParQuantum()
	{		
		//Comme si on utilisait un serveur à temps d'oisivetée
		if(listTachePreteP.size()!=0) //Si la liste des tache périodique n'est pas vide
		{
			listTachePreteP.get(0).setcT(listTachePreteP.get(0).getcT()-getQuantum());
			tacheCourante=listTachePreteP.get(0);
		}
		
		else if(listTachePreteAP.size()!=0) //S'il n'y a pas de tache périodique on passe aux tache AP
		{
			listTachePreteAP.get(0).setcT(listTachePreteAP.get(0).getcT()-getQuantum());
			tacheCourante=listTachePreteAP.get(0);
		}
		
		else //Sinon il n'y a pas de tache a faire et on passe 
			tacheCourante=null;
	}
	
	void miseAjourListe(String politique)
	{
		if(listTachePreteP.size()!=0)
		{
			//La liste des taches pretes
			if(tacheCourante.getcT()==0)//Plus de charge
			{
				//modification de son état et charge
				listTachePreteP.get(0).setEtat("inactive");
				listTachePreteP.get(0).setcT(listTachePreteP.get(0).getCharge());
				
				listTachePBloque.add(listTachePreteP.get(0)); //On ajoute la tache a la liste de celles bloquée
				listTachePreteP.remove(tacheCourante);//Ici la tache est vraiment supprimée
				
			}
			
			else //Il reste toujours de la charge
			{
				if(politique.equals("RR"))
				{
					listTachePreteP.add(listTachePreteP.get(0));
					listTachePreteP.remove(0);
				}
			}
		}
		if(listTachePreteAP.size()!=0)
		{
			if(listTachePreteAP.get(0).getcT()==0) 
			{
				listTachePreteAP.get(0).setEtat("inactive");
				listTacheAPBloque.add(listTachePreteAP.get(0)); //On ajoute la tache a la liste de celles bloquée
				listTachePreteAP.remove(0);
			}
		}

		//Liste des taches bloquées avec la liste des taches prêtes
		int i=0;
		while(i<listTachePBloque.size())
		{		
			if((temps>=listTachePBloque.get(i).getPeriode() && (temps-listTachePBloque.get(i).getReveil())%listTachePBloque.get(i).getPeriode()==0) || temps==listTachePBloque.get(i).getReveil())
			{

				//On modifie son etat a reveillee
				listTachePBloque.get(i).setEtat("reveillee");
				if(politique.equals("RR")||politique.equals("FIFO"))
				{
					listTachePreteP.add(listTachePBloque.get(i));
				}
				else if(politique.equals("SJF"))
				{
					if(listTachePreteP.size()!=0)
					{
						
						TacheP tache=listTachePreteP.get(0);
						listTachePreteP=addToListP(listTachePreteP, listTachePBloque.get(i));
						System.out.println(listTachePreteP.indexOf(tache));
						//On supprime la tache courante de la liste
						listTachePreteP.remove(tache);
						//Et on la réajoute à la tête de la liste
						List<TacheP> tmp=new ArrayList<TacheP>();
						tmp.add(tache);
						for(int l=0;l<listTachePreteP.size();l++)
						{
							tmp.add(listTachePreteP.get(l));
						}
						listTachePreteP=tmp;
						System.out.println(listTachePreteP);
					}
					else
						listTachePreteP=addToListP(listTachePreteP, listTachePBloque.get(i));
					
				}
				else 
				{
					listTachePreteP=addToListP(listTachePreteP, listTachePBloque.get(i));
				}
				
				listTachePBloque.remove(listTachePBloque.get(i));
			}
			else
				i++;
			
		}
		i=0;
		while(i<listTacheAPBloque.size())
		{
			if(temps%listTachePBloque.get(i).getReveil()==temps)
			{
				//On modifie son etat a reveillee
				listTacheAPBloque.get(i).setEtat("reveillee");
				listTachePreteAP.add(listTacheAPBloque.get(i));
				listTacheAPBloque.remove(listTacheAPBloque.get(i));
			}
		}
	}
	
	void executionAlgo(String politique) {
		while(temps<this.getPpcm())
		{	
			if(politique.equals("RR") || politique.equals("DM") || politique.equals("EDF"))
			{
				executerTacheParQuantum();
			}
			
			else if(politique.equals("EDF"))
			{
				executerTacheParQuantum();
				//Il faut mettre a jour la liste des tâche a chaque fois
				//On peut donc mettre a jour les priorités et réinitialiser les listes à chaque fois
				
				int listDeadLineT[]= new int[listTacheP.size()];
				
				//On récupère les deadLines
				for(int i=0; i<listTacheP.size(); i++)
				{
					//Normalement on devrait calculer d=D-t(Date de la prochaine deadline - t)
					//Mais on peu calculer abs(t-D)(Temps actuel-deadline)
					listDeadLineT[i]=Math.abs(temps-listTacheP.get(i).getDeadLine());
				}
				//On les trie en ordre croissant
				for(int i=0; i<listDeadLineT.length; i++)
				{
					//On défini le min une première fois
					int max=listDeadLineT[i];
					int indiceMax=i;
					//On cherche le min dans le reste de la liste
					for(int j=i+1; j<listDeadLineT.length; j++)
					{
						if(max<listDeadLineT[j])
						{
							max=listDeadLineT[j];
							indiceMax=j;
						}
					}
					//On permute les cases i et indiceMin
					if (indiceMax!=i)
					{
						int tmp=listDeadLineT[i];
						listDeadLineT[i]=max;
						listDeadLineT[indiceMax]=tmp;
					}
				}
				
				for(int i=0; i<listTacheP.size();i++)
				{
					int j=0;
					boolean trouv=false;
					while(j<listTacheP.size() && !trouv)
					{
						if((temps-listTacheP.get(i).getDeadLine())==listDeadLineT[j])
						{
							trouv=true;
							listTacheP.get(i).setPriorite(j+1);
						}
						j++;
					}
				}

				listTachePreteP.clear();
				initListesTache();
			}
			//FIFO ou SJF
			else
			{
				executerTacheParQuantum();
			}
			if(tacheCourante==null)
				exec.add(new TacheP());
			else
			{
				if(listTachePreteP.size()!=0)//Si on vient d'executer une tache périodique
				{
					exec.add(listTachePreteP.get(0));
				}
				else//Sinon c'est une tache apériodique
				{
					TacheP tache = new TacheP(tacheCourante.getReveil(), tacheCourante.getCharge(), tacheCourante.getDeadLine(), tacheCourante.getPriorite(), tacheCourante.getNom(), 0);
					exec.add(tache);
				}
				
			}

			System.out.println("Temps: \n" + temps);
			temps+=getQuantum();
			System.out.println("Tache courante: \n" + tacheCourante);

			miseAjourListe(politique);
			
			System.out.println("Liste de taches périodiques dispo: \n" +listTachePreteP);
			System.out.println("Liste de taches périodiques bloquées: \n" +listTachePBloque);
			System.out.println("==================================\n");
			
		}
	}
	
	void initListesTache() {

		for(int i=0; i<listTacheP.size(); i++) {
			if(listTacheP.get(i).getReveil()==0)
				listTachePreteP=addToListP(listTachePreteP,listTacheP.get(i));
			else
				listTachePBloque=addToListP(listTachePBloque,listTacheP.get(i));
		}
		
		for(int i=0; i<listTacheAP.size(); i++) {
			if(listTacheAP.get(i).getReveil()==0)
				listTachePreteAP=addToListAP(listTachePreteAP,listTacheAP.get(i));
			else
				listTacheAPBloque=addToListAP(listTacheAPBloque,listTacheAP.get(i));
		}
		
	}
	
	void politiqueRR() {
		//Ne necessite pas une réorganisation de la liste des tâches
		executionAlgo("RR");
	}
	
	void politiqueDM() {

		//Necessite une réorganisation de la liste des tâches suivant la deadLine on va donc modifier 
		//les priorités des taches suivant la deadline et réinitialiser la liste
		int listDeadLine[]= new int[listTacheP.size()];
		
		//On récupère les deadLines
		for(int i=0; i<listTacheP.size(); i++)
		{
			listDeadLine[i]=listTacheP.get(i).getDeadLine();
		}
		//On les trie en ordre croissant
		for(int i=0; i<listDeadLine.length; i++)
		{
			//On défini le min une première fois
			int min=listDeadLine[i];
			int indiceMin=i;
			//On cherche le min dans le reste de la liste
			for(int j=i+1; j<listDeadLine.length; j++)
			{
				if(min>listDeadLine[j])
				{
					min=listDeadLine[j];
					indiceMin=j;
				}
			}
			//On permute les cases i et indiceMin
			if (indiceMin!=i)
			{
				int tmp=listDeadLine[i];
				listDeadLine[i]=min;
				listDeadLine[indiceMin]=tmp;
			}
		}
		
		for(int i=0; i<listTacheP.size();i++)
		{
			int j=0;
			boolean trouv=false;
			while(j<listTacheP.size() && !trouv)
			{
				if(listTacheP.get(i).getDeadLine()==listDeadLine[j])
				{
					trouv=true;
					listTacheP.get(i).setPriorite(j+1);
				}
				j++;
			}
		}
		
		//Et on réinitialise les listes
		listTachePreteP.clear();
		initListesTache();
		executionAlgo("DM");
	}
	
	void politiqueFIFO() {

		//Ne necessite pas une réinitialisation de la liste des taches
		executionAlgo("FIFO");
	}
	
	void politiqueSJF() {

		//Necessite une réorganisation de la liste des tâches suivant la charge on va donc modifier 
		//les priorités des taches suivant la charge et réinitialiser la liste
		int listCharge[] = new int[listTacheP.size()];
		
		//On récupère les deadLines
		for(int i=0; i<listTacheP.size(); i++)
		{
			listCharge[i]=listTacheP.get(i).getCharge();
		}
		//On les trie en ordre croissant
		for(int i=0; i<listCharge.length; i++)
		{
			
			int min=listCharge[i]; //Ici on donne une valeur initiale a min pour commencer la comparaison
			int indiceMin=i;//Ici on sauvegarde l'indice du minimum
			
			for(int j=i+1; j<listCharge.length; j++) //On fait le tour de touuuut le tableau
			{
				if(min>listCharge[j])//Si on rencontre une valeur plus faible
				{
					min=listCharge[j];//On l'enregistre ici
					indiceMin=j;//Et on enregistre son indice
				}
			}//Lorsqu'on sort de cette boucle for on aura le minimum du tableau de l'indice i+1 jusqu'a la fin
			
			if (indiceMin!=i)
			{
				//Si les deux indices: celui du min et le courant sont différents
				//ça veut dire que le minimum a changé parce qu'au début on l'a initialisé à i
				//Et donc on trouvé une valeur plus faible
				//Ainsi on doit permuter cette valeur avec l'ancienne
				int tmp=listCharge[i];//On stock l'ancienne valeur dans une variable intermédiaire
				listCharge[i]=min;//On affecte la nouvelle valeur (celle du vrai min)
				listCharge[indiceMin]=tmp;//Et on affecte a la case avec laquelle on a permuté la valeur de l'ancienne case
			}
		}
		
		for(int i=0; i<listTacheP.size();i++)
		{
			int j=0;
			boolean trouv=false;
			while(j<listTacheP.size() && !trouv)
			{
				if(listTacheP.get(i).getCharge()==listCharge[j])
				{
					trouv=true;
					listTacheP.get(i).setPriorite(j+1);
				}
				j++;
			}
		}
		
		//Et on réinitialise les listes

		listTachePreteP.clear();
		initListesTache();
		executionAlgo("SJF");
	}
	
	void politiqueEDF() {
		executionAlgo("EDF");
	}
	
	public Tache getTacheCourante() {
		return tacheCourante;
	}

	public void setTacheCourante(Tache tacheCourante) {
		this.tacheCourante = tacheCourante;
	}
	
	public List<TacheP> getListTachePreteP() {
		return listTachePreteP;
	}

	public void setListTachePreteP(List<TacheP> listTachePreteP) {
		this.listTachePreteP = listTachePreteP;
	}

	public List<TacheP> getListTacheP() {
		return listTacheP;
	}

	public void setListTacheP(List<TacheP> listTacheP) {
		this.listTacheP = listTacheP;
	}

	public List<TacheP> getListTachePBloque() {
		return listTachePBloque;
	}

	public void setListTachePBloque(List<TacheP> listTachePBloque) {
		this.listTachePBloque = listTachePBloque;
	}

	public List<TacheAP> getListTacheAPBloque() {
		return listTacheAPBloque;
	}

	public void setListTacheAPBloque(List<TacheAP> listTacheAPBloque) {
		this.listTacheAPBloque = listTacheAPBloque;
	}

	public List<TacheAP> getListTachePreteAP() {
		return listTachePreteAP;
	}

	public void setListTachePreteAP(List<TacheAP> listTachePreteAP) {
		this.listTachePreteAP = listTachePreteAP;
	}

	public int getPpcm() {
		return ppcm;
	}

	public void setPpcm(int ppcm) {
		this.ppcm = ppcm;
	}

	public List<TacheAP> getListTacheAP() {
		return listTacheAP;
	}

	public int getTemps() {
		return temps;
	}

	public void setTemps(int temps) {
		this.temps = temps;
	}

	public List<TacheP> getExec() {
		return exec;
	}

	public void setExec(List<TacheP> exec) {
		this.exec = exec;
	}

	public static int getQuantum() {
		return quantum;
	}

	public static void setQuantum(int quantum) {
		Ordonnanceur.quantum = quantum;
	}

	

	

	
	
}
