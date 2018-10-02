
package br.com.manchestercity.automacao;

import br.com.manchestercity.automacao.FrameworkDefaults.InputType;

public class InputMaxLengthException extends Exception {

	private static final long serialVersionUID = 4998794970615908763L;

	String inputId;
	String inputName;

	InputType type;

	public InputMaxLengthException(String id, String name, InputType type) {
		super();

		this.inputId = id;
		this.inputName = name;

		this.type = type;
	}

	public String getMessage() {
		String message = "INPUT TIPO \"" + this.type.name() + "\" DE ID: \"" + this.inputId + "\" E NAME: \"" + this.inputName + "\" NÃO POSSUI MAXLENGTH DEFINIDO!!!\n";
		message += super.getMessage();

		return message;
	}
}
