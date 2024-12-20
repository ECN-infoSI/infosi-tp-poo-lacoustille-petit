package org.centrale.objet.WoE;

/**
 * Classe archer : personnage disposant de flèches
 *
 * @author Ulysse
 */
public class Archer extends Personnage implements Combatif, Jouable {

    /**
     * Nombre moyen de flèches de base en cas de génération aléatoire
     */
    private static final int NB_FLECHES_BASE_MOY = 20;
    /**
     * Variabilité du nombre de flèche de base en cas de génération aléatoire
     * Tq MOY-VAR <= nbFleches <= MOY+VAR
     */
    private static final int NB_FLECHES_BASE_VAR = 10;

    /**
     * Génération aléatoire du nombre de flèches
     * 
     * @return nbFlèches random
     */
    private final int nbFlechesBaseRandom() {
        return getRandom(2 * NB_FLECHES_BASE_VAR + 1) - NB_FLECHES_BASE_VAR + NB_FLECHES_BASE_MOY;
    }

    /**
     * Nombre de flèches possédées par l'Archer
     */
    private int nbFleches;

    /**
     * Initialise un archer
     *
     * @param n       nom
     * @param pV      points de vie
     * @param dA      distance d'attaque
     * @param pPar    points de parade
     * @param paAtt   probabilité de réussir une attaque
     * @param paPar   probabilité de réussir une parade
     * @param dMax    distance d'attaque maximale
     * @param vitesse vitesse de déplacement
     * @param p       position (Point2D)
     * @param nbFl    nombre de flèches
     */
    public Archer(String n, int pV, int dA, int pPar, int paAtt, int paPar, int dMax, int vitesse, Point2D p,
            int nbFl) {
        super(n, pV, dA, pPar, paAtt, paPar, dMax, vitesse, p);
        this.nbFleches = nbFl;
    }

    /**
     * Crée un archer identique à l'archer a
     *
     * @param a Archer à copier
     */
    public Archer(Archer a) {
        super(a);
        this.nbFleches = a.getNbFleches();
        this.setDistAttMax(this.getRandom(4) + 2);
    }

    /**
     * Crée un Archer aléatoire au point (x, y) donné
     *
     * @param x abscisse
     * @param y ordonnée
     */
    public Archer(int x, int y) {
        super(x, y);
        this.nbFleches = nbFlechesBaseRandom();
        this.setDistAttMax(this.getRandom(4) + 2);
    }

    /**
     * Crée un Archer aléatoire au point p donné
     *
     * @param p point
     */
    public Archer(Point2D p) {
        super(p);
        this.nbFleches = nbFlechesBaseRandom();
        this.setDistAttMax(this.getRandom(4) + 2);
    }

    /**
     * Génère un archer aléatoire
     */
    public Archer() {
        super();
        this.nbFleches = nbFlechesBaseRandom();
        this.setDistAttMax(this.getRandom(4) + 2);
    }

    /**
     *
     * @return nombre de flèches
     */
    public int getNbFleches() {
        return this.nbFleches;
    }

    /**
     * Modifie le nombre de flèches d'un archer
     *
     * @param nbFleches nombre de fleches
     */
    public void setNbFleches(int nbFleches) {
        this.nbFleches = nbFleches;
    }

    /**
     * Affiche des informations sur l'archer
     */
    @Override
    public void affiche() {
        super.affiche();
        System.out.print(this.getNom() + " maîtrise l'archerie et a " + getNbFleches() + " flèches.\n");
        // Fenetre.addMessage("Cet archer a " + getNbFleches() + " flèches.");
    }

    /**
     * Utilise une flèche
     */
    public void utilFleche() {
        setNbFleches(getNbFleches() - 1);
    }

    /**
     * Combattre une créature désignée
     *
     * TODO: Subdiviser la fonction
     * 
     * @param c Créature désignée
     */
    @Override
    public void combattre(Creature c) {
        String msg; // Message d'attaque
        int dgts; // dégats
        double d = this.distance(c);
        if (d <= 1) {
            int pAtt = this.getPageAtt();
            Epee arme = this.getArme();

            if (arme != null) {
                pAtt += (int) (100 - pAtt) * arme.getPageAtt(); // Calcul des stats d'attaque avec arme
            }

            if (this.lanceDe(pAtt)) {
                int dAtt = this.getDegAtt();

                if (arme != null) {
                    dAtt *= (5 + arme.getDegAtt()) / 10.; // Calcul des dégats avec arme
                }

                int pPar = c.getPagePar();
                boolean cIsPerso = c instanceof Personnage;
                if (cIsPerso) {
                    Epee cArme = ((Personnage) c).getArme();
                    if (cArme != null) {
                        pPar += (int) (100 - pAtt) * this.getArme().getPagePar();
                    }
                }

                if (c.lanceDe(pPar)) {
                    int dPar = c.getPtPar();
                    if (cIsPerso) {
                        Epee cArme = ((Personnage) c).getArme();
                        if (cArme != null) {
                            dPar *= (5 + cArme.getPtPar()) / 10.;
                        }
                    }
                    dgts = Math.max(0, dAtt - dPar);
                    if (dgts == 0) {
                        msg = "Le coup atteint sa cible mais est complètement bloqué.";
                    } else {
                        msg = "Le coup est bloqué mais inflige tout de même " + dgts + " dégats !";
                        c.setPtVie(Math.max(0, c.getPtVie() - dgts));
                    }
                } else {
                    dgts = dAtt;
                    msg = "Le coup touche et inflige " + dgts + " dégats !";
                    c.setPtVie(Math.max(0, c.getPtVie() - dgts));
                }
            } else {
                msg = "Le coup a raté.";
            }
        } else if (d <= this.getDistAttMax()) {
            if (this.getNbFleches() == 0) {
                msg = "Vous n'avez plus de flèches !";
            } else {
                int pAtt = this.getPageAtt();
                this.utilFleche();
                if (this.lanceDe(pAtt)) {
                    dgts = this.getDegAtt();
                    c.setPtVie(Math.max(0, c.getPtVie() - dgts));
                    msg = "La flèche touche et inflige " + dgts + " dégats !";
                } else {
                    msg = "La flèche a raté.";
                }
            }
        } else {
            msg = "La cible est trop loin.";
        }
        System.out.println(msg);
        Fenetre.addMessage(msg);
    }

    /**
     * TODO: OSKOUR C MOCHE
     * 
     * @see Deplacable
     */
    public void deplace(Objet[][] grille) {
    }

    /**
     * TODO: OSKOUR C MOCHE
     * 
     * @see Deplacable
     */
    public void deplace(Objet[][] grille, Point2D p) {
    }
}
