
package br.com.manchestercity.automacao;

import java.io.StringWriter;
import java.lang.reflect.Method;

import javax.xml.namespace.QName;

import org.apache.axis.Message;
import org.apache.axis.MessageContext;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.SerializationContext;
import org.apache.axis.encoding.ser.BeanSerializer;
import org.apache.axis.server.AxisServer;
import org.xml.sax.helpers.AttributesImpl;

public class AxisObjectUtil {

	private static final String SOAP_START = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"><soapenv:Header /><soapenv:Body>";
	private static final String SOAP_START_XSI = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><soapenv:Header /><soapenv:Body>";
	private static final String SOAP_END = "</soapenv:Body></soapenv:Envelope>";

	public static String serializeAxisObject(final Object obj, final boolean removeNamespaces, final boolean prettyPrint) throws AxisObjectException {
		final StringWriter outStr = new StringWriter();
		final TypeDesc typeDesc = getAxisTypeDesc(obj);
		QName qname = typeDesc.getXmlType();
		String lname = qname.getLocalPart();

		if (lname.startsWith(">") && lname.length() > 1)
			lname = lname.substring(1);

		qname = removeNamespaces ? new QName(lname) : new QName(qname.getNamespaceURI(), lname);
		final AxisServer server = new AxisServer();
		final BeanSerializer ser = new BeanSerializer(obj.getClass(), qname, typeDesc);
		final SerializationContext ctx = new SerializationContext(outStr, new MessageContext(server));
		ctx.setSendDecl(false);
		ctx.setDoMultiRefs(false);
		ctx.setPretty(prettyPrint);

		try {
			ser.serialize(qname, new AttributesImpl(), obj, ctx);
		}
		catch (final Exception e) {
			throw new AxisObjectException("Unable to serialize object " + obj.getClass().getName(), e);
		}

		String xml = outStr.toString();

		if (removeNamespaces) {
			xml = xml.replaceAll(" xmlns[:=].*?\".*?\"", "").replaceAll(" xsi:type=\".*?\"", "");
		}

		return ( xml );
	}

	public static TypeDesc getAxisTypeDesc(final Object obj) throws AxisObjectException {
		final Class<? extends Object> objClass = obj.getClass();
		try {
			final Method methodGetTypeDesc = objClass.getMethod("getTypeDesc", new Class[] {});
			final TypeDesc typeDesc = (TypeDesc) methodGetTypeDesc.invoke(obj, new Object[] {});
			return ( typeDesc );
		}
		catch (final Exception e) {
			throw new AxisObjectException("Unable to get Axis TypeDesc for " + objClass.getName(), e);
		}
	}

	public static Object deserializeObject(String xml, Class<?> clazz) throws AxisObjectException {
		assert xml != null : "xml != null";
		assert clazz != null : "clazz != null";

		Object result = null;
		try {
			Message message = new Message(SOAP_START + xml + SOAP_END);
			result = message.getSOAPEnvelope().getFirstBody().getObjectValue(clazz);
		}
		catch (Exception e) {
			try {
				Message message = new Message(SOAP_START_XSI + xml + SOAP_END);
				result = message.getSOAPEnvelope().getFirstBody().getObjectValue(clazz);
			}
			catch (Exception e1) {
				throw new AxisObjectException(e1);
			}
		}
		return result;
	}
}
