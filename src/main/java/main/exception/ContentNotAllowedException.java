package main.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContentNotAllowedException extends Exception {
    Map<String, String> errors;

    public ContentNotAllowedException createWith(String string, String stringMap) {
        errors.put(string, stringMap);
        return new ContentNotAllowedException(errors);
    }

}