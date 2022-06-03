package no.ntnu.bicycle.model;


import lombok.Data;

import java.util.Map;

//!TODO can you write documentation for this?
@Data
public class Email {
    String to;
    String subject;
    String text;
    String template;

    Map<String, Object> properties;
}
