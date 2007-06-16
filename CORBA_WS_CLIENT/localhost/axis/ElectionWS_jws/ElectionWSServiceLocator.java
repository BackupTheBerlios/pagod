/**
 * ElectionWSServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package localhost.axis.ElectionWS_jws;

public class ElectionWSServiceLocator extends org.apache.axis.client.Service implements localhost.axis.ElectionWS_jws.ElectionWSService {

    // Use to get a proxy class for ElectionWS
    private final java.lang.String ElectionWS_address = "http://localhost:8080/axis/ElectionWS.jws";

    public java.lang.String getElectionWSAddress() {
        return ElectionWS_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String ElectionWSWSDDServiceName = "ElectionWS";

    public java.lang.String getElectionWSWSDDServiceName() {
        return ElectionWSWSDDServiceName;
    }

    public void setElectionWSWSDDServiceName(java.lang.String name) {
        ElectionWSWSDDServiceName = name;
    }

    public localhost.axis.ElectionWS_jws.ElectionWS getElectionWS() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(ElectionWS_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getElectionWS(endpoint);
    }

    public localhost.axis.ElectionWS_jws.ElectionWS getElectionWS(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            localhost.axis.ElectionWS_jws.ElectionWSSoapBindingStub _stub = new localhost.axis.ElectionWS_jws.ElectionWSSoapBindingStub(portAddress, this);
            _stub.setPortName(getElectionWSWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (localhost.axis.ElectionWS_jws.ElectionWS.class.isAssignableFrom(serviceEndpointInterface)) {
                localhost.axis.ElectionWS_jws.ElectionWSSoapBindingStub _stub = new localhost.axis.ElectionWS_jws.ElectionWSSoapBindingStub(new java.net.URL(ElectionWS_address), this);
                _stub.setPortName(getElectionWSWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        String inputPortName = portName.getLocalPart();
        if ("ElectionWS".equals(inputPortName)) {
            return getElectionWS();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://localhost:8080/axis/ElectionWS.jws", "ElectionWSService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("ElectionWS"));
        }
        return ports.iterator();
    }

}
