package com.mrbpurnachandra.sponews.controller;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.HttpMethod;
import com.google.cloud.storage.Storage;
import com.mrbpurnachandra.sponews.model.Author;
import com.mrbpurnachandra.sponews.model.ImageInfo;
import com.mrbpurnachandra.sponews.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/upload/image")
public class ImageUploadController {
    private static final String BUCKET_NAME = "file-upload-1332c.appspot.com";
    private final Storage storage;
    private final AuthorService authorService;

    @Autowired
    public ImageUploadController(Storage storage, AuthorService authorService) {
        this.storage = storage;
        this.authorService = authorService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Map<String, String>> uploadImage(ImageInfo imageInfo, OAuth2AuthenticationToken authentication) {
        String email = authentication.getPrincipal().getAttribute("email");
        Optional<Author> optionalAuthor = authorService.findAuthorByEmail(email);

        if (optionalAuthor.isEmpty()) {
            return ResponseEntity.status(HttpStatusCode.valueOf(403)).build();
        }

        String blobName = UUID.randomUUID().toString();
        BlobInfo blobInfo = BlobInfo.newBuilder(BUCKET_NAME, blobName).setContentType(imageInfo.type()).build();

        Map<String, String> extensionHeaders = new HashMap<>();

        extensionHeaders.put("Content-Type", imageInfo.type());

        URL url = storage.signUrl(blobInfo, 10, TimeUnit.MINUTES, Storage.SignUrlOption.httpMethod(HttpMethod.PUT), Storage.SignUrlOption.withExtHeaders(extensionHeaders), Storage.SignUrlOption.withV4Signature());

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("name", blobName);
        responseBody.put("signedURL", url.toString());
        responseBody.put("bucket", BUCKET_NAME);

        return ResponseEntity.ok(responseBody);
    }
}
