package com.example.sendmessage.model;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Email {
	
    private String subject;
	 
	private String from;
	
	private List<String> to;
	
	private String body;
}