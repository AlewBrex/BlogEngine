package main.api.response.post;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import main.api.response.ResultResponse;

@Data
@RequiredArgsConstructor
public class ImageUploadResponse implements ResultResponse {
  private String pathUpload;
}
