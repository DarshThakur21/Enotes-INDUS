package com.enotes.Enotes_INDUS.dto;


import com.enotes.Enotes_INDUS.model.FileDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Builder
@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileDownloadDto {
  private  FileDetails fileDetails;
  private byte []  fileData;

}
