package com.example.sqs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Msg {
	
	private String id;
    private String body;
    private String name;
    
    @JsonCreator
    public Msg (@JsonProperty("id")String id, 
    					@JsonProperty("body") String body, 
    					@JsonProperty("name") String name) {
    	this.id = id;
    	this.body = body;
    	this.name = name;
    }

	public String getId() {
		return id;
	}

	public String getBody() {
		return body;
	}

	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return "{\"id\":\"" + id + "\", \"body\":\"" + body + "\", \"name\":\"" + name + "\"}";
	}
	
	
}
