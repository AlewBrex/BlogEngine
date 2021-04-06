package main.api.response.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.api.response.ResultResponse;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageUploadResponse implements ResultResponse {
  private String pathUpload;
}
