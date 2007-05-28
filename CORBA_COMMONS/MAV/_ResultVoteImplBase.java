package MAV;


/**
* MAV/_ResultVoteImplBase.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from ../idls/MAV.idl
* lundi 28 mai 2007 15 h 21 CEST
*/

public abstract class _ResultVoteImplBase extends org.omg.CORBA.portable.ObjectImpl
                implements MAV.ResultVote, org.omg.CORBA.portable.InvokeHandler
{

  // Constructors
  public _ResultVoteImplBase ()
  {
  }

  private static java.util.Hashtable _methods = new java.util.Hashtable ();
  static
  {
    _methods.put ("_get_idBV", new java.lang.Integer (0));
    _methods.put ("_get_nomBV", new java.lang.Integer (1));
    _methods.put ("_get_idCanton", new java.lang.Integer (2));
    _methods.put ("_get_canton", new java.lang.Integer (3));
    _methods.put ("_get_idCirc", new java.lang.Integer (4));
    _methods.put ("_get_circonscription", new java.lang.Integer (5));
    _methods.put ("_get_idDept", new java.lang.Integer (6));
    _methods.put ("_get_departement", new java.lang.Integer (7));
    _methods.put ("_get_idCandidat", new java.lang.Integer (8));
    _methods.put ("_get_nom", new java.lang.Integer (9));
    _methods.put ("_get_prenom", new java.lang.Integer (10));
    _methods.put ("_get_nbVote", new java.lang.Integer (11));
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
       case 0:  // MAV/ResultVote/_get_idBV
       {
         int $result = (int)0;
         $result = this.idBV ();
         out = $rh.createReply();
         out.write_long ($result);
         break;
       }

       case 1:  // MAV/ResultVote/_get_nomBV
       {
         String $result = null;
         $result = this.nomBV ();
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 2:  // MAV/ResultVote/_get_idCanton
       {
         int $result = (int)0;
         $result = this.idCanton ();
         out = $rh.createReply();
         out.write_long ($result);
         break;
       }

       case 3:  // MAV/ResultVote/_get_canton
       {
         String $result = null;
         $result = this.canton ();
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 4:  // MAV/ResultVote/_get_idCirc
       {
         int $result = (int)0;
         $result = this.idCirc ();
         out = $rh.createReply();
         out.write_long ($result);
         break;
       }

       case 5:  // MAV/ResultVote/_get_circonscription
       {
         String $result = null;
         $result = this.circonscription ();
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 6:  // MAV/ResultVote/_get_idDept
       {
         int $result = (int)0;
         $result = this.idDept ();
         out = $rh.createReply();
         out.write_long ($result);
         break;
       }

       case 7:  // MAV/ResultVote/_get_departement
       {
         String $result = null;
         $result = this.departement ();
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 8:  // MAV/ResultVote/_get_idCandidat
       {
         int $result = (int)0;
         $result = this.idCandidat ();
         out = $rh.createReply();
         out.write_long ($result);
         break;
       }

       case 9:  // MAV/ResultVote/_get_nom
       {
         String $result = null;
         $result = this.nom ();
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 10:  // MAV/ResultVote/_get_prenom
       {
         String $result = null;
         $result = this.prenom ();
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 11:  // MAV/ResultVote/_get_nbVote
       {
         int $result = (int)0;
         $result = this.nbVote ();
         out = $rh.createReply();
         out.write_long ($result);
         break;
       }

       default:
         throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
    }

    return out;
  } // _invoke

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:MAV/ResultVote:1.0"};

  public String[] _ids ()
  {
    return (String[])__ids.clone ();
  }


} // class _ResultVoteImplBase
