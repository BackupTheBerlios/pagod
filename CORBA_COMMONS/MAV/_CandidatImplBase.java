package MAV;


/**
* MAV/_CandidatImplBase.java .
* Generated by the IDL-to-Java compiler (portable), version "3.1"
* from ../idls/MAV.idl
* mardi 22 mai 2007 18 h 36 CEST
*/

public abstract class _CandidatImplBase extends org.omg.CORBA.portable.ObjectImpl
                implements MAV.Candidat, org.omg.CORBA.portable.InvokeHandler
{

  // Constructors
  public _CandidatImplBase ()
  {
  }

  private static java.util.Hashtable _methods = new java.util.Hashtable ();
  static
  {
    _methods.put ("_get_id", new java.lang.Integer (0));
    _methods.put ("_get_nom", new java.lang.Integer (1));
    _methods.put ("_get_prenom", new java.lang.Integer (2));
    _methods.put ("_get_description", new java.lang.Integer (3));
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
       case 0:  // MAV/Candidat/_get_id
       {
         int $result = (int)0;
         $result = this.id ();
         out = $rh.createReply();
         out.write_long ($result);
         break;
       }

       case 1:  // MAV/Candidat/_get_nom
       {
         String $result = null;
         $result = this.nom ();
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 2:  // MAV/Candidat/_get_prenom
       {
         String $result = null;
         $result = this.prenom ();
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 3:  // MAV/Candidat/_get_description
       {
         String $result = null;
         $result = this.description ();
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       default:
         throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
    }

    return out;
  } // _invoke

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:MAV/Candidat:1.0"};

  public String[] _ids ()
  {
    return (String[])__ids.clone ();
  }


} // class _CandidatImplBase
