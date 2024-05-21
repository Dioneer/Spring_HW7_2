package Pegas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/api/v1/image")
public class ImageController {
    @Autowired
    @Value("${app.image.path}")
    private String bucket;

    @GetMapping
    public ResponseEntity<byte[]> findAvatar(){
        Path fullPath = Path.of(bucket, "skunk.jpg");
        byte[] bytes = new byte[0];
        try {
            bytes = Files.readAllBytes(fullPath);
        } catch (RuntimeException | IOException e){
            System.out.println("Error");
        }
        return new ResponseEntity<>(bytes, HttpStatus.OK);
    }
}
