package main.api.response.result;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OkResultResponse {
    private boolean result;
}