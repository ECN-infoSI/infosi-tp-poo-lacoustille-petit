package org.centrale.objet.WoE;

/**
 * Super-classe pour les objets de WoE
 *
 * @author timot
 */
public abstract class Objet extends Entite {
    
    public Objet(Objet o) {
        super(o.getPos(), false);
    }
    
    public Objet(Point2D pos) {
        super(pos, false);
    }
    
    public Objet() {
        super(new Point2D(), false);
    }

    public abstract void recuperer(Personnage p);
}
