package com.enotes.Enotes_INDUS.model;



//id	int	NO
//upload_file_name	varchar(255)	NO
//original_file_name	varchar(255)	NO
//display_file_name	varchar(255)	NO
//file_path	varchar(255)	NO
//file_size	double	YES

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FileDetails {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Integer id;
    private  String uploadFileName;
    private  String originalFileName;
    private  String displayFileName;
    private  String filePath;
    private Long fileSize;
}
