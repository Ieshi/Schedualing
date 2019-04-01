package ordonnancement;

public class TacheP extends Tache {
	private int periode;
	
	public TacheP(int r, int c, int d, int prio, String nom, int p) {
		super(r, c, d, prio, nom);
		
		periode=p;
	}	
	public TacheP() {
		super();
		
		periode=0;
	}

	public int getPeriode() {
		return periode;
	}

	public void setPeriode(int periode) {
		this.periode = periode;
	}
	
	public void showTache() {
		String res=this.toString();
		res+="Période: " + periode + "\n";
		System.out.println(res);
	}

}
