package com.project.oag.app.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailDetail {
    private String recipient;
    private String message;
    private String subject;
    private String attachment;
	private String sender;
}
