package MAV;


/**
* MAV/_SRVImplBase.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from ../idls/MAV.idl
* lundi 28 mai 2007 15 h 21 CEST
*/

public abstract class _SRVImplBase extends org.omg.CORBA.portable.ObjectImpl
                implements MAV.SRV, org.omg.CORBA.portable.InvokeHandler
{

  // Constructors
  public _SRVImplBase ()
  {
  }

  private static java.util.Hashtable _methods = new java.util.Hashtable ();
  static
  {
    _methods.put ("listeResultat", new java.lang.Integer (0));
    _methods.put ("listeCandidat", new java.lang.Integer (1));
    _methods.put ("authMAV", new java.lang.Integer (2));
    _methods.put ("authPersonne", new java.lang.Integer (3));
    _methods.put ("vote", new java.lang.Integer (4));
    _methods.put ("enregistrerVTR", new java.lang.Integer (5));
  }

  public org.omg.CORBA.portable.OutputStream _invoke (String $method,
                                org.omg.CORBA.portable.InputStream in,
                                org.omg.CORBA.portable.ResponseHandler $rh)
  {
    org.omg.CORBA.portable.OutputStream out = null;
    java.lang.Integer __method = (java.lang.Integer)_methods.get ($method);
    if (__method == null)
      throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);

    switch (__method.intValue ())
    {
       case 0:  // MAV/SRV/listeResultat
       {
         try {
           MAV.ResultVote $result[] = null;
           $result = this.listeResultat ();
           out = $rh.createReply();
           MAV.SRVPackage.ResultSeqHelper.write (out, $result);
         } catch (MAV.SRVPackage.InternalErrorException $ex) {
           out = $rh.createExceptionReply ();
           MAV.SRVPackage.InternalErrorExceptionHelper.write (out, $ex);
         }
         break;
       }

       case 1:  // MAV/SRV/listeCandidat
       {
         try {
           MAV.Candidat $result[] = null;
           $result = this.listeCandidat ();
           out = $rh.createReply();
           MAV.SRVPackage.CandidatSeqHelper.write (out, $result);
         } catch (MAV.SRVPackage.InternalErrorException $ex) {
           out = $rh.createExceptionReply ();
           MAV.SRVPackage.InternalErrorExceptionHelper.write (out, $ex);
         }
         break;
       }

       case 2:  // MAV/SRV/authMAV
       {
         try {
           int idMAV = in.read_long ();
           int idBV = in.read_long ();
           boolean $result = false;
           $result = this.authMAV (idMAV, idBV);
           out = $rh.createReply();
           out.write_boolean ($result);
         } catch (MAV.SRVPackage.InternalErrorException $ex) {
           out = $rh.createExceptionReply ();
           MAV.SRVPackage.InternalErrorExceptionHelper.write (out, $ex);
         }
         break;
       }

       case 3:  // MAV/SRV/authPersonne
       {
         try {
           int numInsee = in.read_long ();
           String password = in.read_string ();
           int idBV = in.read_long ();
           this.authPersonne (numInsee, password, idBV);
           out = $rh.createReply();
         } catch (MAV.SRVPackage.BadAuthentificationException $ex) {
           out = $rh.createExceptionReply ();
           MAV.SRVPackage.BadAuthentificationExceptionHelper.write (out, $ex);
         } catch (MAV.SRVPackage.AlreadyVoteException $ex) {
           out = $rh.createExceptionReply ();
           MAV.SRVPackage.AlreadyVoteExceptionHelper.write (out, $ex);
         } catch (MAV.SRVPackage.InternalErrorException $ex) {
           out = $rh.createExceptionReply ();
           MAV.SRVPackage.InternalErrorExceptionHelper.write (out, $ex);
         } catch (MAV.SRVPackage.IncorrectBVPersonException $ex) {
           out = $rh.createExceptionReply ();
           MAV.SRVPackage.IncorrectBVPersonExceptionHelper.write (out, $ex);
         }
         break;
       }

       case 4:  // MAV/SRV/vote
       {
         try {
           int numInsee = in.read_long ();
           String password = in.read_string ();
           int idCandidat = in.read_long ();
           int idBV = in.read_long ();
           boolean $result = false;
           $result = this.vote (numInsee, password, idCandidat, idBV);
           out = $rh.createReply();
           out.write_boolean ($result);
         } catch (MAV.SRVPackage.BadAuthentificationException $ex) {
           out = $rh.createExceptionReply ();
           MAV.SRVPackage.BadAuthentificationExceptionHelper.write (out, $ex);
         } catch (MAV.SRVPackage.AlreadyVoteException $ex) {
           out = $rh.createExceptionReply ();
           MAV.SRVPackage.AlreadyVoteExceptionHelper.write (out, $ex);
         } catch (MAV.SRVPackage.InternalErrorException $ex) {
           out = $rh.createExceptionReply ();
           MAV.SRVPackage.InternalErrorExceptionHelper.write (out, $ex);
         } catch (MAV.SRVPackage.IncorrectBVPersonException $ex) {
           out = $rh.createExceptionReply ();
           MAV.SRVPackage.IncorrectBVPersonExceptionHelper.write (out, $ex);
         }
         break;
       }

       case 5:  // MAV/SRV/enregistrerVTR
       {
         MAV.VTR vtr = MAV.VTRHelper.read (in);
         this.enregistrerVTR (vtr);
         out = $rh.createReply();
         break;
       }

       default:
         throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
    }

    return out;
  } // _invoke

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:MAV/SRV:1.0"};

  public String[] _ids ()
  {
    return (String[])__ids.clone ();
  }


} // class _SRVImplBase
