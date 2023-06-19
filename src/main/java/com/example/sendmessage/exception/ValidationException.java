package com.example.sendmessage.exception;

import java.util.ArrayList;
import java.util.List;

import com.example.sendmessage.enums.TypeMessageEnum;

public class ValidationException extends BusinessException {

	private static final long serialVersionUID = 1L;

	private List<String> errors;
	private TypeMessageEnum typeMessageEnum;

	public ValidationException(List<String> errors) {
		super(errors.toString());
		this.errors = errors;
	}

	public ValidationException(List<String> errors, TypeMessageEnum typeMessageEnum) {
		super(errors.toString());
		this.errors = errors;
		this.typeMessageEnum = typeMessageEnum;
	}


	public ValidationException(String erros) {
		super(erros);
		if(errors == null) {
			errors = new ArrayList<String>();
		}
		errors.add(erros);
	}

	public List<String> getErrors() {
		return errors;
	}

	public TypeMessageEnum getTypeMessageEnum() {
		return typeMessageEnum;
	}


//    private List<String> errors;
//    private TypeMessageEnum typeMessageEnum;
//
//    public ValidationException(List<String> errors) {
//    	super(errors.toString());
//    	this.errors = errors;
//    	//System.out.println(this.errors);
//    }
//
//    public ValidationException(List<String> errors, TypeMessageEnum typeMessageEnum) {
//		super();
//		this.errors = errors;
//		this.typeMessageEnum = typeMessageEnum;
//	}
//
//	public ValidationException(String erro) {
//		super();
//		if(errors == null) {
//			errors = new ArrayList<String>();
//		}
//		errors.add(erro);
//	}
//
//	public List<String> getErrors() {
//        return errors;
//    }
//
//	public TypeMessageEnum getTypeMessageEnum() {
//		return typeMessageEnum;
//	}

}