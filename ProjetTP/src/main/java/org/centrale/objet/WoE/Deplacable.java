package org.centrale.objet.WoE;

/**
 *
 * @author Ulysse
 */
public interface Deplacable {
    public void deplace(Creature[][] grille);
    public void deplace(Creature[][] grille, Point2D p);
}