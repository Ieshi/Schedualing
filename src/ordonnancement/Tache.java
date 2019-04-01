package ordonnancement;

public abstract class Tache {
	private int reveil, charge, deadLine, priorite, cT, dT;
	private String nom, etat;
	
	public Tache(int r, int c, int d, int prio, String nom) {
		reveil=r;
		charge=c;
		deadLine=d;
		priorite=prio;
		
		this.nom=nom;
		
		cT=charge;
		dT=deadLine;
		
		if(reveil==0) {
			etat="reveillee";
		}
		else
			etat="inactive";
	}	
	public Tache() {
		reveil=0;
		charge=0;
		deadLine=0;
		priorite=0;
		
		this.nom="null";
		
		cT=charge;
		dT=deadLine;
		etat="null";
	}
	
	public String toString() {
		String res="Nom: " + nom +"\n";
		res+="Etat: " + etat + "\n";
		/*res+="Charge: " + charge + "\n";
		res+="DeadLine: " + deadLine + "\n";
		res+="Priorité: " + priorite + "\n";
		res+="Charge restante: " + cT + "\n";
		res+="reveil: " + reveil + "\n";
		res+="DeadLine restante: " + dT + "\n";*/
		return res;
	}
	
	public void showTache() {
		System.out.println(this.toString());
	}

	public int getReveil() {
		return reveil;
	}

	public void setReveil(int reveil) {
		this.reveil = reveil;
	}

	public int getCharge() {
		return charge;
	}

	public void setCharge(int charge) {
		this.charge = charge;
	}

	public int getDeadLine() {
		return deadLine;
	}

	public void setDeadLine(int deadLine) {
		this.deadLine = deadLine;
	}

	public int getPriorite() {
		return priorite;
	}

	public void setPriorite(int priorite) {
		this.priorite = priorite;
	}

	public int getcT() {
		return cT;
	}

	public void setcT(int cT) {
		this.cT = cT;
	}

	public int getdT() {
		return dT;
	}

	public void setdT(int dT) {
		this.dT = dT;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getEtat() {
		return etat;
	}

	public void setEtat(String etat) {
		this.etat = etat;
	}
	
	
}
