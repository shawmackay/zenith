/*
 * Apollo : apollo.messaging.messages
 * 
 * 
 * InvocationMessage.java
 * Created on 24-Feb-2004
 * 
 * InvocationMessage
 *
 */

package org.jini.projects.zenith.messaging.messages;


/**
 * Implements a simple single Command Message format
 * 
 * @author calum
 */
public class InvocationMessage implements Message {
	/**
     * 
     */
    private static final long serialVersionUID = 3545517317929972787L;
    String methodName;
    String methodSignature;
	Object[] parameters;
	MessageHeader header;

	/*
	 * @see apollo.messaging.messages.Message#getHeader()
	 */
	public MessageHeader getHeader() {
		// TODO Complete method stub for getHeader
		return header;
	}

	/*
	 * @see apollo.messaging.messages.Message#getMessageContent()
	 */
	public Object getMessageContent() {
		// TODO Complete method stub for getMessageContent
		return getMessageContentAsString();
	}

	/*
	 * @see apollo.messaging.messages.Message#getMessageContentAsString()
	 */
	public String getMessageContentAsString() {
		StringBuffer b;
		b = new StringBuffer();
		int i = 0;
		try {
			
			b.append("MethodInvocation:" + methodName + "(");
			if (this.parameters != null)
				if (this.parameters.length > 0) {
					
					if (this.parameters.length > 1) {
						for (i = 0; i < parameters.length - 1; i++)
							b.append("[" + parameters[i].getClass().getName() + ":" + parameters[i] + "],");
					}				
					
					b.append("[" + parameters[i].getClass().getName());
					
					b.append(":" + parameters[i] + "]");
				}
			b.append(")");
		} catch (RuntimeException e) {
			// TODO Handle RuntimeException
			System.out.println(b.toString() +":" +i);
			e.printStackTrace();
		}
		return b.toString();
	}

	/**
	 * @param methodName
	 * @param parameters
	 */
	public InvocationMessage(MessageHeader header, String methodName, Object[] parameters, String methodSignature) {
		super();
		this.methodName = methodName;
		this.parameters = parameters;
		this.header = header;
		this.methodSignature = methodSignature;
	}

	/**
	 * @return Returns the method name used for invocation
	 */
	public String getMethodName() {
		return this.methodName;
	}

	/**
	 * @param methodName
	 *                   The methodName to set.
	 */
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	/**
	 * @return Returns the parameters.
	 */
	public Object[] getParameters() {
		return this.parameters;
	}

	/**
	 * @param parameters
	 *                   The parameters to set.
	 */
	public void setParameters(Object[] parameters) {
		this.parameters = parameters;
	}
	/**
	* getMethodSignature
	* @return String
	*/
	public String getMethodSignature(){
		return methodSignature;
	}
	/**
	* setMethodSignature
	* @param methodSignature
	*/
	public void setMethodSignature(String methodSignature){
		this.methodSignature=methodSignature;
	}

}
