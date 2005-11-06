/*
 * Projet PAGOD
 * 
 * $Id: ActivityEndState.java,v 1.3 2005/11/06 14:23:51 yak Exp $
 */

package pagod.wizard.control.states;

import java.util.List;

import javax.swing.JOptionPane;

import pagod.common.model.Activity;
import pagod.common.model.Product;
import pagod.common.model.Step;
import pagod.utils.LanguagesManager;
import pagod.wizard.control.ActivityScheduler;
import pagod.wizard.control.ApplicationManager;

/**
 * @author yak
 * 
 */
public class ActivityEndState extends ActivityState
{

    /**
     * @param activityScheduler
     * @param activity
     */
    public ActivityEndState(ActivityScheduler activityScheduler,
                            Activity activity)
    {
        super(activityScheduler, activity);
        // TODO Corps de constructeur généré automatiquement
    }

    /**
     * @param activityScheduler
     * @param activity
     * @param iCurrentStep
     */
    public ActivityEndState(ActivityScheduler activityScheduler,
                            Activity activity, int iCurrentStep)
    {
        super(activityScheduler, activity, iCurrentStep);
        // on test les postconditions
      preCondChecker pdcTemp = new preCondChecker ();
      //on verifie les precondition
        if (pdcTemp.checkPreCond())
        {

            this.terminate();

        }
        else
        {
            this.previous();
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see pagod.wizard.control.states.ActivityState#previous()
     */
    public void previous()
    {
        // on teste le nombre step
        switch (this.stepList.size())
        {
            // si on est il n'y a pas de step on revient a l'acitivité
            // presentation
            case 0:
                this.activityScheduler
                        .setActivityState(new ActivityPresentationState(
                                this.activityScheduler, this.activity));
                break;
            // sinon on revient au dernier step
            default:
                this.activityScheduler.setActivityState(new LastStepState(
                        this.activityScheduler, this.activity));
                break;

        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see pagod.wizard.control.states.ActivityState#next()
     */
    public void next()
    {
        // TODO Supprimer ceci est un test
        System.err
                .println("ActivityEndState : pas de next possible sur cet etat");

    }

    /*
     * (non-Javadoc)
     * 
     * @see pagod.wizard.control.states.ActivityState#terminate()
     */
    public void terminate()
    {
        // les préconditions sont vérifiées, on peu revinir a la présentation
        // on change d'état pour null
        this.activityScheduler.setActivityState(null);
        // on effectue un terminante sur l'application manager
        ApplicationManager.getInstance().manageRequest(
                ApplicationManager.Request.TERMINATE_ACTIVITY);
    }

    private class preCondChecker
    {

        public preCondChecker()
        {

        }

        public boolean checkPreCond()
        {
            // on construit le message avec tout le produits
            String sPrecondMessage = new String ("");
            String sPrecondTitle = LanguagesManager.getInstance().getString("postCondTitle");
            sPrecondMessage += LanguagesManager.getInstance().getString("postCondMessage")+ "\n";
           // pour chaque produit on ajoute le nom a la liste des produits
            for (Product productTemp : ActivityEndState.this.activity.getOutputProducts())
            {
                //ajout du nom au message
                sPrecondMessage +=  productTemp.getName()+"\n";
            }
            
            
            
            int iRetour = JOptionPane.showConfirmDialog(ActivityEndState.this.activityScheduler.getMfPagod(), 
                    sPrecondMessage,sPrecondTitle, JOptionPane.YES_NO_OPTION);
            // en fonction du retour un renvoir le bon booleen
            switch (iRetour)
            {
                case JOptionPane.NO_OPTION:
                    return false;
                case JOptionPane.YES_OPTION:
                    return true;
                default:
                    return false;

            }

        }

    }
}
