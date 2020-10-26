package main.api.response.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Map;

@Data
@AllArgsConstructor
public class BadResultResponse
{
    private OkResultResponse result;
    private Map<String, String> errors;
}