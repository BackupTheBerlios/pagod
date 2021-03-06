package MAV.SRVPackage;


/**
* MAV/SRVPackage/BadAuthentificationHelper.java .
* Generated by the IDL-to-Java compiler (portable), version "3.1"
* from ../idls/MAV.idl
* mardi 27 mars 2007 18 h 57 CEST
*/

abstract public class BadAuthentificationHelper
{
  private static String  _id = "IDL:MAV/SRV/BadAuthentification:1.0";

  public static void insert (org.omg.CORBA.Any a, MAV.SRVPackage.BadAuthentification that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static MAV.SRVPackage.BadAuthentification extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  private static boolean __active = false;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      synchronized (org.omg.CORBA.TypeCode.class)
      {
        if (__typeCode == null)
        {
          if (__active)
          {
            return org.omg.CORBA.ORB.init().create_recursive_tc ( _id );
          }
          __active = true;
          org.omg.CORBA.StructMember[] _members0 = new org.omg.CORBA.StructMember [1];
          org.omg.CORBA.TypeCode _tcOf_members0 = null;
          _tcOf_members0 = org.omg.CORBA.ORB.init ().create_string_tc (0);
          _members0[0] = new org.omg.CORBA.StructMember (
            "reason",
            _tcOf_members0,
            null);
          __typeCode = org.omg.CORBA.ORB.init ().create_exception_tc (MAV.SRVPackage.BadAuthentificationHelper.id (), "BadAuthentification", _members0);
          __active = false;
        }
      }
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static MAV.SRVPackage.BadAuthentification read (org.omg.CORBA.portable.InputStream istream)
  {
    MAV.SRVPackage.BadAuthentification value = new MAV.SRVPackage.BadAuthentification ();
    // read and discard the repository ID
    istream.read_string ();
    value.reason = istream.read_string ();
    return value;
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, MAV.SRVPackage.BadAuthentification value)
  {
    // write the repository ID
    ostream.write_string (id ());
    ostream.write_string (value.reason);
  }

}
